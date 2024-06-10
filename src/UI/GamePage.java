package UI;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

import GameLogic.Command;
import GameLogic.GamingThread;
import GameLogic.MalletListener;
import GameLogic.MessageCommand;
import GameLogic.ReaderListener;
import GameLogic.ReaderThread;

// Class for the game page
public class GamePage extends JFrame implements ReaderListener {
    // Constants for frame dimensions
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    // Frame dimensions
    public static final int FRAMEWIDTH = 700;
    public static final int FRAMEHEIGHT = 600;
    public static final int GAMEWIDTH = FRAMEWIDTH / 2;
    protected static final int MIDDLE = FRAMEHEIGHT / 2;
    private static final long serialVersionUID = 1L;

    // Table and socket objects
    public Table table;
    protected Socket socket;

    // Labels for player points
    private JLabel points1;
    private JLabel points2;
    private int total1 = 0;
    private int total2 = 0;

    // Chat object
    private Chat chat;

    // Object output stream for sending commands
    private ObjectOutputStream objOut;

    // User object for login info
    private static final LoginPage user = new LoginPage();
    @SuppressWarnings("unused")
    private static String opponentName;

    // Database URL and credentials
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/game_database?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    static final String USER = "root";
    static final String PASS = "";

    // Constructor
    public GamePage() throws IOException {
        setTitle("Air Hockey");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAMEWIDTH, FRAMEHEIGHT);
        setLocationRelativeTo(null);

        // Create container for components
        Container container = getContentPane();

        // Set up the table
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BorderLayout());
        table = new Table();
        table.setPreferredSize(new Dimension(GAMEWIDTH, FRAMEHEIGHT));

        JPanel menuPanel = createMenuPanel();
        chat = new Chat();
        setFocusable(true);
        
        // Add the table to the center
        // container.add(table, BorderLayout.CENTER);

        // Create a panel for scores and chat
        JPanel scoresAndChatPanel = new JPanel();
        scoresAndChatPanel.setLayout(new BoxLayout(scoresAndChatPanel, BoxLayout.Y_AXIS));

        // Add the scores panel to the west
        scoresAndChatPanel.add(menuPanel);

        // Add the chat panel to the east
        scoresAndChatPanel.add(chat);

        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        container.add(table);
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        actionMap.put("enter", new EnterAction());

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        container.add(scoresAndChatPanel);
    }

    // Method to set up the game
    protected void setUp(int number) throws IOException{
        new ReaderThread(socket, this).start();
        OutputStream out = socket.getOutputStream();
        objOut = new ObjectOutputStream(out);
        table.addMouseMotionListener(new MalletListener(this));
        table.setPuck(number);
        setVisible(true);
        new GamingThread(this).start();
    }

    // Method to set up the game with opponent's name
    @SuppressWarnings("static-access")
    protected void setUp(int number, String userSes) throws IOException {
        this.opponentName = userSes;
        new ReaderThread(socket, this).start();
        OutputStream out = socket.getOutputStream();
        objOut = new ObjectOutputStream(out);
        table.addMouseMotionListener(new MalletListener(this));
        table.setPuck(number);
        setVisible(true);
        new GamingThread(this).start();
    }

    // Method to create the menu panel
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Adjust padding

        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.X_AXIS));
        scoresPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally

        JLabel play1 = new JLabel(user.username + ": ");
        play1.setFont(play1.getFont().deriveFont(Font.BOLD));
        play1.setForeground(new Color(52, 152, 219));
        scoresPanel.add(play1);

        total1 = 0;
        points1 = new JLabel(String.valueOf(total1));
        points1.setForeground(new Color(255, 102, 0));
        points1.setFont(points1.getFont().deriveFont(Font.BOLD, 16)); // Increase font size
        scoresPanel.add(points1);

        scoresPanel.add(Box.createHorizontalStrut(35));

        JLabel play2 = new JLabel("Player 2 : ");
        play2.setFont(play2.getFont().deriveFont(Font.BOLD));
        play2.setForeground(new Color(52, 152, 219));
        scoresPanel.add(play2);

        total2 = 0;
        points2 = new JLabel(String.valueOf(total2));
        points2.setForeground(new Color(255, 102, 0));
        points2.setFont(points2.getFont().deriveFont(Font.BOLD, 16)); // Increase font size
        scoresPanel.add(points2);
        
        // scoresPanel.setPreferredSize(new Dimension(350 , 100));

        menuPanel.add(scoresPanel, BorderLayout.CENTER);

        return menuPanel;
    }

    // Method to retrieve scores from the database
    public int getScores(String playerName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int score = 0;
        try {
            Class.forName(JDBC_DRIVER);
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "SELECT * FROM leaderboard WHERE player_name = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, playerName);
            ResultSet rs = stmt.executeQuery();
            @SuppressWarnings("unused")
            String player = rs.getString("player_name");
            score = rs.getInt("score");
            rs.close();
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(stmt != null)
                    stmt.close();
                if(conn != null)
                    conn.close();
            } catch(SQLException se2) {
                se2.printStackTrace();
            }
        }
        return score;
    }

    // Method to move the puck
    public void movePuck() throws IOException, InterruptedException, LineUnavailableException, UnsupportedAudioFileException {
        int point = table.movePuck();
        if (point == 1) {
            points1.setText(String.valueOf(++total1));
            addScore(LoginPage.username, total1);
        } else if (point == 2) {
            points2.setText(String.valueOf(++total2));
            addScore(LoginPage.username, total2);
        }
        repaint();
    }

    // Method to move the mallet
    public void moveMallet(double x, double y) throws LineUnavailableException, IOException {
        table.updateMalletCoordinates(x, y, 1);
    }

    // Method to get puck speed
    public int getPuckSpeed() {
        return table.getPuckSpeed();
    }

    // Method to send command
    public synchronized void sendCommand(Command command) throws IOException, InterruptedException {
        objOut.writeObject(command);
        objOut.flush();
    }

    // Method to update chat
    public void updateChat(String msg) {
        chat.updateChat(msg);
    }

    // Method to update mallet coordinates
    public void updateMalletCoordinates(double x, double y) {
        table.updateMalletCoordinates(x, y, 2);
    }

    // Method to update puck coordinates
    public void updatePuckCoordinates(double x, double y, int speed) {
        table.updatePuck(x, y, speed);
    }

    // Method to synchronize puck
    public void syncPuck() throws IOException, InterruptedException {
    }

    // Method invoked when an object is read
    @Override
    public void onObjectRead(Command command) {
        command.execute(this);
    }

    // Method invoked when socket is closed
    @Override
    public void onCloseSocket(Socket socket) {
        IOUtils.closeQuietly(socket);
    }

    // Method to add score to database
    public void addScore(String player, int score) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.prepareStatement("INSERT INTO leaderboard (player_name, score) VALUES (?, ?) ON DUPLICATE KEY UPDATE score = GREATEST(score, VALUES(score))");
            stmt.setString(1, player);
            stmt.setInt(2, score);
            stmt.executeUpdate();
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(stmt != null)
                    stmt.close();
                if(conn != null)
                    conn.close();
            } catch(SQLException se2) {
                se2.printStackTrace();
            }
        }
    }

    // Inner class for Enter action
    private class EnterAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent arg0) {
            try {
                String text = chat.readText();
                MessageCommand message = new MessageCommand(text);
                sendCommand(message);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
