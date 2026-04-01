package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GameRenderer extends JLayeredPane {

    private final HashMap<GameObject, CompletableFuture<Boolean>> collisionCheck = new HashMap<>();
    @lombok.Getter
    private final GameDebugOverlay debugOverlay = new GameDebugOverlay();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        debugOverlay.setBounds(0, 0, getWidth(), getHeight());
    }

    @Override
    public void paint(Graphics g) {
        if (getGameManager().isGameState(GameState.IN_GAME)) {
            getGameManager().getLightingManager().buildLightMap(
                    getCamera(),
                    getWidth(),
                    getHeight());
        }
        super.paint(g);
    }

    private volatile boolean renderingActive = false;

    public void startRenderingLoop() {
        renderingActive = true;
        Thread renderThread = new Thread(() -> {
            long lastFrameTime = System.currentTimeMillis();
            GameManager gameManager = getGameManager();
            while (renderingActive) {
                long now = System.currentTimeMillis();
                double frameTime = now - lastFrameTime;
                lastFrameTime = now;
                if (frameTime > 0) {
                    debugOverlay.setLastFrameTimeMs(frameTime);
                }

                // Render Pass
                gameManager.getGameObjects().forEach((gameObject) -> {
                    if (gameObject.getCurrentPlayingAnimation() != null && gameObject.getComponent() != null && gameObject.getCurrentPlayingAnimation().getCurrentFrame() != null) {
                        gameObject.getComponent().setIcon(
                                gameObject.getCurrentPlayingAnimation().getCurrentFrame().getRawImageIcon());
                    }
                });

                getCamera().flushCalculation();

                gameManager.getGameObjects().forEach(gameObject -> getCamera().finishGameObject(gameObject, getCamera().calculateObjectPosition(gameObject)));

                // Render final game objects
                getCamera()
                        .getCalculatedGameObjects()
                        .forEach(
                                (gameObject, newLoc) ->
                                        gameObject
                                                .getComponent()
                                                .setBounds(
                                                        newLoc[0],
                                                        newLoc[1],
                                                        (int) gameObject.getDimension().getWidth(),
                                                        (int) gameObject.getDimension().getHeight()));

                // Depth sort pass - Y-based ordering for 2.5D effect
                List<GameObject> sortable = new ArrayList<>();
                for (GameObject obj : gameManager.getGameObjects()) {
                    if (obj.isDepthSortable()) {
                        sortable.add(obj);
                    }
                }
                sortable.sort(Comparator.comparingInt(GameObject::getDepthSortY));
                for (int i = 0; i < sortable.size(); i++) {
                    setPosition(sortable.get(sortable.size() - 1 - i).getComponent(), i);
                }

                // Lighting
                gameManager.getLightingManager().getGlowComponent().setBounds(0, 0, getWidth(), getHeight());
                gameManager.getLightingManager().getDarknessComponent().setBounds(0, 0, getWidth(), getHeight());
                debugOverlay.setBounds(0, 0, getWidth(), getHeight());
                repaint();

                try {
                    Thread.sleep(6); // Minimal sleep to prevent 100% CPU usage
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "RenderingThread");
        renderThread.setDaemon(true);
        renderThread.start();
    }

    public void stopRenderingLoop() {
        renderingActive = false;
    }

    public Camera getCamera() {
        return Perceptionallity.getGame().getGameManager().getCamera();
    }

    public GameManager getGameManager() {
        return Perceptionallity.getGame().getGameManager();
    }
}
