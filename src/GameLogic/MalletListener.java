package GameLogic;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import UI.GamePage;

// Mouse motion listener for controlling the mallet in the game
public class MalletListener extends MouseMotionAdapter {
    // Reference to the GamePage instance
	private GamePage game;

    // Constructor for MalletListener class
	public MalletListener(GamePage game) {
        // Initialize the GamePage reference
		this.game = game;
	}

    // Method to handle mouse movement events
	@Override
	public void mouseMoved(MouseEvent e) {
        // Get the current location of the mouse pointer
		Point point = game.getLocation();
		try {
            // Move the player's mallet to the current location of the mouse pointer
			game.moveMallet(point.getX(), point.getY());

            // Send the location of the player's mallet to the second player
			game.sendCommand(game.table.getCommand('m'));
		}
        // Handle potential exceptions related to I/O operations or audio
		catch (IOException | LineUnavailableException  | InterruptedException e1) {
			e1.printStackTrace();
		}

        // Repaint the game interface to reflect the updated mallet position
		game.repaint();
	}
}
