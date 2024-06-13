package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Manager;
import de.furkan.perceptionallity.game.entity.EntityAttributes;
import de.furkan.perceptionallity.game.entity.npc.GameNPC;
import de.furkan.perceptionallity.game.entity.npc.TestNPC;
import de.furkan.perceptionallity.game.entity.player.GamePlayer;
import de.furkan.perceptionallity.menu.components.MenuLabel;
import de.furkan.perceptionallity.util.font.GameFont;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.swing.Timer;
import lombok.Getter;

@Getter
public class GameManager extends Manager {

  private final Camera camera = new Camera();

  private final List<GameObject> gameObjects = new ArrayList<>();

  private final List<GameKeyEvent> keyEvents = new ArrayList<>();
  private final List<GameAction> loopCalls = new ArrayList<>();

  private final List<GameNPC> gameNPCs = new ArrayList<>();
  private final Timer gameTimer;
  private final Set<Integer> pressedKeys = new HashSet<>();
  private final int GAME_UPDATE_MS = 15;
  private GameState gameState = GameState.NONE;
  private GamePlayer currentPlayer;
  private long updatesPassed = 0;
  private MenuLabel statsLabel;

  public GameManager() {
    gameTimer =
        new Timer(
            GAME_UPDATE_MS,
            e -> {
              try {
                loopCalls.forEach(GameAction::onAction);
              } catch (Exception exception) {
                getGame().handleFatalException(exception);
              }
            });

    // Each frame render loop
    registerLoopAction(
        new GameAction() {
          long startTime;

          @Override
          public void onAction() {

            // Just in case if a player has no real-life:
            if (updatesPassed == Long.MAX_VALUE) updatesPassed = 0;
            updatesPassed += 1;

            // KeyEvent Pass
            pressedKeys.forEach(
                integer ->
                    keyEvents.stream()
                        .filter(
                            gameKeyEvent ->
                                Arrays.asList(gameKeyEvent.getKeyRegister()).contains(integer))
                        .forEach(
                            gameKeyEvent -> {
                              if (!gameKeyEvent.getPressedKeys().contains(integer))
                                gameKeyEvent.getPressedKeys().add(integer);
                              try {
                                gameKeyEvent.getKeyListener().whileKeyPressed(integer);
                              } catch (Exception e) {
                                getGame().handleFatalException(new RuntimeException(e));
                              }
                            }));

            // GameObject Pass
            gameObjects.forEach(
                (gameObject) -> {
                  gameObject.getLastLocation().set(gameObject.getWorldLocation());

                  gameObject.getWorldLocation().applyVelocity(gameObject.getCurrentVelocity());

                  if (gameObject.isPassToCollisionCheck() && gameObject.getOnCollision() != null) {
                    if (gameObject.getCollisionBoundaries() == null) {
                      getLogger()
                          .warning(
                              "This GameObject does not have collision boundaries set ("
                                  + gameObject.getClass().getSimpleName()
                                  + ")");
                    }
                    CompletableFuture<Boolean> collisionFuture = new CompletableFuture<>();
                    getGame().getGamePanel().passToCollisionCheck(gameObject, collisionFuture);

                    collisionFuture.whenComplete(
                        (isColliding, throwable) -> {
                          if (isColliding) {
                            gameObject.getOnCollision().onAction();
                          }
                        });
                  }

                  // Animation Pass
                  if (gameObject.getCurrentPlayingAnimation() != null) {
                    if ((updatesPassed
                                % ((1000 / GAME_UPDATE_MS)
                                    / gameObject.getCurrentPlayingAnimation().getFramesPerSecond())
                            == 0)
                        || gameObject.getCurrentPlayingAnimation().isFresh()) {

                      // Unga bunga workaround:
                      if (gameObject.getCurrentPlayingAnimation().isFresh()) {
                        if (gameObject.getLastPlayedAnimation() != null)
                          gameObject.getLastPlayedAnimation().resetAnimation();
                        gameObject.getCurrentPlayingAnimation().setFresh(false);
                      }

                      gameObject.getCurrentPlayingAnimation().nextFrame();
                      gameObject
                          .getComponent()
                          .setIcon(
                              gameObject
                                  .getCurrentPlayingAnimation()
                                  .getCurrentFrame()
                                  .getRawImageIcon());
                    }
                  }
                });

            // Telemetry Pass
            if (updatesPassed % (100 / GAME_UPDATE_MS) == 0 && getGame().isDebug()) {
              var frameTime = (System.currentTimeMillis() - startTime);
              statsLabel.setText(1000 / frameTime + " fps, " + frameTime + " ms");
              statsLabel.recalculateDimension();
              statsLabel.buildComponent();
            }
            startTime = System.currentTimeMillis();

            getGame().getGamePanel().repaint();
            getGame().getGamePanel().revalidate();
          }
        });
  }

