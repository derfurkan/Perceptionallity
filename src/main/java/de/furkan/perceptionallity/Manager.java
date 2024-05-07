package de.furkan.perceptionallity;

import de.furkan.perceptionallity.resources.ResourceManager;
import de.furkan.perceptionallity.sound.SoundEngine;

import java.util.logging.Logger;

public abstract class Manager {

    public abstract void initialize();

    public ResourceManager getResourceManager() {
        return getGame().getResourceManager();
    }

    public Logger getLogger() {
        return getGame().getLogger();
    }

    public SoundEngine getSoundEngine() {
        return getGame().getSoundEngine();
    }

    public Game getGame() {
        return Perceptionallity.getGame();
    }
}
