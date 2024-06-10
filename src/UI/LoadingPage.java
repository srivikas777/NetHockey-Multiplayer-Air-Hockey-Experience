package UI;

import javax.swing.*;
import java.awt.*;

// Class for the loading page
public class LoadingPage extends JDialog {
    private static final long serialVersionUID = 1L;

    // Constructor
    public LoadingPage() {
        initializeGUI();
        setupComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to initialize GUI
    private void initializeGUI() {
        setAlwaysOnTop(true);
        setResizable(false);
        setTitle("Loading");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int width = (int) (screenWidth * 0.5);
        int height = (int) (screenHeight * 0.5);
        int x = (screenWidth - width) / 2;
        int y = (screenHeight - height) / 2;
        setSize(width, height);
        setLocation(x, y);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        getContentPane().add(mainPanel);
    }

    // Method to set up components
    private void setupComponents() {
        JPanel mainPanel = (JPanel) getContentPane().getComponent(0);

        ImageIcon icon = new ImageIcon(getClass().getResource("pics/resize.gif"));
        JLabel pic = new JLabel(icon);
        pic.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(pic);

        JLabel loadingLabel = new JLabel("Loading...");
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
        mainPanel.add(loadingLabel);

        JLabel waitingLabel = new JLabel("Waiting for 2nd player to join...");
        waitingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        waitingLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        mainPanel.add(waitingLabel);

        mainPanel.add(Box.createVerticalGlue());
    }

    // Main method for testing
    public static void main(String[] args) {
        new LoadingPage();
    }
}
