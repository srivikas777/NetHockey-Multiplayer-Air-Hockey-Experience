package Server;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import UI.LoadingPage;

// Thread for running the server
public class ServerThread extends Thread {
    // Reference to the loading page frame
	private LoadingPage frame;

    // Constructor for ServerThread class
	public ServerThread(LoadingPage frame) {
        // Initialize the loading page frame reference
		this.frame = frame;
	}

    // Method to be executed when the thread starts
	@Override
	public void run() {
		try {
            // Create a new server table
			new ServerTable(false);
		}
        // Catch IOException, LineUnavailableException, and UnsupportedAudioFileException
		catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            // Print stack trace if an exception occurs
			e.printStackTrace();
		}
        // Dispose the loading page frame
		frame.dispose();
	}
}
