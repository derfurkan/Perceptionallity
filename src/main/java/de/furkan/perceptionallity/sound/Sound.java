package de.furkan.perceptionallity.sound;

import de.furkan.perceptionallity.Perceptionallity;
import lombok.Getter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

@Getter
public class Sound {

    private final File soundFile;
    private final byte[] audioBytes;
    private final AudioFormat audioFormat;

    public Sound(String soundKey, String... soundPath) {
        this.soundFile =
                Perceptionallity.getGame().getResourceManager().getResourceFile(soundKey, soundPath);

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.soundFile);
            audioBytes = audioInputStream.readAllBytes();
            audioFormat = audioInputStream.getFormat();
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
}
