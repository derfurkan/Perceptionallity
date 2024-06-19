package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Manager;
import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.discord.RPCStates;
import de.furkan.perceptionallity.game.entity.EntityAttributes;
import de.furkan.perceptionallity.game.entity.environment.GameCampfire;
import de.furkan.perceptionallity.game.entity.npc.GameNPC;
import de.furkan.perceptionallity.game.entity.npc.TestNPC;
import de.furkan.perceptionallity.game.entity.player.GamePlayer;
import de.furkan.perceptionallity.menu.components.label.MenuLabel;
import de.furkan.perceptionallity.util.font.GameFont;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.Timer;
import lombok.Getter;
import lombok.Setter;

@Getter
public class GameManager extends Manager {

  private final Camera camera = new Camera();
  private final List<GameObject> gameObjects = Collections.synchronizedList(new ArrayList<>());
  private final List<GameKeyEvent> keyEvents = new ArrayList<>();
  private final List<GameAction> loopCalls = new ArrayList<>();
  private final List<GameNPC> gameNPCs = new ArrayList<>();
  private final Timer gameTimer;
  private final Set<Integer> pressedKeys = new HashSet<>();
  private final int GAME_UPDATE_MS = 15;


  @Getter
  private final int DISTANCE_UNTIL_DISPOSE =
      2000; // The min distance afar from the Player until a GameObject is being disposed from

  // rendering

  @Setter private boolean gamePaused = false;

  private GameState gameState = GameState.NONE;
  private GamePlayer currentPlayer;
  private long updatesPassed = 0;
  private MenuLabel statsLabel, objectLabel,locationLabel;


