package de.furkan.perceptionallity.sound;

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
    this.volume = volume;
    this.loop = loop;
    this.audioFormat = audioFormat;
    start();
  }

  @Override
  public void run() {
    try {
      sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
      sourceDataLine.open(audioFormat);
      FloatControl gainControl =
          (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(20f * (float) Math.log10(volume));

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
    } catch (LineUnavailableException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeToLine(byte[] audioData) {

    currentAudioData = audioData;
  }
}
