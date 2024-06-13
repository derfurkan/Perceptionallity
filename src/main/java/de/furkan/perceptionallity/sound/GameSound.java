package de.furkan.perceptionallity.sound;

import de.furkan.perceptionallity.Perceptionallity;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import lombok.Getter;

@Getter
public class GameSound {

  private final File soundFile;
  private final byte[] audioBytes;
  private final AudioFormat audioFormat;

  public GameSound(String soundKey, String... soundPath)
      throws UnsupportedAudioFileException, IOException {
    this.soundFile =
        Perceptionallity.getGame().getResourceManager().getResourceFile(soundKey, soundPath);

    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.soundFile);
    audioBytes = audioInputStream.readAllBytes();
    audioFormat = audioInputStream.getFormat();
  }
}
