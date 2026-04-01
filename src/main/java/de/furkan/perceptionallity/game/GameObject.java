package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Game;
import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.game.entity.npc.GameNPC;
import de.furkan.perceptionallity.game.lighting.LightSource;
import de.furkan.perceptionallity.resources.ResourceManager;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Getter
public abstract class GameObject {

    private final WorldLocation worldLocation;
    private final Dimension dimension;
    private final boolean passToCollisionCheck;
    private final GameVelocity currentVelocity;
    private final JLabel component = new JLabel();

    @Setter
    private Dimension collisionBoundaries;
    @Setter
    private GameAction onCollision;
    @Setter
    private boolean depthSortable = false;
    private LightSource lightSource;
    private Animation currentPlayingAnimation;
    private Animation lastPlayedAnimation;
    private int objectLayer;

    public GameObject(
            Dimension dimension, WorldLocation worldLocation, boolean passToCollisionCheck) {
        this.worldLocation = worldLocation;
        this.passToCollisionCheck = passToCollisionCheck;
        this.currentVelocity = new GameVelocity(0, 0);
        this.dimension = dimension;
    }

    public void playAnimation(Animation animation) {
        try {
            if (currentPlayingAnimation != null) lastPlayedAnimation = currentPlayingAnimation.clone();
            currentPlayingAnimation = animation.clone();
        } catch (Exception e) {
            Perceptionallity.handleFatalException(e);
        }
        currentPlayingAnimation.resizeTo(getDimension());
    }

    public void playAnimation(Animation animation, int fps) {
        playAnimation(animation);
        currentPlayingAnimation.setFramesPerSecond(fps);
    }

    public Rectangle buildRectangle() {
        return new Rectangle(
                worldLocation.getX(), worldLocation.getY(), dimension.width, dimension.height);
    }

    public void setLightSource(LightSource lightSource) {
        this.lightSource = lightSource;
        if (lightSource != null) { // Why Claude why
            lightSource.setParentObject(this);
        }
    }

    public int getDepthSortY() {
        return getWorldLocation().getY() + (int) getDimension().getHeight();
    }

    public void initializeGameObject(int layer, boolean depthSortable) {
        this.depthSortable = depthSortable;
        initializeGameObject(layer);
    }

    public void initializeGameObject(int layer) {
        getGameManager().registerGameObject(this);
        if (this instanceof GameNPC)
            getGameManager().getGameNPCs().add((GameNPC) this);
        if (lightSource != null)
            getGameManager().getLightingManager().addLight(lightSource);
        component.setBounds(
                getWorldLocation().getX(),
                getWorldLocation().getY(),
                (int) getDimension().getWidth(),
                (int) getDimension().getHeight());
        objectLayer = layer;
        getGame().getGameRenderer().add(component, Integer.valueOf(objectLayer));
    }

    public void unInitializeGameObject() {
        if (lightSource != null)
            getGameManager().getLightingManager().removeLight(lightSource);
        getGameManager().unregisterGameObject(this);
        if (this instanceof GameNPC)
            getGameManager().getGameNPCs().remove((GameNPC) this);
        Perceptionallity.getGame().getGameRenderer().remove(getComponent());
    }

    public int distanceTo(WorldLocation worldLocation) {
        return (int)
                Math.hypot(
                        worldLocation.getX() - getWorldLocation().toCenterLocation(getDimension()).getX(),
                        worldLocation.getY() - getWorldLocation().toCenterLocation(getDimension()).getY());
    }

    public ResourceManager getResourceManager() {
        return getGame().getResourceManager();
    }

    public Game getGame() {
        return Perceptionallity.getGame();
    }

    public GameManager getGameManager() {
        return getGame().getGameManager();
    }
}
