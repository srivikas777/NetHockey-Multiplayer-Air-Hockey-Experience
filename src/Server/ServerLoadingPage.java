package Server;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import UI.LoadingPage;

// Class for displaying the server loading page
public class ServerLoadingPage {

    // Constructor for ServerLoadingPage class
    public ServerLoadingPage() throws IOException, LineUnavailableException, UnsupportedAudioFileException, InterruptedException {
        // Create a new LoadingPage frame
        LoadingPage frame = new LoadingPage();
        // Set the frame visible
        frame.setVisible(true);
        // Start a new server thread with the LoadingPage frame
        new ServerThread(frame).start();
    }
}
