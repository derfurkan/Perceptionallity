package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class GameRenderer extends JLayeredPane {

    private final HashMap<GameObject, CompletableFuture<Boolean>> collisionCheck = new HashMap<>();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
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
            long lastFrameTime = 0;
            double frameTime;
            GameManager gameManager = getGameManager();
            while (renderingActive) {

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

                // Update UI Labels
                if (gameManager.getUpdatesPassed() % (100 / gameManager.getGAME_UPDATE_MS()) == 0 && Perceptionallity.getGame().isDebug()) {
                    frameTime = System.currentTimeMillis() - lastFrameTime;

                    if (frameTime > 0) {
                        gameManager.getStatsLabel().setText(String.format("%.1f fps, %.1f ms", 1000 / frameTime, frameTime));
                        gameManager.getStatsLabel().recalculateDimension();
                        gameManager.getStatsLabel().buildComponent();
                    }

                    gameManager.getObjectLabel().setText(
                            gameManager.getGameObjects().size()
                                    + " / "
                                    + new DecimalFormat("#,###").format(gameManager.getGameObjects().size())
                                    + " Objects");
                    gameManager.getObjectLabel().recalculateDimension();
                    gameManager.getObjectLabel().buildComponent();

                    gameManager.getLocationLabel().setText(
                            gameManager.getCurrentPlayer().getWorldLocation().getX()
                                    + " X, "
                                    + gameManager.getCurrentPlayer().getWorldLocation().getY()
                                    + " Y");
                    gameManager.getLocationLabel().recalculateDimension();
                    gameManager.getLocationLabel().buildComponent();
                }

                // Lighting
                gameManager.getLightingManager().getGlowComponent().setBounds(0, 0, getWidth(), getHeight());
                gameManager.getLightingManager().getDarknessComponent().setBounds(0, 0, getWidth(), getHeight());
                repaint();

                lastFrameTime = System.currentTimeMillis();

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
