package de.furkan.perceptionallity.sound;

import java.util.*;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import lombok.Getter;

@Getter
public class SoundEngine {

  private final HashMap<Sound, SoundThread> soundThreads = new HashMap<>();

  public void playAudio(Sound sound, float volume, boolean loop) {
    SoundThread soundThread = new SoundThread(loop, volume, sound.getAudioFormat());
    soundThread.writeToLine(sound.getAudioBytes());
    soundThreads.put(sound, soundThread);
  }

  public void stopAllAudio() {
    soundThreads.forEach(
        (sound, soundThread) -> {
          soundThread.getSourceDataLine().flush();
          soundThread.getSourceDataLine().close();
        });
  }

  public void setVolumeOfAll(float newVolume) {
    soundThreads.forEach((sound, soundThread) -> setVolumeOf(sound, newVolume));
  }

  public void setVolumeOf(Sound sound, float newVolume) {
    if (!soundThreads.containsKey(sound)) {
      throw new RuntimeException("This sound is not being played");
    }
    SourceDataLine sourceDataLine = soundThreads.get(sound).getSourceDataLine();

    FloatControl gainControl =
        (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(20f * (float) Math.log10(newVolume));
  }

  public boolean isAudioAlreadyPlaying(Sound sound) {
    return soundThreads.containsKey(sound);
  }
}