  @Override
  public void initialize() throws Exception {
    getLogger().info("Initializing game");

    statsLabel =
        new MenuLabel(
            3,
            3,
            "0 fps, 0 ms",
            20,
            Color.BLACK,
            getResourceManager().getResource("ingame_font", GameFont.class));

    getGame().getGamePanel().add(statsLabel.getJComponent(), 0);

    getGame().getGamePanel().setBackground(Color.WHITE);
    getGame()
        .getGameFrame()
        .addKeyListener(
            new java.awt.event.KeyListener() {
              @Override
              public void keyTyped(KeyEvent e) {}

              @Override
              public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
              }

              @Override
              public void keyReleased(KeyEvent e) {

                keyEvents.stream()
                    .filter(
                        gameKeyEvent ->
                            Arrays.stream(gameKeyEvent.getKeyRegister())
                                .anyMatch(integer -> integer == e.getKeyCode()))
                    .forEach(
                        gameKeyEvent -> {
                          gameKeyEvent.getPressedKeys().remove((Object) e.getKeyCode());
                          try {
                            gameKeyEvent.getKeyListener().keyReleased(e);
                          } catch (Exception ex) {
                            getGame().handleFatalException(new RuntimeException(ex));
                          }
                        });
                pressedKeys.remove(e.getKeyCode());
              }
            });

    // Delete this after testing:
    TestNPC testNPC = new TestNPC(new WorldLocation(100, 100));
    testNPC.initializeGameObject(1);

    currentPlayer = new GamePlayer(new WorldLocation(20, 20), true);
    currentPlayer.setAttribute(EntityAttributes.MOVEMENT_SPEED, 5);
    currentPlayer.setAttribute(EntityAttributes.RUN_SPEED_FACTOR, 5);
    currentPlayer.registerKeyEvent();
    currentPlayer.initializeGameObject(1);

    testNPC.setCollisionBoundaries(new Dimension(30, 40));
    currentPlayer.setCollisionBoundaries(new Dimension(30, 40));

    currentPlayer.setOnCollision(
        () -> {
          currentPlayer.getWorldLocation().set(currentPlayer.getLastLocation());
          camera.flushCalculation();
        });

    camera.centerOnObject(currentPlayer);

    // When everything is loaded and in place we start the actual game loop
    setGameState(GameState.IN_GAME);
    getLogger().info("Initialized game");
    startGameLoop();
  }

  public void registerNPC(GameNPC gameNPC) {
    gameNPCs.add(gameNPC);
  }

  public void unregisterNPC(GameNPC gameNPC) {
    gameNPCs.remove(gameNPC);
  }

  public void registerLoopAction(GameAction gameAction) {
    loopCalls.add(gameAction);
  }

  public void registerKeyEvent(GameKeyEvent gameKeyListener) {
    getLogger()
        .info("Registered new KeyEvent " + Arrays.toString(gameKeyListener.getKeyRegister()));
    keyEvents.add(gameKeyListener);
  }

  private void startGameLoop() {
    getLogger().info("Started game loop");
    gameTimer.start();
  }

  private void stopGameLoop() {
    getLogger().info("Stopped game loop");
    gameTimer.stop();
  }

  public boolean isGameComponent(Component component) {
    return gameObjects.stream().anyMatch(gameObject -> gameObject.getComponent() == component);
  }

  public GameObject getGameObjectByComponent(Component component) {
    return gameObjects.stream()
        .filter(gameObject -> gameObject.getComponent() == component)
        .findFirst()
        .orElseThrow();
  }

  public boolean isGameState(GameState gameState) {
    return this.gameState == gameState;
  }

  public void setGameState(GameState gameState) {
    getLogger()
        .info("Setting new GameState (" + this.gameState.name() + " -> " + gameState.name() + ")");
    this.gameState = gameState;
  }

  public void registerGameObject(GameObject gameObject) {
    getLogger().info("Registered new GameObject (" + gameObject.getClass().getSimpleName() + ")");
    gameObjects.add(gameObject);
  }

  public void unregisterGameObject(GameObject gameObject) {
    getLogger().info("Unregistered GameObject (" + gameObject.getClass().getSimpleName() + ")");
    gameObjects.remove(gameObject);
  }
}
