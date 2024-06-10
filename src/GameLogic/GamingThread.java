package GameLogic;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import UI.GamePage;

// Thread class for handling game logic in a separate thread
public class GamingThread extends Thread {
    // Reference to the GamePage instance
	private GamePage game;

    // Constructor for GamingThread class
	public GamingThread(GamePage game) {
        // Initialize the GamePage reference
		this.game = game;
	}

    // Method to be executed when the thread starts
	@Override
	public void run() {
        // Continuously execute the game loop
		while (true) {
            // Get the current speed of the puck in the game
			int speed = game.getPuckSpeed();
            // If the puck is moving
			if (speed > 0) {
                // Attempt to move the puck and synchronize its position
				try {
					game.movePuck();
					game.syncPuck();
				}
                // Handle potential exceptions related to audio or I/O operations
				catch (LineUnavailableException | IOException | UnsupportedAudioFileException | InterruptedException e) {
					e.printStackTrace();
				}
			}
            // Sleep for a calculated amount of time based on the puck's speed
			try {
				sleep(20 - speed);
			}
            // Handle interruption exceptions
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