  public GameManager() {
    gameTimer =
        new Timer(
            GAME_UPDATE_MS,
            e -> {
              if (gamePaused) return;
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

            var cameraRectangle = new Rectangle();
            cameraRectangle.setBounds(
                getCamera().getCameraViewLocation().getX(),
                getCamera().getCameraViewLocation().getY(),
                getGame().getWINDOW_WIDTH(),
                getGame().getWINDOW_HEIGHT());

            //               Remove objects which are not in camera view (Using Multi-Threaded
            // rendering
            //             calculations)
            List<List<GameObject>> splitGameObjectLists = splitArrayList(getGameObjects(), 100);
            List<GameObject> validGameObjects = Collections.synchronizedList(new ArrayList<>());

            ExecutorService executorService =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            splitGameObjectLists.forEach(
                gameObjects -> {
                  executorService.submit(
                      () -> {
                        gameObjects.forEach(
                            gameObject -> {
                                gameObject.getLastLocation().set(gameObject.getWorldLocation());

                                gameObject.getWorldLocation().applyVelocity(gameObject.getCurrentVelocity());

                                if (gameObject.buildRectangle().intersects(cameraRectangle)) {
                                  validGameObjects.add(gameObject);
                              } else {
                                  getGame().getGamePanel().remove(gameObject.getComponent());
                              }
                            });
                      });
                });
            executorService.shutdown();
            try {
              executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
              getGame().handleFatalException(e);
            }

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
                                getGame().handleFatalException(e);
                              }
                            }));

            // GameObject Pass
            validGameObjects.forEach(
                (gameObject) -> {

                  if (Arrays.stream(getGame().getGamePanel().getComponents())
                      .noneMatch(component -> component == gameObject.getComponent())) {
                    getGame().getGamePanel().add(gameObject.getComponent(),gameObject.getObjectLayer());
                  }

                  // GameObject Collision Pass
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

                  // GameObject Animation Pass

                  if (gameObject.getCurrentPlayingAnimation() != null) {

                    if ((updatesPassed
                            % ((1000 / GAME_UPDATE_MS)
                                / gameObject.getCurrentPlayingAnimation().getFramesPerSecond())
                        == 0)) {

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
            var frameTime = (System.currentTimeMillis() - startTime);

            if (frameTime < GAME_UPDATE_MS) {
              try {
                Thread.sleep(GAME_UPDATE_MS - frameTime);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            }

            frameTime = (System.currentTimeMillis() - startTime);

            if (updatesPassed % (100 / GAME_UPDATE_MS) == 0 && getGame().isDebug()) {
              statsLabel.setText(
                  1000 / frameTime
                      + " fps, "
                      + frameTime
                      + " ms");
              objectLabel.setText(validGameObjects.size()+" / "+new DecimalFormat("#,###").format(gameObjects.size()) + " Objects");

              locationLabel.setText(currentPlayer.getWorldLocation().getX() + " X, " + currentPlayer.getWorldLocation().getY()+" Y");

              objectLabel.recalculateDimension();
              objectLabel.buildComponent();
              statsLabel.recalculateDimension();
              statsLabel.buildComponent();
                locationLabel.recalculateDimension();
                locationLabel.buildComponent();
            }
            startTime = System.currentTimeMillis();

            getGame().getGamePanel().repaint();
            getGame().getGamePanel().revalidate();
          }
        });
  }

  @Override
  public void initialize() {

    new Thread(
            () -> {
              try {
                getLogger().info("Initializing game");

                statsLabel =
                    new MenuLabel(
                        3,
                        3,
                        "",
                        20,
                        Color.BLACK,
                        getResourceManager().getResource("ingame_font", GameFont.class));


                  objectLabel =
                          new MenuLabel(
                                  3,
                                  0,
                                  "",
                                  20,
                                  Color.BLACK,
                                  getResourceManager().getResource("ingame_font", GameFont.class));
                  locationLabel =
                          new MenuLabel(
                                  3,
                                  0,
                                  "",
                                  20,
                                  Color.BLACK,
                                  getResourceManager().getResource("ingame_font", GameFont.class));
                  objectLabel.setBelow(statsLabel,0);
                  locationLabel.setBelow(objectLabel,0);
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

                int distance = 10000;

                for (int i = 0; i < 2_000; i++) {
                  GameCampfire gameCampfire =
                      new GameCampfire(
                          new WorldLocation(
                              ThreadLocalRandom.current().nextInt(-distance, distance),
                              ThreadLocalRandom.current().nextInt(-distance, distance)));
                  gameCampfire.initializeGameObject(1);
                }

                TestNPC testNPC = new TestNPC(new WorldLocation(100, 100));
                testNPC.initializeGameObject(1);

                currentPlayer = new GamePlayer(new WorldLocation(-20, -20), true);
                currentPlayer.setAttribute(EntityAttributes.MOVEMENT_SPEED, 5);
                currentPlayer.setAttribute(EntityAttributes.RUN_SPEED_FACTOR, 5);
                currentPlayer.registerKeyEvent();
                currentPlayer.initializeGameObject(1);

                testNPC.setCollisionBoundaries(new Dimension(30, 40));
                currentPlayer.setCollisionBoundaries(new Dimension(30, 40));

                currentPlayer.setOnCollision(
                    () -> {
                      //
                      // currentPlayer.getWorldLocation().set(currentPlayer.getLastLocation());
                      //                    camera.flushCalculation();
                    });

                camera.centerOnObject(currentPlayer);

                Perceptionallity.getDiscordRPCHandler().setState(RPCStates.IN_GAME, "Test Area");

                // When everything is loaded and in place we start the actual game loop
                setGameState(GameState.IN_GAME);
                getLogger().info("Initialized game");
                startGameLoop();
                getGame().getGamePanel().add(statsLabel.getJComponent(), 0);
                getGame().getGamePanel().add(objectLabel.getJComponent(), 0);
                getGame().getGamePanel().add(locationLabel.getJComponent(), 0);
                getGame().getGamePanel().setBackground(Color.WHITE);

                Perceptionallity.getGame().getMenuManager().getCurrentMenu().unLoadMenu();
              } catch (Exception e) {
                getGame().handleFatalException(e);
              }
            })
        .start();
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
        .orElseThrow(() -> {
            RuntimeException runtimeException = new RuntimeException("GameObject not found by Component");
            getGame().handleFatalException(runtimeException);
            return runtimeException;
        });
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


    private <T> List<List<T>> splitArrayList(List<T> list, int chunkSize) {
        List<List<T>> chunks = new ArrayList<>();

        if (chunkSize == 0) {
            chunks.add(list);
            return chunks;
        }

        int listSize = list.size();

        for (int i = 0; i < listSize; i += chunkSize) {
            int end = Math.min(listSize, i + chunkSize);
            chunks.add(new ArrayList<>(list.subList(i, end)));
        }

        return chunks;
    }

}
