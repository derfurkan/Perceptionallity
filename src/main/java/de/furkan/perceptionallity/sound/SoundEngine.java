package de.furkan.perceptionallity.sound;

import java.util.HashMap;
import lombok.Getter;

@Getter
public class SoundEngine {

  private final HashMap<Sound, SoundThread> soundThreads = new HashMap<>();

  /**
   * Plays the specified sound with the given volume and looping preference. A new SoundThread is
   * created and started for the sound, and the sound is added to the soundThreads map.
   *
   * @param sound The Sound object to be played.
   * @param volume The volume at which the sound should be played.
   * @param loop A boolean indicating whether the sound should loop continuously.
   */
  public void playAudio(Sound sound, float volume, boolean loop) {
    SoundThread soundThread = new SoundThread(loop, volume, sound.getAudioFormat());
    soundThread.writeToLine(sound.getAudioBytes());
    soundThreads.put(sound, soundThread);
  }

  /**
   * Stops all currently playing sounds and closes their associated SourceDataLines. This method
   * iterates through all entries in the soundThreads map, stopping and closing each one.
   */
  public void stopAllAudio() {
    soundThreads.forEach(
        (sound, soundThread) -> {
          soundThread.getSourceDataLine().flush();
          soundThread.getSourceDataLine().close();
        });
  }

  /**
   * Sets the volume for all currently playing sounds to the specified level. This method iterates
   * through all sound threads and applies the new volume setting to each.
   *
   * @param newVolume The new volume level to be set for all sounds.
   */
  public void setVolumeOfAll(float newVolume) {
    soundThreads.forEach((sound, soundThread) -> setVolumeOf(sound, newVolume));
  }

  /**
   * Sets the volume of a specific sound if it is currently being played. Throws a RuntimeException
   * if the sound is not found in the currently playing sound threads.
   *
   * @param sound The sound whose volume is to be adjusted.
   * @param newVolume The new volume level for the specified sound.
   * @throws RuntimeException If the specified sound is not currently being played.
   */
  public void setVolumeOf(Sound sound, float newVolume) {
    if (!soundThreads.containsKey(sound)) {
      throw new RuntimeException("This sound is not being played");
    }
    soundThreads.get(sound).setVolume(newVolume);
  }

  /**
   * Checks if the specified sound is currently being played.
   *
   * @param sound The sound to check.
   * @return true if the sound is currently being played, false otherwise.
   */
  public boolean isAudioAlreadyPlaying(Sound sound) {
    return soundThreads.containsKey(sound);
  }
}
