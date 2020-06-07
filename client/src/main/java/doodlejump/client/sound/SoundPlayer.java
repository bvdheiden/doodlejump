package doodlejump.client.sound;

import javax.sound.sampled.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SoundPlayer {
    public static final String FILENAME_PREFIX = "sounds/";

    private static final List<Clip> clipList = new CopyOnWriteArrayList<>();

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

                    clipList.add(clip);
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + FILENAME_PREFIX + fileName);
                }
            }
        }).start();
    }

    public static synchronized void stop() {
        for (Clip clip : clipList) {
            clip.stop();
        }

        clipList.clear();
    }
}
