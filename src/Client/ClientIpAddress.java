package Client;

import javax.swing.*;

import UI.ClientSetupUI;

public class ClientIpAddress extends JDialog {
    // serialVersionUID for serialization
    private static final long serialVersionUID = 1L;
    
    // JTextField to input IP address
    public JTextField ip;
    
    // User session identifier
    private String userSes;

    // Constructor for ClientIpAddress class
    public ClientIpAddress(String userSes) {
        this.userSes = userSes;
        // Set up UI using ClientSetupUI class
        ClientSetupUI.setupUI(this);
        // Adjust the size of the dialog to fit its contents
        pack();
        // Set the dialog to appear in the center of the screen
        setLocationRelativeTo(null);
        // Make the dialog visible
        setVisible(true);
    }

    // Method to handle the event when the enter button is clicked
    public void enterButtonClicked() {
        // Get the IP address from the text field
        String ipAddress = ip.getText();
        try {
            // Create a new ClientTable instance with the provided IP address and user session
            new ClientTable(ipAddress, userSes);
        } catch (Exception e1) {
            // Print the stack trace if an exception occurs
            e1.printStackTrace();
        }
        // Dispose the dialog after processing
        dispose();
    }

//    // Main method to instantiate the ClientIpAddress class
//    public static void main(String[] args) {
//        // Create a new instance of ClientIpAddress with a test user session
//        new ClientIpAddress("Test User");
//    }
}
