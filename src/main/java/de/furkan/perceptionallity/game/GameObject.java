package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Game;
import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.resources.ResourceManager;
import de.furkan.perceptionallity.util.sprite.Sprite;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Getter
public abstract class GameObject {

    private final WorldLocation worldLocation, lastLocation = new WorldLocation(0, 0);
    private final Dimension dimension;
    private final boolean passToCollisionCheck;
    @Setter
    private Dimension collisionBoundaries;
    private final GameVelocity currentVelocity;
    @Setter
    private GameAction onCollision;
    private Animation currentPlayingAnimation;
    @Setter
    private Sprite initialSprite;
    @Setter
    private boolean isAnimationFresh = true;
    @Setter
    private JLabel component;

    public GameObject(
            Dimension dimension, WorldLocation worldLocation, boolean passToCollisionCheck) {
        this.worldLocation = worldLocation;
        this.passToCollisionCheck = passToCollisionCheck;
        this.currentVelocity = new GameVelocity(0, 0);
        this.dimension = dimension;
    }

    public void playAnimation(Animation animation) {
        currentPlayingAnimation = animation.clone();
        currentPlayingAnimation.resizeTo(getDimension());
        setAnimationFresh(true);
    }

    public void playAnimation(Animation animation,int fps) {
        animation.setFramesPerSecond(fps);
        playAnimation(animation);
    }

    public Rectangle buildRectangle() {
        return new Rectangle(
                worldLocation.getX(), worldLocation.getY(), dimension.width, dimension.height);
    }


    public void initializeGameObject(int layer) {
        Perceptionallity.getGame().getGameManager().registerGameObject(this);
        component.setBounds(
                getWorldLocation().getX(),
                getWorldLocation().getY(),
                (int) getDimension().getWidth(),
                (int) getDimension().getHeight());
        Perceptionallity.getGame().getGamePanel().add(getComponent(), layer);
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
