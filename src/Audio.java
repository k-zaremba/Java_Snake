import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {

    Clip clip;
    File file;
    AudioInputStream audio;

    Audio(String fileName) {
        try {
            this.file = new File(fileName);
            this.clip = AudioSystem.getClip();
            this.audio = AudioSystem.getAudioInputStream(file);
            clip.open(audio);
        } catch (LineUnavailableException e) {
            System.out.println("LINE");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO");
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            System.out.println("UNSUPPORTED");
            e.printStackTrace();
        }
    }

    void start () {
        clip.start();
    }

    void stop(){
        clip.stop();
    }
}