package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Manager;
import de.furkan.perceptionallity.game.entity.player.GamePlayer;
import de.furkan.perceptionallity.menu.components.MenuLabel;
import de.furkan.perceptionallity.util.font.GameFont;
import lombok.Getter;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Getter
public class GameManager extends Manager {

    private final Camera camera = new Camera();

    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<GameKeyEvent> keyEvents = new ArrayList<>();
    private final List<GameAction> loopCalls = new ArrayList<>();
    private final Timer gameTimer;

    private final Set<Integer> pressedKeys = new HashSet<>();

    private final int GAME_UPDATE_MS = 16;


    private long updatesPassed = 0;
    private MenuLabel statsLabel;

    public GameManager() {
        gameTimer = new Timer(GAME_UPDATE_MS, e -> loopCalls.forEach(GameAction::onAction));

        // Each frame render loop
        registerLoopAction(new GameAction() {
            long startTime;

            @Override
            public void onAction() {

                // Just in case if a player has no real-life:
                if (updatesPassed == Long.MAX_VALUE) updatesPassed = 0;
                updatesPassed += 1;

                // KeyEvent pass
                pressedKeys.forEach(integer -> {
                    keyEvents.stream().filter(gameKeyEvent -> Arrays.asList(gameKeyEvent.getKeyRegister()).contains(integer)).forEach(gameKeyEvent -> {

                        if(!gameKeyEvent.getPressedKeys().contains(integer))
                            gameKeyEvent.getPressedKeys().add(integer);

                        gameKeyEvent.getKeyListener().whileKeyPressed(integer);
                    });
                });


                // GameObject Pass
                gameObjects.forEach(
                        (gameObject) -> {

                            gameObject.getLastLocation().set(gameObject.getWorldLocation());
                            gameObject.getWorldLocation().applyVelocity(gameObject.getCurrentVelocity());

                            if (gameObject.isPassToCollisionCheck() && gameObject.getOnCollision() != null) {
                                if (gameObject.getCollisionBoundaries() == null) {
                                    getLogger().warning("This GameObject does not have collision boundaries set (" + gameObject.getClass().getSimpleName() + ")");
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

                            if (gameObject.getCurrentPlayingAnimation() != null) {
                                if ((updatesPassed
                                        % ((1000 / GAME_UPDATE_MS)
                                        / gameObject.getCurrentPlayingAnimation().getFramesPerSecond())
                                        == 0)
                                        || gameObject.isAnimationFresh()) {
                                    if (gameObject.isAnimationFresh()) gameObject.setAnimationFresh(false);

                                    gameObject
                                            .getComponent()
                                            .setIcon(
                                                    gameObject
                                                            .getCurrentPlayingAnimation()
                                                            .getCurrentFrame()
                                                            .getRawImageIcon());

                                    gameObject.getCurrentPlayingAnimation().nextFrame();
                                }
                            }


                        });

                if (updatesPassed % (100 / GAME_UPDATE_MS) == 0) {
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
    public void initialize() {
        statsLabel = new MenuLabel(3, 3, "0 fps, 0 ms", 20, Color.BLACK, getResourceManager().getResource("ingame_font", GameFont.class));
        getGame().getGamePanel().add(statsLabel.getJComponent(), 0);
        startGameLoop();

        getGame().getGamePanel().setBackground(Color.WHITE);
        getGame()
                .getGameFrame()
                .addKeyListener(
                        new java.awt.event.KeyListener() {
                            @Override
                            public void keyTyped(KeyEvent e) {

                            }

                            @Override
                            public void keyPressed(KeyEvent e) {
                                pressedKeys.add(e.getKeyCode());
                            }

                            @Override
                            public void keyReleased(KeyEvent e) {

                                keyEvents.stream().filter(gameKeyEvent -> Arrays.stream(gameKeyEvent.getKeyRegister()).anyMatch(integer -> integer == e.getKeyCode())).forEach(gameKeyEvent -> {
                                        gameKeyEvent.getPressedKeys().remove((Object)e.getKeyCode());
                                        gameKeyEvent.getKeyListener().keyReleased(e);
                                });
                                pressedKeys.remove(e.getKeyCode());
                            }


                        });

        GamePlayer gamePlayer1 = new GamePlayer(new WorldLocation(170, 170), false);
        gamePlayer1.initializeGameObject(2);


        GamePlayer gamePlayer = new GamePlayer(new WorldLocation(20, 20), true);
        gamePlayer.registerKeyEvent();
        gamePlayer.initializeGameObject(1);

//        gamePlayer1.setCollisionBoundaries(new Dimension(30,40));
//        gamePlayer.setCollisionBoundaries(new Dimension(30,40));
//
//        gamePlayer.setOnCollision(() -> {
//            gamePlayer.getWorldLocation().set(gamePlayer.getLastLocation());
//            camera.flushCalculation();
//        });

        camera.centerOnObject(gamePlayer);
    }

    public void registerLoopAction(GameAction gameAction) {
        loopCalls.add(gameAction);
    }

    public void registerKeyEvent(GameKeyEvent gameKeyListener) {
        getLogger().info("Registered new KeyEvent " + Arrays.toString(gameKeyListener.getKeyRegister()));
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
                .get();
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
