package sudokuV5;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
   
/**
 * This enum encapsulates all the sound effects of a game, so as to separate the sound playing
 * codes from the game codes.
 * 1. Define all your sound effect names and the associated wave file.
 * 2. To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play().
 * 3. You might optionally invoke the static method SoundEffect.init() to pre-load all the
 *    sound files, so that the play is not paused while loading the file for the first time.
 * 4. You can use the static variable SoundEffect.volume to mute the sound.
 */
public enum SoundEffect {
   NEW_GAME("./sudoku/Ring06.wav"),  // Assuming you put the files under `src/main/resources/sounds`
   WRONG_GUESS("./sudoku/Bling.wav"),
   CORRECT_GUESS("./sudoku/tap.wav"),
   CONGRATS("./sudoku/Congrats.wav"),
   BGMUSIC("./sudoku/BG.wav");
   
   
   // Nested class for specifying volume
   public static enum Volume {
      MUTE, LOW, MEDIUM, HIGH
   }
   
   public static Volume volume = Volume.LOW;
   
   // Each sound effect has its own clip, loaded with its own sound file.
   private Clip clip;
   
   // Constructor to construct each element of the enum with its own sound file.
   SoundEffect(String soundFileName) {
      
         try {
             URL url = this.getClass().getClassLoader().getResource(soundFileName);
             if (url == null) {
                 System.err.println("Resource not found: " + soundFileName);
                 return; // Exit constructor if resource not found
             }
             AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
             clip = AudioSystem.getClip();
             clip.open(audioInputStream);
         } catch (UnsupportedAudioFileException e) {
             System.err.println("Unsupported audio format: " + soundFileName);
         } catch (IOException e) {
             System.err.println("I/O error loading audio file: " + soundFileName);
         } catch (LineUnavailableException e) {
             System.err.println("Audio line unavailable: " + soundFileName);
         } catch (IllegalArgumentException e) {
             System.err.println("Error: " + e.getMessage());
         }
     
     
   }
   
   // Play or Re-play the sound effect from the beginning, by rewinding.
   public void play() {
      if (volume != Volume.MUTE) {
         if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
         clip.setFramePosition(0); // rewind to the beginning
         clip.start();     // Start playing
      }
   }
   
   // Optional static method to pre-load all the sound files.
   static void init() {
      values(); // calls the constructor for all the elements
   }
   public void loop() {
      if (this == CORRECT_GUESS && volume != Volume.MUTE) {
          clip.loop(Clip.LOOP_CONTINUOUSLY);
      }
  }
   public static void main(String[] args) {
      try {
          SoundEffect.init(); // Initialize and load all sounds
          SoundEffect.BGMUSIC.play(); // Test playing a sound
          Thread.sleep(5000); // Wait to allow sound to play
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
  
   
}
