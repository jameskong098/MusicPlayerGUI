package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MusicPlayerGUI implements ActionListener, LineListener {
	private JFrame frame;
	private JPanel panel; 
	private JPanel panel2;
	private JLabel songFileTypeNote;
	private JButton loadButton;
	private JButton loopButton;
	private JButton deleteSong;
	private JButton queueSong;
	private JButton playButton;
	private JButton pauseButton;
	private JButton skipButton;
	private JButton autoPlay;
	private JButton previousButton;
	private JButton fastForward;
	private JButton rewind;
	private JLabel currentSong;
	private JLabel nextSong;
	private JLabel songListLabel;
	private JScrollPane scroll;
	private JTextArea songList;
	private DoubleLinkedList L;
	private Queue Q;
	private Clip clip;
	private PrintStream output;
	private File file;
	private File musicFile;
	private Scanner input;
	private Node Qcur;
	private Node cur;
	private boolean temp;
	private boolean loopStatus;
	private boolean autoPlayStatus;
	
	
	public MusicPlayerGUI() throws FileNotFoundException {
		frame = new JFrame();
		panel = new JPanel(); 
		panel2 = new JPanel();
		songFileTypeNote = new JLabel("Note: Only AIFF, AU, and WAV file formats are currently supported!");
		loadButton = new JButton("Load Music File");
		loadButton.addActionListener(this);
		loopButton = new JButton("Loop [Off]");
		loopButton.addActionListener(this);
		loopStatus = false;
		deleteSong = new JButton("Delete Song");
		deleteSong.addActionListener(this);
		queueSong = new JButton("Queue Song");
		queueSong.addActionListener(this);
		playButton = new JButton("Play");
		playButton.addActionListener(this);
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(this);
		skipButton = new JButton("Next Song");
		skipButton.addActionListener(this);
		previousButton = new JButton("Previous Song");
		previousButton.addActionListener(this);
		autoPlay = new JButton("Autoplay [On]");
		autoPlay.addActionListener(this);
		autoPlayStatus = true;
		fastForward = new JButton("Fast Forward");
		fastForward.addActionListener(this);
		rewind = new JButton("Rewind");
		rewind.addActionListener(this);
		currentSong = new JLabel("Current Song Playing: none");
		nextSong = new JLabel();
		songListLabel = new JLabel("Song List: ");
		songList = new JTextArea(15,1);
		songList.setEditable(false);
		Color color = new Color(0, 0, 0);
		songList.setBorder(BorderFactory.createBevelBorder(0, color, color));
		scroll = new JScrollPane(songList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBorder(BorderFactory.createEmptyBorder(8, 150, 20, 150));
		L = new DoubleLinkedList();
		Q = new Queue();
		cur = new Node();
		Qcur = null;
		musicFile = new File("musicFile.txt");
		temp = true;
	}
	
	public void createPanels() {
		panel.setBorder(BorderFactory.createEmptyBorder(20, 120, 0, 120));
		panel.setLayout(new GridLayout(0,1));
		panel.add(songFileTypeNote);
		panel.add(loadButton);
		panel.add(deleteSong);
		panel.add(queueSong);
		panel.add(loopButton);
		panel.add(autoPlay);
		panel.add(currentSong);
		panel.add(nextSong);
		panel.add(songListLabel);
		panel2.add(rewind);
		panel2.add(fastForward);
		panel2.add(playButton);
		panel2.add(pauseButton);
		panel2.add(previousButton);
		panel2.add(skipButton);
	}
	
	public void createFrame() {
		frame.add(panel, BorderLayout.NORTH);
		frame.add(scroll, BorderLayout.CENTER);
		frame.add(panel2, BorderLayout.SOUTH);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Music Player v.1.0 by James D. Kong - jameskong098@gmail.com");
		frame.pack();
		frame.setVisible(true);
	}
	
	public boolean checkForMusicFile() throws FileNotFoundException {
		if (musicFile.exists() == true) {
			input = new Scanner(musicFile);
			while (input.hasNextLine()) {
				String temp = input.nextLine();
				file = new File(temp);
				L.insert(file.getAbsolutePath(), trimSongName(file.getName()));
			}
			cur = L.head;
			setSongList();
			nextSong.setText("Next Song: " + cur.data2);
			clip = runMusic(cur);
			return true;
		}
		return false;
	}
	
	public void checkNextSong() {
		if (cur.next != null && Q.getSize() <= 1) {
			nextSong.setText("Next Song: " + cur.next.data2);
		}
		else if (Q.getSize() > 1) {
			nextSong.setText("Next Song: " + Q.L.head.next.data2);
		}
		else {
			nextSong.setText("Next Song: none");
		}
	}
	
	public void searchForDeleteFile(String songPath) throws FileNotFoundException {
		if (musicFile.exists() == true) {
			input = new Scanner(musicFile);
			String temp2 = "";
			int counter = 0;
			while (input.hasNextLine()) {
				String temp = input.nextLine();
				if (!temp.equals(songPath)) {
					temp2 += temp + "\n";
				}
				counter += 1;
			}
			input.close();
			if (counter > 1) {
				temp2 = temp2.substring(0,temp2.length() - 1);
				output = new PrintStream(new FileOutputStream(musicFile));
				musicFile = new File("musicFile.txt");
				output.println(temp2);
				output.close();
			}
			else {
				musicFile.delete();
			}
		}
	}
	
	public void setSongList() {
		Node songListNode = L.head;
		String temp = "";
		int counter = 1;
		while (songListNode != null) {
			if (songListNode.next != null) {
				temp += counter + ". " + songListNode.data2 + "\n";
			}
			else {
				temp += counter + ". " + songListNode.data2;
			}
			counter += 1;
			songListNode = songListNode.next;
		}
		songList.setText(temp);
	}
	
	public Clip runMusic(Node cur) {
		try {
			if (cur != null) {
				file = new File(cur.data);
			}
			if (file.exists() == false) {
				JOptionPane.showMessageDialog(null, "Error: Music file was moved");
			}
			else {
				AudioInputStream music = AudioSystem.getAudioInputStream(file);
				clip = AudioSystem.getClip();
				clip.open((music));
				return clip;
			}
		}
		catch(Exception c) {
			JOptionPane.showMessageDialog(null, "No music file has been loaded");
		}
		return null;
	}
	
	public String getFileType(File file) {
		String temp = file.getName();
		String type = "";
		int dotExt = temp.lastIndexOf('.');
		for (int i=dotExt+1; i < temp.length(); i++) {
			type += temp.charAt(i);
		}
		return type;
	}
	
	public String trimSongName(String fileName) {
		String type = "";
		int dotExt = fileName.lastIndexOf('.');
		for (int i=0; i < dotExt; i++) {
			type += fileName.charAt(i);
		}
		return type;
	}
	
	public boolean checkNumber(String num) {
		if (num == null) {
			return false;
		}
		try {
			Integer.parseInt(num);
			return true;
		}
		catch (NumberFormatException e){
			return false;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loadButton) {
			JFileChooser fileChooser = new JFileChooser();
			int response = fileChooser.showOpenDialog(null);
			if (response == JFileChooser.APPROVE_OPTION) {
				file = new File(fileChooser.getSelectedFile().getAbsolutePath()); 
				if (getFileType(file).equals("aiff") == false && getFileType(file).equals("au") == false && getFileType(file).equals("wav") == false) {
					JOptionPane.showMessageDialog(null, "Unsupported file type: Please upload an AIFF, AU, or WAV file!");
				}
				else {
					try {
						output = new PrintStream(new FileOutputStream(musicFile, true));
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					if (L.head == null) {
						L.insert(file.getAbsolutePath(), trimSongName(file.getName()));
						output.println(file.getAbsolutePath());
						cur = L.head;
						setSongList();
						checkNextSong();
						clip = runMusic(cur);
					}
					else {
						L.insert(file.getAbsolutePath(), trimSongName(file.getName()));
						setSongList();
						checkNextSong();
						output.println(file.getAbsolutePath());
					}
					output.close();
				}
			}
		}
		else if (e.getSource() == deleteSong) {
			String songNum = JOptionPane.showInputDialog("Please type in the number of the song to be deleted:");
			boolean temp = checkNumber(songNum);
			boolean checkRemoved = false;
			if (songNum != null) {
				while (temp == false) {
					JOptionPane.showMessageDialog(null, "Invalid input, please only type in numbers!");
					songNum = JOptionPane.showInputDialog("Please type in the number of the song to be deleted:");
					temp = checkNumber(songNum);
					if (songNum == null) {
						break;
					}
				}
				if (songNum != null) {
					int convertNum = Integer.parseInt(songNum);
					Node songNumNode = L.head;
					int counter = 1;
					while (songNumNode != null) {
						if (counter == convertNum) {
							try {
								if (cur == songNumNode) {
									if (clip != null) {
										clip.stop();
									}
									if (cur.next != null) {
										cur = cur.next;				
										clip = runMusic(cur);
									}
									else if (cur.prev != null) {
										cur = cur.prev;	
										clip = runMusic(cur);
									}
									else {
										cur = null;
									}
									if (clip != null && cur != null) {
										String temp2 = "Current Song Playing:   " + cur.data2;
										currentSong.setText(temp2);
										clip.start();
										clip.addLineListener(this);
									}
									else {
										String temp2 = "Current Song Playing: none" ;
										currentSong.setText(temp2);
									}
								}
								searchForDeleteFile(songNumNode.data);
								checkRemoved = L.remove(songNumNode);
								setSongList();
								checkNextSong();
								break;
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						counter += 1;
						songNumNode = songNumNode.next;
					}
					if (checkRemoved == false) {
						JOptionPane.showMessageDialog(null, "No such song number was found for " + convertNum);
					}
				}
			}
		}
		else if (e.getSource() == queueSong) {
			JOptionPane.showMessageDialog(null, "Queue is currently disabled due to bugs");
		}
		/**
		else if (e.getSource() == queueSong) {
			String songNum = JOptionPane.showInputDialog("Please type in the number of the song to be queued:");
			boolean temp = checkNumber(songNum);
			boolean found = false;
			if (songNum != null) {
				while (temp == false) {
					JOptionPane.showMessageDialog(null, "Invalid input, please only type in numbers!");
					songNum = JOptionPane.showInputDialog("Please type in the number of the song to be queued:");
					temp = checkNumber(songNum);
					if (songNum == null) {
						break;
					}
				}
				if (songNum != null) {
					int convertNum = Integer.parseInt(songNum);
					Node songNumNode = L.head;
					int counter = 1;
					while (songNumNode != null) {
						if (counter == convertNum) {
							Q.enqueue(songNumNode);
							found = true;
							break;
						}
						counter += 1;
						songNumNode = songNumNode.next;
					}
					if (found == false) {
						JOptionPane.showMessageDialog(null, "No such song number was found for " + convertNum);
					}
					else {
						checkNextSong();
					}
				}
			}
		}
		*/
		else if (e.getSource() == loopButton) {
			if (loopStatus == false) {
				loopButton.setText("Loop [On]");
				loopStatus = true;
			}
			else {
				loopButton.setText("Loop [Off]");
				loopStatus = false;
			}
		}
		else if (e.getSource() == playButton) {
			if (cur != null) {
				if (cur.data != null) {
					file = new File(cur.data);
					temp = file.exists();
				}
				if (clip != null) {
					if (clip.isActive() == true) {
						JOptionPane.showMessageDialog(null, "Song is already playing");
					}
					if (Q.getSize() > 0) {
						String temp = "Current Song Playing:   " + Qcur.data2;
						currentSong.setText(temp);
						clip.start();
						clip.addLineListener(this);
					}
					else {	
						String curSong = "Current Song Playing:   " + cur.data2;
						currentSong.setText(curSong);
						checkNextSong();
						clip.start();
						clip.addLineListener(this);
					}
				}
				else if (temp == false) {
					JOptionPane.showMessageDialog(null, "Error: Music file was moved");
				}
				else {
					JOptionPane.showMessageDialog(null, "No music file has been loaded");
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "No music file has been loaded");
			}
		}
		else if (e.getSource() == pauseButton) {
			if (cur != null) {
				if (cur.data != null) {
					file = new File(cur.data);
					temp = file.exists();
				}
				if (clip != null) {
					if (clip.isActive() == false) {
						JOptionPane.showMessageDialog(null, "Song is already paused");
					}
					if (Q.getSize() > 0) {
						String temp = "Current Song Playing:   " + Qcur.data2;
						currentSong.setText(temp);
						clip.start();
						clip.addLineListener(this);
					}
					else {
						String curSong = "Current Song Playing:   " + cur.data2 + " [Paused]";
						currentSong.setText(curSong);
						clip.stop();
					}
				}
				else if (temp == false) {
					JOptionPane.showMessageDialog(null, "Error: Music file was moved");
				}
				else {
					JOptionPane.showMessageDialog(null, "No music file has been loaded");
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "No music file has been loaded");
			}
		}
		else if (e.getSource() == skipButton) {
			if (cur != null) {
				if (cur.data != null) {
					file = new File(cur.data);
					temp = file.exists();
				}
				if (Q.getSize() > 0) {
					try {
						Qcur = Q.dequeue();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					clip.stop();
					clip = runMusic(Qcur);
					checkNextSong();
					String temp = "Current Song Playing:   " + Qcur.data2;
					currentSong.setText(temp);
					clip.start();
					clip.addLineListener(this);
				}
				else if (cur.next != null) {
					if (clip != null) {
						clip.stop();
					}
					cur = cur.next;
					clip = runMusic(cur);
					checkNextSong();
					if (clip != null) {
						String curSong = "Current Song Playing:   " + cur.data2;
						currentSong.setText(curSong);
						clip.start();
						clip.addLineListener(this);
					}
				}	
				else if (temp == false) {
					JOptionPane.showMessageDialog(null, "Error: Music file was moved");
				}
				else if (L.size > 0) {
					JOptionPane.showMessageDialog(null, "No next song");
				}
				else {
					JOptionPane.showMessageDialog(null, "No music file has been loaded");
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "No music file has been loaded");
			}
		}
		else if (e.getSource() == previousButton) {
			if (cur != null) {
				if (cur.data != null) {
					file = new File(cur.data);
					temp = file.exists();
				}
				if (Q.getSize() > 0) {
					try {
						Qcur = Q.dequeue();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					clip.stop();
					clip = runMusic(Qcur);
					checkNextSong();
					String temp = "Current Song Playing:   " + Qcur.data2;
					currentSong.setText(temp);
					clip.start();
					clip.addLineListener(this);
				}
				else if (cur.prev != null) {
					if (clip != null) {
						clip.stop();
					}
					cur = cur.prev;
					clip = runMusic(cur);
					checkNextSong();
					if (clip != null) {
						String curSong = "Current Song Playing:   " + cur.data2;
						currentSong.setText(curSong);
						clip.start();
						clip.addLineListener(this);
					}
				}
				else if (temp == false) {
					JOptionPane.showMessageDialog(null, "Error: Music file was moved");
				}
				else if (L.size > 0) {
					JOptionPane.showMessageDialog(null, "No previous song");
				}
				else {
					JOptionPane.showMessageDialog(null, "No music file has been loaded");
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "No music file has been loaded");
			}
		}
		else if (e.getSource() == fastForward) {
			if (cur != null) {
				if (cur.data != null) {
					file = new File(cur.data);
					temp = file.exists();
				}
				if (clip != null) {
					if (clip.isActive() == false) {
						JOptionPane.showMessageDialog(null, "No song is currently playing!");
					}
					else if (clip.getFramePosition() + 300000 < clip.getFrameLength()) {
						clip.setFramePosition(clip.getFramePosition() + 300000);
					}
					else if (loopStatus == true) {
						if (0 + 300000 < clip.getFrameLength()) {
							clip.setFramePosition(300000);
						}
						else {
							clip.setFramePosition(0);
						}
						clip.start();
						clip.addLineListener(this);
					}
					else {
						clip.stop();
						if (Q.getSize() > 0) {
							try {
								Qcur = Q.dequeue();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							clip.stop();
							clip = runMusic(Qcur);
							checkNextSong();
							String temp = "Current Song Playing:   " + Qcur.data2;
							currentSong.setText(temp);
							clip.start();
							clip.addLineListener(this);
						}
						else if (cur.next != null) {
							cur = cur.next;
							clip = runMusic(cur);
							checkNextSong();
							String curSong = "Current Song Playing:   " + cur.data2;
							currentSong.setText(curSong);
							clip.start();
							clip.addLineListener(this);
						}
						else {
							JOptionPane.showMessageDialog(null, "No next song");
						}
					}
				}
				else if (temp == false) {
					JOptionPane.showMessageDialog(null, "Error: Music file was moved");
				}
				else {
					JOptionPane.showMessageDialog(null, "No music file has been loaded");
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "No music file has been loaded");
			}
		}
		else if (e.getSource() == rewind) {
			if (cur != null) {
				if (cur.data != null) {
					file = new File(cur.data);
					temp = file.exists();
				}
				if (clip != null) {
					if (clip.isActive() == false) {
						JOptionPane.showMessageDialog(null, "No song is currently playing!");
					}
					else if (clip.getFramePosition() - 300000 > 0) {
						clip.setFramePosition(clip.getFramePosition() - 300000);
					}
					else if (loopStatus == true) {
						if (clip.getFrameLength() - 300000 > 0) {
							clip.setFramePosition(clip.getFrameLength() - 300000);
						}
						else {
							clip.setFramePosition(0);
						}
						clip.start();
						clip.addLineListener(this);
					}
					else {
						clip.stop();
						if (Q.getSize() > 0) {
							try {
								Qcur = Q.dequeue();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							clip.stop();
							clip = runMusic(Qcur);
							checkNextSong();
							String temp = "Current Song Playing:   " + Qcur.data2;
							currentSong.setText(temp);
							clip.start();
							clip.addLineListener(this);
						}
						else if (cur.prev != null) {
							cur = cur.prev;
							clip = runMusic(cur);
							checkNextSong();
							String curSong = "Current Song Playing:   " + cur.data2;
							currentSong.setText(curSong);
							clip.start();
							clip.addLineListener(this);
						}
						else {
							JOptionPane.showMessageDialog(null, "No previous song");
						}
					}
				}
				else if (temp == false) { 
					JOptionPane.showMessageDialog(null, "Error: Music file was moved");
				}
				else {
					JOptionPane.showMessageDialog(null, "No music file has been loaded");
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "No music file has been loaded");
			}
		}
		else if (e.getSource() == autoPlay) {
			if (autoPlayStatus == true) {
				autoPlayStatus = false;
				autoPlay.setText("AutoPlay [Off]");
			}
			else {
				autoPlayStatus = true;
				autoPlay.setText("AutoPlay [On]");
			}
		}
	}

	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
		if (type == LineEvent.Type.STOP && clip.getFramePosition() == clip.getFrameLength() && autoPlayStatus == true && loopStatus == false && Q.getSize() == 0) {
			if (cur.next != null) {
				cur = cur.next;
				clip = runMusic(cur);
				checkNextSong();
				String temp = "Current Song Playing:   " + cur.data2;
				currentSong.setText(temp);
				clip.start();
				clip.addLineListener(this);
			}
			else {
				clip.setFramePosition(0);
				checkNextSong();
				String temp = "Current Song Playing:   " + cur.data2 + " [Paused]";
				currentSong.setText(temp);
			}
		}
		else if (type == LineEvent.Type.STOP && clip.getFramePosition() == clip.getFrameLength() && autoPlayStatus == false && loopStatus == false && Q.getSize() == 0){
			clip.setFramePosition(0);
			clip.addLineListener(this);
			checkNextSong();
			String temp = "Current Song Playing:   " + cur.data2 + " [Paused]";
			currentSong.setText(temp);
		}
		else if (type == LineEvent.Type.STOP && clip.getFramePosition() == clip.getFrameLength() && loopStatus == true) {
			clip.setFramePosition(0);
			clip.start();
		}
		else if (type == LineEvent.Type.STOP && clip.getFramePosition() == clip.getFrameLength() && Q.getSize() > 0) {
			try {
				Qcur = Q.dequeue();
			} catch (Exception e) {
				e.printStackTrace();
			}
			clip = runMusic(Qcur);
			checkNextSong();
			String temp = "Current Song Playing:   " + Qcur.data2;
			currentSong.setText(temp);
			clip.start();
			clip.addLineListener(this);
			
		}
	}

}
