package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

// Panel for chat functionality
public class Chat extends JPanel {
    // serialVersionUID for serialization
    private static final long serialVersionUID = 1L;
    
    // Components for chat
    private JTextArea area;
    private JTextField text;
    private JButton sendButton;

    // Constructor for Chat class
    public Chat() {
        // Set layout and preferred size
        setLayout(new BorderLayout());
     // Calculate preferred width based on GamePage.GAMEWIDTH
        int preferredWidth = GamePage.GAMEWIDTH / 2;
        
        // Calculate preferred height based on GamePage.FRAMEHEIGHT
        int preferredHeight = 8 * GamePage.FRAMEHEIGHT / 10;
        
        // Set preferred size
        setPreferredSize(new Dimension(preferredWidth, 500));
        // Set border
        setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(new Color(189, 195, 199), 2)));

        // Initialize text area for chat messages
        area = new JTextArea("Welcome to chat!\n");
        area.setEditable(false);
        area.setLineWrap(true);
        area.setBackground(Color.WHITE);
        area.setForeground(new Color(231, 76, 60)); 
        area.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        area.setBorder(new CompoundBorder(new LineBorder(new Color(189, 195, 199), 1), new EmptyBorder(5, 5, 5, 5))); // Padding
        JScrollPane pane = new JScrollPane(area);
        pane.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(pane, BorderLayout.CENTER);

        // Initialize input panel for typing messages
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Initialize text field for typing messages
        text = new JTextField();
        text.setPreferredSize(new Dimension(300, 30));
        text.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        inputPanel.add(text);

        // Initialize send button for sending messages
        sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(80, 30));
        sendButton.setBackground(new Color(52, 152, 219));
        sendButton.setForeground(Color.BLACK);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(new CompoundBorder(new LineBorder(new Color(189, 195, 199), 1), new EmptyBorder(5, 10, 5, 10)));
        sendButton.addActionListener(e -> {
            try {
                readText();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        inputPanel.add(sendButton);

        // Add input panel to the south side of the panel
        add(inputPanel, BorderLayout.SOUTH);
    }

    // Method to read text from the text field and display it in the chat area
    protected String readText() throws IOException {
        String line = text.getText();
        area.append("Me: " + line + "\n");

        text.setText("");
        return line;
    }

    // Method to update the chat area with the opponent's message
    protected void updateChat(String msg) {
        area.append("Opponent: " + msg + "\n");
    }
}
