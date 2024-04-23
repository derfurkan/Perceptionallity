package de.furkan.perceptionallity;

import de.furkan.perceptionallity.resources.ResourceManager;
import de.furkan.perceptionallity.sound.SoundManager;

import java.util.logging.Logger;

public abstract class Manager {

    public abstract void initialize();

    public ResourceManager getResourceManager() {
        return Perceptionallity.getGame().getResourceManager();
    }
    public Logger getLogger() {
        return Perceptionallity.getGame().getLogger();
    }

    public SoundManager getSoundManager() {
        return Perceptionallity.getGame().getSoundManager();
    }


}
