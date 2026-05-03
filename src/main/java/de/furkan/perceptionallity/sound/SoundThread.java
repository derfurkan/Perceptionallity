package de.furkan.perceptionallity.sound;

import de.furkan.perceptionallity.Perceptionallity;
import lombok.Getter;

import javax.sound.sampled.*;

@Getter
class SoundThread extends Thread {

    private final boolean loop;
    private final float volume;
    private final AudioFormat audioFormat;
    private SourceDataLine sourceDataLine;
    private byte[] currentAudioData,originalAudioData;
    private final Runnable onFinish;

    protected SoundThread(boolean loop, float volume, AudioFormat audioFormat,Runnable onFinish) {
        this.loop = loop;
        this.volume = volume;
        this.audioFormat = audioFormat;
        this.onFinish = onFinish;
        start();
    }

    @Override
    public void run() {

        try {
            sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);

            sourceDataLine.open(audioFormat);
            setVolume(volume);

            sourceDataLine.start();

            while (sourceDataLine.isOpen()) {
                if (currentAudioData == null)
                    return;
                sourceDataLine.flush();
                sourceDataLine.write(currentAudioData, 0, currentAudioData.length);
                if (loop) {
                    currentAudioData = originalAudioData;
                } else {
                    currentAudioData = null;
                }
            }
            Perceptionallity.getGame().getSoundEngine().getSoundThreads().remove(this);
        } catch (Exception e) {
            Perceptionallity.handleFatalException(e);
        } finally {
            onFinish.run();
        }
    }

    public void writeToLine(byte[] audioData) {
        currentAudioData = audioData;
        originalAudioData = audioData;
    }

    public void setVolume(float volume) {
        FloatControl gainControl =
                (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
        float gain = (float) (Math.log10(volume) * 20);
        gainControl.setValue(gain);
    }
}
