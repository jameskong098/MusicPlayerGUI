/**
 * James Kong
 * jameskong098@gmail.com
 * December 21, 2021
 * Music Player GUI Project
 * MainFunction Class: Main function that runs the Music Player GUI methods to construct a music player.
 * Known Bugs: FileNotFoundException
 */

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
