package de.furkan.perceptionallity.game.entity.player;

import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.game.GameKeyEvent;
import de.furkan.perceptionallity.game.GameKeyListener;
import de.furkan.perceptionallity.game.WorldLocation;
import de.furkan.perceptionallity.game.entity.GameEntity;

import java.awt.*;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.*;

public class GamePlayer extends GameEntity {

    private DIRECTION lastDirection;


    public GamePlayer(WorldLocation worldLocation, boolean passToCollisionCheck) {
        super(new Dimension(100, 110), worldLocation, 100, 70, 4, passToCollisionCheck);
        playAnimation(getResourceManager().getResource("player_idle_down_animation", Animation.class));
    }

    /**
     * Registers a GameKeyListener with the GameManager class. This listener listens for key events
     * and performs specific actions based on the key pressed.
     */
    GameKeyEvent gameKeyEvent;
    public void registerKeyEvent() {

        getGameManager()
                .registerKeyEvent( gameKeyEvent = new GameKeyEvent(new GameKeyListener() {

                    @Override
                    public void whileKeyPressed(int integer) {
                        if(getCurrentVelocity().isZero()) {
                            ANIMATION_KEYS newKey = null;

                            switch (integer) {
                                case VK_W -> {
                                    getCurrentVelocity().set(0, -getMoveSpeed());
                                    newKey = ANIMATION_KEYS.WALK_UP;
                                    lastDirection = DIRECTION.NORTH;
                                }
                                case VK_A -> {
                                    getCurrentVelocity().set(-getMoveSpeed(), 0);
                                    newKey = ANIMATION_KEYS.WALK_LEFT;
                                    lastDirection = DIRECTION.WEST;
                                }
                                case VK_S -> {
                                    getCurrentVelocity().set(0, getMoveSpeed());
                                    newKey = ANIMATION_KEYS.WALK_DOWN;
                                    lastDirection = DIRECTION.SOUTH;
                                }
                                case VK_D -> {
                                    getCurrentVelocity().set(getMoveSpeed(), 0);
                                    newKey = ANIMATION_KEYS.WALK_RIGHT;
                                    lastDirection = DIRECTION.OST;
                                }

                            }
                            if (newKey == null) {
                                throw new RuntimeException(
                                        "This exception should never ever come up. If it did came up there is something horrible going on.");
                            }

                            playAnimation(
                                    getResourceManager().getResource(newKey.animationKey, Animation.class), 8);
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent keyEvent) {
                        // TODO: Fix that only valid pressed keys will trigger keyReleased

                        getCurrentVelocity().set(0, 0);

                        ANIMATION_KEYS idleAnimation = null;

                        switch (lastDirection) {
                            case NORTH -> idleAnimation = ANIMATION_KEYS.IDLE_UP;
                            case OST -> idleAnimation = ANIMATION_KEYS.IDLE_RIGHT;
                            case SOUTH -> idleAnimation = ANIMATION_KEYS.IDLE_DOWN;
                            case WEST -> idleAnimation = ANIMATION_KEYS.IDLE_LEFT;
                        }

                        playAnimation(
                                getResourceManager().getResource(idleAnimation.animationKey, Animation.class));

                    }
                }, VK_W, VK_A, VK_S, VK_D));
    }

    enum DIRECTION {
        NORTH,
        OST,
        SOUTH,
        WEST
    }

    enum ANIMATION_KEYS {
        WALK_UP("player_walk_up_animation"),
        WALK_DOWN("player_walk_down_animation"),
        WALK_LEFT("player_walk_left_animation"),
        WALK_RIGHT("player_walk_right_animation"),
        IDLE_UP("player_idle_up_animation"),
        IDLE_DOWN("player_idle_down_animation"),
        IDLE_LEFT("player_idle_left_animation"),
        IDLE_RIGHT("player_idle_right_animation");

        final String animationKey;

        ANIMATION_KEYS(String animationKey) {
            this.animationKey = animationKey;
        }
    }
}
