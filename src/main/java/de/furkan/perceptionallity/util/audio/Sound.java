package de.furkan.perceptionallity.util.audio;

import de.furkan.perceptionallity.Perceptionallity;
import lombok.Getter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Getter
public class Sound {

    private final File soundFile;

    public Sound(String soundKey, String... soundPath) {
        this.soundFile = Perceptionallity.getGame().getResourceManager().getResourceFile(soundKey, soundPath);
    }

    private AudioInputStream skipSilence(AudioInputStream inputStream) throws IOException, UnsupportedAudioFileException {
        byte[] buffer = new byte[4096]; // Adjust buffer size as needed
        boolean foundNonSilenceStart = false;
        boolean foundNonSilenceEnd = false;

        // Read the input stream until non-silent audio data is found at the beginning
        while (!foundNonSilenceStart && inputStream.read(buffer) != -1) {
            // Check if the current buffer contains non-silent audio data
            for (byte b : buffer) {
                if (b != 0) { // Assuming 0 represents silence; adjust condition as needed
                    foundNonSilenceStart = true;
                    break;
                }
            }
        }

        // Read the input stream from the end to find non-silent audio data at the end
        inputStream.mark(Integer.MAX_VALUE); // Mark the current position to be able to reset
        int bytesSkippedFromEnd = 0;
        int availableBytes;
        while ((availableBytes = inputStream.available()) > 0) {
            inputStream.skip(availableBytes);
            bytesSkippedFromEnd += availableBytes;
            if (inputStream.read(buffer) != -1) {
                // Check if the current buffer contains non-silent audio data
                for (int i = buffer.length - 1; i >= 0; i--) {
                    if (buffer[i] != 0) { // Assuming 0 represents silence; adjust condition as needed
                        foundNonSilenceEnd = true;
                        break;
                    }
                }
            }
        }

        // Reset the stream to the marked position
        inputStream.reset();
        int bytesSkippedFromStart = inputStream.available();

        // If non-silent audio data is found at both ends, create a new AudioInputStream with trimmed data
        if (foundNonSilenceStart || foundNonSilenceEnd) {
            int newLength = bytesSkippedFromStart + bytesSkippedFromEnd;
            byte[] trimmedData = new byte[newLength];
            inputStream.read(trimmedData, 0, bytesSkippedFromStart);
            inputStream.skip(newLength - bytesSkippedFromStart); // Skip remaining bytes
            return new AudioInputStream(new ByteArrayInputStream(trimmedData), inputStream.getFormat(), newLength);
        } else {
            // If no non-silent audio data is found, return the original input stream
            return inputStream;
        }
    }


}
