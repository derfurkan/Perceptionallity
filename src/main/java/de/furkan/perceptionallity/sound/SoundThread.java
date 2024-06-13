package de.furkan.perceptionallity.sound;

import de.furkan.perceptionallity.Perceptionallity;
import javax.sound.sampled.*;
import lombok.Getter;

@Getter
public class SoundThread extends Thread {

  private final boolean loop;
  private final float volume;
  private final AudioFormat audioFormat;
  private SourceDataLine sourceDataLine;
  private byte[] currentAudioData;

  protected SoundThread(boolean loop, float volume, AudioFormat audioFormat) {
    this.loop = loop;
    this.volume = volume;
    this.audioFormat = audioFormat;
    start();
  }

  @Override
  public void run() {

    try {
      sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);

      sourceDataLine.open(audioFormat);
      setVolume(volume);

      sourceDataLine.start();
      sourceDataLine.addLineListener(
          event -> {
            if (event.getType() == LineEvent.Type.STOP && loop) {
              new SoundThread(true, volume, audioFormat);
            }
          });

      while (true) {
        if (currentAudioData == null) return;
        sourceDataLine.flush();
        sourceDataLine.write(currentAudioData, 0, currentAudioData.length);
        currentAudioData = null;
      }
    } catch (Exception e) {
      Perceptionallity.getGame().handleFatalException(new RuntimeException(e));
    }
  }

  public void writeToLine(byte[] audioData) {
    currentAudioData = audioData;
  }

  public void setVolume(float volume) {
    FloatControl gainControl =
        (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
    float gain = (float) (Math.log10(volume) * 20);
    gainControl.setValue(gain);
  }
}
