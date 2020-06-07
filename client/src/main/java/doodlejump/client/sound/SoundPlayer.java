package doodlejump.client.sound;

import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {
    public static final String FILENAME_PREFIX = "sounds/";

    public static synchronized void play(final String fileName)
    {
        // Note: use .wav files
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream(FILENAME_PREFIX + fileName));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + FILENAME_PREFIX + fileName);
                }
            }
        }).start();
    }

    public static synchronized void loop(final String fileName)
    {
        // Note: use .wav files
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream(FILENAME_PREFIX + fileName));
                    clip.open(inputStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + FILENAME_PREFIX + fileName);
                }
            }
        }).start();
    }
}
