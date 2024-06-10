package Client;

import java.io.IOException;
import java.net.Socket;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import UI.GamePage;

public class ClientTable extends GamePage {
    // serialVersionUID for serialization
	private static final long serialVersionUID = 1L;

    // Constructor for ClientTable class
	public ClientTable(String serverAddress, String userSes) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        // Set the location of the frame/dialog to appear in the center of the screen
		setLocationRelativeTo(null);
        // Create a new socket with the specified server address and port number
		socket = new Socket(serverAddress, 3769);
        // Call the setUp method to initialize the game interface
		setUp(1 , userSes);
	}
}
