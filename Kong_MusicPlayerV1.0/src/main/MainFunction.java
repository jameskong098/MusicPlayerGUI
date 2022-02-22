package main;
import java.io.FileNotFoundException;

public class MainFunction {

	public static void main(String[] args) throws FileNotFoundException {
		MusicPlayerGUI MP3 = new MusicPlayerGUI();
		MP3.createPanels();
		MP3.createFrame();
		MP3.checkForMusicFile();
	}

}
