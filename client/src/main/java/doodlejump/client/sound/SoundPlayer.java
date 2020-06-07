package doodlejump.client.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SoundPlayer {
    public static final String FILENAME_PREFIX = "sounds/";

    private static final List<Clip> clipList = new CopyOnWriteArrayList<>();

    public static synchronized void play(final String fileName) {
        // Note: use .wav files
        new Thread(() -> {
            try {
                Clip clip = getClip(fileName);
                setVolume(clip, .8f);
                clip.start();
            } catch (Exception e) {
                System.out.println("play sound error: " + e.getMessage() + " for " + FILENAME_PREFIX + fileName);
            }
        }).start();
    }

    public static synchronized void loop(final String fileName) {
        // Note: use .wav files
        new Thread(() -> {
            try {
                Clip clip = getClip(fileName);
                setVolume(clip, .75f);
                clip.loop(Clip.LOOP_CONTINUOUSLY);

                clipList.add(clip);
            } catch (Exception e) {
                System.out.println("play sound error: " + e.getMessage() + " for " + FILENAME_PREFIX + fileName);
            }
        }).start();
    }

    private static Clip getClip(String fileName) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        Clip clip = AudioSystem.getClip();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getClassLoader().getResourceAsStream(FILENAME_PREFIX + fileName));
        clip.open(inputStream);
        return clip;
    }

    private static void setVolume(Clip clip, float volume) {
        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = control.getMaximum() - control.getMinimum();
        float gain = (range * volume) + control.getMinimum();
        control.setValue(gain);
    }

    public static synchronized void stop() {
        for (Clip clip : clipList) {
            clip.stop();
        }

        clipList.clear();
    }
}
