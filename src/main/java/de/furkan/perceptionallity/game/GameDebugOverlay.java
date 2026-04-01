package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.game.lighting.LightSource;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameDebugOverlay extends JComponent {

    public static final int DEBUG_LAYER = 5;

    private volatile double lastFrameTimeMs;

    private static final Color BOUNDS_COLOR = new Color(0, 255, 0, 120);
    private static final Color BOUNDS_FILL_COLOR = new Color(0, 255, 0, 20);
    private static final Color COLLISION_COLOR = new Color(255, 100, 0, 150);
    private static final Color COLLISION_FILL_COLOR = new Color(255, 100, 0, 25);
    private static final Color DEPTH_LINE_COLOR = new Color(0, 255, 255, 180);
    private static final Color TEXT_BG_COLOR = new Color(0, 0, 0, 160);
    private static final Color TEXT_COLOR = new Color(255, 255, 255, 220);
    private static final Color LIGHT_RADIUS_COLOR = new Color(255, 255, 0, 80);
    private static final Color MENU_BOUNDS_COLOR = new Color(0, 200, 255, 100);

    private static final Font DEBUG_FONT = new Font("Monospaced", Font.PLAIN, 16);
    private static final Stroke DASHED_STROKE = new BasicStroke(1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 10, new float[]{4, 4}, 0);
    private static final Stroke SOLID_STROKE = new BasicStroke(1);
    private static final Stroke COLLISION_STROKE = new BasicStroke(1.5f);

    public GameDebugOverlay() {
        setOpaque(false);
    }

    public void setLastFrameTimeMs(double ms) {
        this.lastFrameTimeMs = ms;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!Perceptionallity.getGame().showDebugLines) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(DEBUG_FONT);
        FontMetrics fm = g2d.getFontMetrics();
    

        GameManager gameManager = Perceptionallity.getGame().getGameManager();

        if (gameManager.isGameState(GameState.IN_GAME)) {
            drawInGameDebug(g2d, fm, gameManager);
        } else if (gameManager.isGameState(GameState.MENU)) {
            drawMenuDebug(g2d, fm);
        }
    }

    private void drawInGameDebug(Graphics2D g2d, FontMetrics fm, GameManager gameManager) {
        Camera camera = gameManager.getCamera();
        HashMap<GameObject, int[]> snapshot = new HashMap<>(camera.getCalculatedGameObjects());

        for (Map.Entry<GameObject, int[]> entry : snapshot.entrySet()) {
            GameObject obj = entry.getKey();
            int[] screenPos = entry.getValue();
            int sx = screenPos[0];
            int sy = screenPos[1];
            int w = (int) obj.getDimension().getWidth();
            int h = (int) obj.getDimension().getHeight();

            drawObjectBounds(g2d, sx, sy, w, h);
            drawCollisionBounds(g2d, obj, sx, sy, w, h);
            drawDepthSortLine(g2d, obj, sx, sy, w, h);
            drawLightRadius(g2d, obj, camera);
            drawInfoLabel(g2d, fm, obj, sx, sy,snapshot);
        }

     
        drawGlobalDebugInfo(g2d, fm, gameManager, camera);
    }

    private void drawObjectBounds(Graphics2D g2d, int sx, int sy, int w, int h) {
        g2d.setStroke(SOLID_STROKE);
        g2d.setColor(BOUNDS_FILL_COLOR);
        g2d.fillRect(sx, sy, w, h);
        g2d.setColor(BOUNDS_COLOR);
        g2d.drawRect(sx, sy, w, h);
    }

    private void drawCollisionBounds(Graphics2D g2d, GameObject obj, int sx, int sy, int w, int h) {
        if (obj.getCollisionBoundaries() == null) return;
        Dimension cb = obj.getCollisionBoundaries();
        int offsetX = (w - cb.width) / 2;
        int offsetY = (h - cb.height) / 2;
        g2d.setStroke(COLLISION_STROKE);
        g2d.setColor(COLLISION_FILL_COLOR);
        g2d.fillRect(sx + offsetX, sy + offsetY, cb.width, cb.height);
        g2d.setColor(COLLISION_COLOR);
        g2d.drawRect(sx + offsetX, sy + offsetY, cb.width, cb.height);
    }

    private void drawDepthSortLine(Graphics2D g2d, GameObject obj, int sx, int sy, int w, int h) {
        if (!obj.isDepthSortable()) return;
        int depthScreenY = sy + h;
        g2d.setStroke(DASHED_STROKE);
        g2d.setColor(DEPTH_LINE_COLOR);
        g2d.drawLine(sx - 10, depthScreenY, sx + w + 10, depthScreenY);
        g2d.setStroke(SOLID_STROKE);
    }

    private void drawLightRadius(Graphics2D g2d, GameObject obj, Camera camera) {
        if (obj.getLightSource() == null) return;
        LightSource ls = obj.getLightSource();
        int[] screenPos = ls.getScreenPosition(camera);
        int cx = screenPos[0];
        int cy = screenPos[1];
        int r = ls.getRadius();
        g2d.setStroke(DASHED_STROKE);
        g2d.setColor(LIGHT_RADIUS_COLOR);
        g2d.drawOval(cx - r, cy - r, r * 2, r * 2);
        g2d.setStroke(SOLID_STROKE);
    }

    private void drawInfoLabel(Graphics2D g2d, FontMetrics fm, GameObject obj, int sx, int sy, HashMap<GameObject,int[]> snapshot) {
        String className = obj.getClass().getSimpleName();
        int[] objCameraLocation = snapshot.entrySet().stream().filter(t -> t.getKey() == obj).findFirst().get().getValue();
        String posStr = "Wl:" + obj.getWorldLocation().getX() + " " + obj.getWorldLocation().getY() + " Cl:" + objCameraLocation[0] + " " + objCameraLocation[1];
        String layerStr = "L:" + obj.getObjectLayer();

        StringBuilder info = new StringBuilder();
        info.append(className).append(" (").append(posStr).append(") ").append(layerStr);

        if (obj.isDepthSortable()) {
            info.append(" dY:").append(obj.getDepthSortY());
        }

        if (!obj.getCurrentVelocity().isZero()) {
            info.append(" v:").append(obj.getCurrentVelocity().getX())
                    .append(",").append(obj.getCurrentVelocity().getY());
        }

        if (obj.getLightSource() != null) {
            info.append(" light:r").append(obj.getLightSource().getRadius());
        }

        String text = info.toString();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int textX = sx;
        int textY = sy - 4;

        g2d.setColor(TEXT_BG_COLOR);
        g2d.fillRect(textX - 2, textY - textHeight, textWidth + 4, textHeight + 2);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString(text, textX, textY - fm.getDescent());
    }

    private void drawGlobalDebugInfo(Graphics2D g2d, FontMetrics fm, GameManager gameManager, Camera camera) {
        String fpsStr = lastFrameTimeMs > 0
                ? String.format("%.1f fps, %.1f ms", 1000.0 / lastFrameTimeMs, lastFrameTimeMs)
                : "-- fps";

        String[] lines = {
                fpsStr,
                "Objects: " + gameManager.getGameObjects().size(),
                "Camera View Location: " + camera.getCameraViewLocation().getX() + "," + camera.getCameraViewLocation().getY(),
                "Gamestate: " + gameManager.getGameState().name(),
                "Centered Object: " + (camera.getCenteredObject() != null
                        ? camera.getCenteredObject().getClass().getSimpleName() : "none"),
                "Light Sources: " + gameManager.getLightingManager().getLightCount()
        };

        int padding = 6;
        int maxWidth = 0;
        for (String line : lines) maxWidth = Math.max(maxWidth, fm.stringWidth(line));
        int panelX = getWidth() - maxWidth - padding * 2;
        int panelY = 10;
        int panelH = lines.length * fm.getHeight() + padding;

        g2d.setColor(TEXT_BG_COLOR);
        g2d.fillRect(panelX, panelY, maxWidth + padding, panelH);
        g2d.setColor(TEXT_COLOR);
    
        for (int i = 0; i < lines.length; i++) {
            g2d.drawString(lines[i], panelX + padding,
                    panelY + padding + fm.getAscent() + i * fm.getHeight());
        }
    }

    private void drawMenuDebug(Graphics2D g2d, FontMetrics fm) {
        GameRenderer renderer = Perceptionallity.getGame().getGameRenderer();
        for (Component child : renderer.getComponents()) {
            if (child == this) continue;
            Rectangle bounds = child.getBounds();
            if (bounds.width == 0 || bounds.height == 0) continue;

            g2d.setStroke(SOLID_STROKE);
            g2d.setColor(MENU_BOUNDS_COLOR);
            g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

            String name = child.getClass().getSimpleName();
            if (name.equals("JLabel")) name = child.getName() != null ? child.getName() : "JLabel";
            int textWidth = fm.stringWidth(name);
            g2d.setColor(TEXT_BG_COLOR);
            g2d.fillRect(bounds.x, bounds.y - fm.getHeight(), textWidth + 4, fm.getHeight());
            g2d.setColor(TEXT_COLOR);
            g2d.drawString(name, bounds.x + 2, bounds.y - fm.getDescent());
        }
    }
}
