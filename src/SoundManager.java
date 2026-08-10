import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;

public class SoundManager {
    // Dictionary to hold all our preloaded sound effects
    private HashMap<String, Clip> clips;

    public SoundManager() {
        clips = new HashMap<>();
        loadAllSounds();
    }

    // load assets
    private void loadAllSounds() {
        // Map a simple string name to the actual file path
        loadClip("beginning", "Assets/sfx/pacman_beginning.wav");
        loadClip("chomp", "Assets/sfx/pacman_chomp.wav");
        loadClip("death", "Assets/sfx/pacman_death.wav");
        loadClip("eatfruit", "Assets/sfx/pacman_eatfruit.wav");
        loadClip("eatghost", "Assets/sfx/pacman_eatghost.wav");
        loadClip("menu", "Assets/sfx/pacman_intermission.wav");
    }

    private void loadClip(String name, String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clips.put(name, clip);
        } catch (Exception e) {
            System.out.println("Failed to load sound: " + filePath);
        }
    }

    public void playSound(String name) {
        Clip clip = clips.get(name);
        if (clip != null) {
            // If the sound is already playing (e.g., eating beans fast), stop it and restart it
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
        }
    }
}