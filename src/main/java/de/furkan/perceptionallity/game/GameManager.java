package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Manager;
import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.game.entity.EntityAttributes;
import de.furkan.perceptionallity.game.entity.environment.GameCampfire;
import de.furkan.perceptionallity.game.entity.npc.GameNPC;
import de.furkan.perceptionallity.game.entity.npc.TestNPC;
import de.furkan.perceptionallity.game.entity.player.GamePlayer;
import de.furkan.perceptionallity.game.lighting.GameLightingManager;
import de.furkan.perceptionallity.menu.components.label.MenuLabel;
import de.furkan.perceptionallity.util.font.GameFont;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class GameManager extends Manager {

    private final Camera camera = new Camera();
    private final GameLightingManager lightingManager = new GameLightingManager();
    private final List<GameObject> gameObjects = Collections.synchronizedList(new ArrayList<>());
    private final List<GameKeyEvent> keyEvents = new ArrayList<>();
    private final List<GameAction> gameThreadLoopCalls = new ArrayList<>();
    public final List<GameNPC> gameNPCs = new ArrayList<>();
    private final TimerTask gameLogicTimer;
    private final Timer gameLogicTimerExecutor;
    private final Set<Integer> pressedKeys = ConcurrentHashMap.newKeySet();
    private final int GAME_UPDATE_MS = 15; // Fixed timestep for game logic (physics, collisions, etc.)

    @Getter
    private final int DISTANCE_UNTIL_DISPOSE =
            2000; // The min distance afar from the Player until a GameObject is being disposed from


    @Setter
    private boolean gamePaused = false;

    private GameState gameState = GameState.NONE;
    private GamePlayer currentPlayer;
    private long updatesPassed = 0;
    private MenuLabel statsLabel, objectLabel, locationLabel;

    public GameManager() {

        // Game Logic Timer - Fixed timestep (GAME_UPDATE_MS)
        gameLogicTimer =
                new TimerTask() {
                    @Override
                    public void run() {
                        if (gamePaused) return;
                        try {
                            gameThreadLoopCalls.forEach(GameAction::onAction);
                        } catch (Exception exception) {
                            Perceptionallity.handleFatalException(exception);
                        }
                    }
                };

        gameLogicTimerExecutor = new Timer("GameLogicThread");
    }

    @Override
    public void initialize() {

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
        objectLabel.setBelow(statsLabel, 0);
        locationLabel.setBelow(objectLabel, 0);

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

                                keyEvents.stream()
                                        .filter(
                                                gameKeyEvent ->
                                                        Arrays.stream(gameKeyEvent.getKeyRegister())
                                                                .anyMatch(integer -> integer == e.getKeyCode()))
                                        .forEach(
                                                gameKeyEvent -> {
                                                    gameKeyEvent.getPressedKeys().remove((Object) e.getKeyCode());
                                                    gameKeyEvent.getKeyListener().keyReleased(e);
                                                });
                                pressedKeys.remove(e.getKeyCode());
                            }
                        });

        // Delete this after testing:

        int distance = 2000;

        for (int i = 0; i < 15; i++) {
            GameCampfire gameCampfire =
                    new GameCampfire(
                            new WorldLocation(
                                    ThreadLocalRandom.current().nextInt(-distance, distance),
                                    ThreadLocalRandom.current().nextInt(-distance, distance)));
            gameCampfire.initializeGameObject(1);
        }

        TestNPC testNPC = new TestNPC(new WorldLocation(100, 100));
        testNPC.initializeGameObject(1);

        currentPlayer = new GamePlayer(new WorldLocation(-20, -20), false);
        currentPlayer.setAttribute(EntityAttributes.MOVEMENT_SPEED, 5);
        currentPlayer.setAttribute(EntityAttributes.RUN_SPEED_FACTOR, 5);
        currentPlayer.registerKeyEvent();
        currentPlayer.initializeGameObject(2);

        testNPC.setCollisionBoundaries(new Dimension(30, 40));
        currentPlayer.setCollisionBoundaries(new Dimension(30, 40));

        currentPlayer.setOnCollision(
                () -> {
                    //
                    // currentPlayer.getWorldLocation().set(currentPlayer.getLastLocation());
                    //                    camera.flushCalculation();
                });

        camera.centerOnObject(currentPlayer);

        // Register Game Logic Actions
        registerLoopAction(
                new GameAction() {
                    @Override
                    public void onAction() {
                        if (gamePaused) return;

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
                                                            gameKeyEvent.getKeyListener().whileKeyPressed(integer);
                                                        }));

                        // Lighting Flicker Pass
                        lightingManager.updateFlicker();

                        // GameObject Physics & Velocity Pass
                        gameObjects.forEach(
                                (gameObject) -> {
                                    gameObject.getWorldLocation().applyVelocity(gameObject.getCurrentVelocity());

                                    // GameObject Animation Pass (Fixed timestep based on GAME_UPDATE_MS)
                                    if (gameObject.getCurrentPlayingAnimation() != null) {
                                        if ((updatesPassed
                                                % ((1000 / GAME_UPDATE_MS)
                                                / gameObject.getCurrentPlayingAnimation().getFramesPerSecond())
                                                == 0)) {
                                            gameObject.getCurrentPlayingAnimation().nextFrame();
                                        }
                                    }
                                });
                    }
                });

        // When everything is loaded and in place we start the actual game loop
        setGameState(GameState.IN_GAME);
        getLogger().info("Initialized game");
        startGameLoop();
        getGame().getGameRenderer().startRenderingLoop();
        getGame().getGameRenderer().add(lightingManager.getGlowComponent(), Integer.valueOf(GameLightingManager.GLOW_LAYER));
        getGame().getGameRenderer().add(lightingManager.getDarknessComponent(), Integer.valueOf(GameLightingManager.DARKNESS_LAYER));
        getGame().getGameRenderer().add(statsLabel.getJComponent(), Integer.valueOf(3));
        getGame().getGameRenderer().add(objectLabel.getJComponent(), Integer.valueOf(3));
        getGame().getGameRenderer().add(locationLabel.getJComponent(), Integer.valueOf(3));
        getGame().getGameRenderer().setBackground(Color.WHITE);

        Perceptionallity.getGame().getMenuManager().getCurrentMenu().unLoadMenu();
    }

    public void registerLoopAction(GameAction gameAction) {
        gameThreadLoopCalls.add(gameAction);
    }

    public void registerKeyEvent(GameKeyEvent gameKeyListener) {
        getLogger()
                .info("Registered new KeyEvent " + Arrays.toString(gameKeyListener.getKeyRegister()));
        keyEvents.add(gameKeyListener);
    }

    private void startGameLoop() {
        getLogger().info("Started game logic loop");
        gameLogicTimerExecutor.scheduleAtFixedRate(gameLogicTimer, 0, GAME_UPDATE_MS);
    }

    private void stopGameLoop() {
        getLogger().info("Stopped game logic loop");
        gameLogicTimerExecutor.cancel();
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
