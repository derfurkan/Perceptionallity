package de.furkan.perceptionallity;

import de.furkan.perceptionallity.resources.ResourceManager;
import de.furkan.perceptionallity.sound.SoundEngine;

import java.util.logging.Logger;

public abstract class Manager {

    public abstract void initialize();

    public ResourceManager getResourceManager() {
        return Perceptionallity.getGame().getResourceManager();
    }
    public Logger getLogger() {
        return Perceptionallity.getGame().getLogger();
    }

    public SoundEngine getSoundEngine() {
        return Perceptionallity.getGame().getSoundEngine();
    }


}
