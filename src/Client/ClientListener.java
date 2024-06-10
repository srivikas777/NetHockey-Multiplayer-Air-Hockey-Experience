package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import UI.UserHome;

public class ClientListener implements ActionListener {

    // Reference to the UserHome instance
    private UserHome home;

    // Constructor for ClientListener class
    public ClientListener(UserHome userHome) {
        // Initialize the UserHome reference
        this.home = userHome;
    }

    // ActionListener interface method implementation
    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the user session from the UserHome instance
        String userSes = home.getUserSession();
        // Create a new instance of ClientIpAddress with the obtained user session
        new ClientIpAddress(userSes);
        // Dispose the UserHome instance after creating the ClientIpAddress instance
        home.dispose();
    }
}
