package de.furkan.perceptionallity.game.lighting;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

import javax.swing.*;

import de.furkan.perceptionallity.game.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameLightingManager {

    public static final int GLOW_LAYER = 0;
    public static final int DARKNESS_LAYER = 3;
    private static final float LIGHT_MAP_SCALE = 0.3f;

    private final List<LightSource> lightSources = new CopyOnWriteArrayList<>();
    @Getter
    private final JComponent darknessComponent = new DarknessComponent();
    @Getter
    private final JComponent glowComponent = new GlowComponent();
    private BufferedImage darknessMap;
    private BufferedImage glowMap;
    private int lastWidth, lastHeight;
    @Getter @Setter
    private float ambientDarkness = 0.8f;
    private Color cachedAmbientColor;
    private float cachedAmbientDarkness;

    public void addLight(LightSource lightSource) {
        lightSources.add(lightSource);
    }

    public void removeLight(LightSource lightSource) {
        lightSources.remove(lightSource);
    }

    public int getLightCount() {
        return lightSources.size();
    }

    public void updateFlicker() {
        for (LightSource light : lightSources) {
            light.updateFlicker();
        }
    }

    private Color getAmbientColor() {
        if (cachedAmbientColor == null || cachedAmbientDarkness != ambientDarkness) {
            cachedAmbientColor = new Color(0, 0, 0, (int) (ambientDarkness * 255));
            cachedAmbientDarkness = ambientDarkness;
        }
        return cachedAmbientColor;
    }

    /**
     * Builds the light map off-EDT (call from the render thread).
     * Safe because paintImmediately is called after this completes.
     */
    public void buildLightMap(Camera camera, int screenWidth, int screenHeight) {
        if (lightSources.isEmpty()) {
            darknessMap = null;
            glowMap = null;
            return;
        }

        int scaledWidth = Math.max(1, (int) (screenWidth * LIGHT_MAP_SCALE));
        int scaledHeight = Math.max(1, (int) (screenHeight * LIGHT_MAP_SCALE));

        if (darknessMap == null || lastWidth != scaledWidth || lastHeight != scaledHeight) {
            darknessMap = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
            glowMap = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
            lastWidth = scaledWidth;
            lastHeight = scaledHeight;
        }

        // Build darkness map (ambient darkness + holes)
        Graphics2D dg = darknessMap.createGraphics();
        dg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        dg.setComposite(AlphaComposite.Clear);
        dg.fillRect(0, 0, scaledWidth, scaledHeight);
        dg.setComposite(AlphaComposite.SrcOver);
        dg.setColor(getAmbientColor());
        dg.fillRect(0, 0, scaledWidth, scaledHeight);

        // Build glow map (colored tints only)
        Graphics2D gg = glowMap.createGraphics();
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gg.setComposite(AlphaComposite.Clear);
        gg.fillRect(0, 0, scaledWidth, scaledHeight);
        gg.setComposite(AlphaComposite.SrcOver);

        for (LightSource light : lightSources) {
            int[] screenPos = light.getScreenPosition(camera);
            int lx = (int) (screenPos[0] * LIGHT_MAP_SCALE);
            int ly = (int) (screenPos[1] * LIGHT_MAP_SCALE);
            int radius = (int) (light.getRadius() * LIGHT_MAP_SCALE);

            if (lx + radius < 0 || lx - radius > scaledWidth || ly + radius < 0 || ly - radius > scaledHeight) {
                continue;
            }

            float intensity = light.getIntensity();
            int safeRadius = Math.max(1, radius);

            // Punch hole in darkness
            dg.setComposite(AlphaComposite.DstOut);
            dg.setPaint(new RadialGradientPaint(
                    lx, ly, safeRadius,
                    new float[]{0.0f, 0.7f, 1.0f},
                    new Color[]{
                            new Color(1f, 1f, 1f, intensity),
                            new Color(1f, 1f, 1f, intensity * 0.3f),
                            new Color(0f, 0f, 0f, 0f)
                    }
            ));
            dg.fillOval(lx - radius, ly - radius, radius * 2, radius * 2);

            // Color tint on glow map
            Color lc = light.getColor();
            int r = lc.getRed(), gr2 = lc.getGreen(), b = lc.getBlue();
            gg.setPaint(new RadialGradientPaint(
                    lx, ly, safeRadius,
                    new float[]{0.0f, 0.3f, 0.7f, 1.0f},
                    new Color[]{
                            new Color(r, gr2, b, 140),
                            new Color(r, gr2, b, 90),
                            new Color(r, gr2, b, 30),
                            new Color(r, gr2, b, 0)
                    }
            ));
            gg.fillOval(lx - radius, ly - radius, radius * 2, radius * 2);
        }

        dg.dispose();
        gg.dispose();
    }

    private class DarknessComponent extends JComponent {
        DarknessComponent() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (darknessMap != null) {
                ((Graphics2D) g).drawImage(darknessMap, 0, 0, getWidth(), getHeight(), null);
            }
        }
    }

    private class GlowComponent extends JComponent {
        GlowComponent() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (glowMap != null) {
                ((Graphics2D) g).drawImage(glowMap, 0, 0, getWidth(), getHeight(), null);
            }
        }
    }
}
