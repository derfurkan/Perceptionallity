package de.furkan.perceptionallity.sound;

import de.furkan.perceptionallity.util.audio.Sound;
import lombok.Getter;

import javax.sound.sampled.*;
import java.util.*;

@Getter
public class SoundManager {

  private final HashMap<Sound, Clip> runningClips = new HashMap<>();

  public void playAudio(Sound sound,float volume) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                runningClips.put(sound,clip);
                clip.open(AudioSystem.getAudioInputStream(sound.getSoundFile()));
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(20f * (float) Math.log10(volume));
                clip.start();
                clip.addLineListener(event -> {
                    if(event.getType() == LineEvent.Type.STOP)
                        runningClips.remove(sound);
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
  }

    public void playAudioLoop(Sound sound,float volume) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    runningClips.put(sound,clip);
                    clip.open(AudioSystem.getAudioInputStream(sound.getSoundFile()));
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(volume));
                    clip.start();
                    clip.addLineListener(event -> {
                        if(event.getType() == LineEvent.Type.STOP)
                            run();
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    public boolean isAudioAlreadyPlaying(Sound sound) {
      return runningClips.containsKey(sound);
    }

    public void setVolumeOfAll(float volume) {
      runningClips.forEach((sound,clip) -> setVolumeOf(sound,volume));
    }

    public void setVolumeOf(Sound sound,float volume) {
      if(!runningClips.containsKey(sound)) {
          throw new RuntimeException("Cannot set volume if audio clip which is not being played. Please make sure to play the audio clips before setting the volume of it.");
      }
      Clip clip = runningClips.get(sound);
      FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.shift(gainControl.getValue(),20f * (float) Math.log10(volume),gainControl.getUpdatePeriod());


    }

    public void batchUpdateVolumeOfAll(float volume) {
        new Thread(() -> {
            runningClips.forEach((sound, clip) -> {
                setVolumeOf(sound, volume);
            });
        }).start();
    }

    public void stopAllAudio() {
      runningClips.forEach((sound,clip) -> clip.stop());
    }

    public void stopAudio(Sound sound) {
      if(runningClips.containsKey(sound)) {
          runningClips.get(sound).stop();
      }
    }

}
