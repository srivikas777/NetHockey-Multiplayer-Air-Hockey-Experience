package UI;

import javax.swing.*;
import Client.ClientIpAddress;
import java.awt.*;
import java.awt.event.*;

// Class for setting up the client UI
public class ClientSetupUI {
    
    // Method to set up the client UI
    public static void setupUI(ClientIpAddress clientSetup) {
        // Set properties of the client setup dialog
        clientSetup.setAlwaysOnTop(true);
        clientSetup.setResizable(false);
        clientSetup.setTitle("Join Game");

        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Calculate dimensions and position of the dialog
        int width = (int) (screenWidth * 0.5);
        int height = (int) (screenHeight * 0.5);
        int x = (screenWidth - width) / 2;
        int y = (screenHeight - height) / 2;
        clientSetup.setSize(width, height);
        clientSetup.setLocation(x, y);

        // Create content panel with GridBagLayout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);

        // Set constraints for GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add image to content panel
        ImageIcon imageIcon = new ImageIcon(ClientSetupUI.class.getResource("pics/net.gif"));
        JLabel imageLabel = new JLabel(imageIcon);
        contentPanel.add(imageLabel, gbc);

        // Add instruction label to content panel
        gbc.gridy++;
        gbc.insets = new Insets(0, 10, 10, 10);
        JLabel instructionLabel = new JLabel("Enter IP address to join an already started game server");
        instructionLabel.setFont(new Font("Tahoma", Font.PLAIN, 16)); // Adjust font size and style
        instructionLabel.setForeground(Color.BLACK); // Set text color
        contentPanel.add(instructionLabel, gbc);

        // Add text field for IP address entry to content panel
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 10, 10);
        clientSetup.ip = new JTextField("Please enter your IP address");
        clientSetup.ip.setFont(new Font("Tahoma", Font.PLAIN, 12)); // Adjust font size and style
        clientSetup.ip.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add border
        clientSetup.ip.setPreferredSize(new Dimension(200, 20)); // Set preferred size
        contentPanel.add(clientSetup.ip, gbc);

        // Add enter button to content panel
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 10, 10);
        JButton enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Tahoma", Font.PLAIN, 14)); // Adjust font size and style
        enterButton.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add border
        // Action listener for the enter button
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientSetup.enterButtonClicked();
            }
        });
        contentPanel.add(enterButton, gbc);

        // Set content panel as the content pane of the client setup dialog
        clientSetup.setContentPane(contentPanel);
        clientSetup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        clientSetup.setResizable(false);
        clientSetup.setVisible(true);
    }
}
