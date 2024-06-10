package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//import UI.AirHockey;
import UI.UserHome;

// Listener for server-related actions
public class ServerListener implements ActionListener {
    // Reference to the UserHome instance
    private UserHome home;

    // Constructor for ServerListener class
    public ServerListener(UserHome userHome) {
        // Initialize the UserHome reference
        this.home = userHome;
    }

    // Method called when an action is performed
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Close the UserHome window
            home.dispose();
            // Open the server loading page
            new ServerLoadingPage();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException | InterruptedException e1) {
            // Print stack trace if an exception occurs
            e1.printStackTrace();
        }
    }
}
