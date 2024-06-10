package Server;

import java.io.IOException;
import java.net.ServerSocket;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import UI.GamePage;

// Class representing the server game table
public class ServerTable extends GamePage {
    // serialVersionUID for serialization
	private static final long serialVersionUID = 1L;

    // Constructor for ServerTable class
	public ServerTable(boolean test) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        // Create a server socket and accept connections
		try (ServerSocket serverSocket = new ServerSocket(3769)) {
			socket = serverSocket.accept();
		}
        // Set up the game with server as the first player
		setUp(3); // 3 = server first
	}

    // Method to synchronize the puck position
	@Override
	public void syncPuck() throws IOException, InterruptedException {
        // Send the puck command to the client
		sendCommand(table.getCommand('p'));
	};
}
