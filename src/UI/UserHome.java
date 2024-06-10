package UI;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import Client.ClientListener;
import Server.ServerListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

// UserHome class to display the user's home interface
public class UserHome extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String userSession;
    private JTable scoreTable;
    private DefaultTableModel tableModel;

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/game_database?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";

    // Database credentials
    static final String USER = "root";
    static final String PASS = "";

    // Constructor
    public UserHome(String userSession) {
        this.userSession = userSession;
        initializeGUI();
        setupComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        updateScores(); // Update scores when the frame is created
    }

    // Initialize GUI settings
    private void initializeGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("User Home");
        setResizable(false);

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
    }

    // Setup GUI components
    private void setupComponents() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome, " + userSession + "!");
        welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(welcomeLabel, gbc);

        JButton startGameButton = new JButton("Start Game");
        startGameButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        centerPanel.add(startGameButton, gbc);

        JButton joinGameButton = new JButton("Join Game");
        joinGameButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        gbc.gridx = 1;
        centerPanel.add(joinGameButton, gbc);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        centerPanel.add(logoutButton, gbc);

        // Add centerPanel to the center of the contentPane
        contentPane.add(centerPanel, BorderLayout.CENTER);

        // Initialize table model and JTable
        tableModel = new DefaultTableModel(new String[]{"Player", "Score"}, 0);
        scoreTable = new JTable(tableModel);
        scoreTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        scoreTable.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        scoreTable.setGridColor(new Color(189, 195, 199));
        scoreTable.setSelectionBackground(new Color(52, 152, 219));
        scoreTable.setSelectionForeground(Color.WHITE);

        // Add the table to a JScrollPane and add it to the east of the contentPane
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setBorder(new CompoundBorder(new LineBorder(new Color(189, 195, 199), 2), new EmptyBorder(5, 5, 5, 5)));
        contentPane.add(scrollPane, BorderLayout.EAST);

        // Add Refresh Button
        JButton refreshButton = new JButton("Refresh Scores");
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(new CompoundBorder(new LineBorder(new Color(189, 195, 199), 1), new EmptyBorder(5, 10, 5, 10)));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateScores();
            }
        });

        // Add refreshButton to a new panel and place it under the table
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshPanel.add(Box.createHorizontalStrut(180)); 
        refreshPanel.add(refreshButton);
        contentPane.add(refreshPanel, BorderLayout.SOUTH);

        // Add action listeners to the play button and logout button
        startGameButton.addActionListener(new ServerListener(this));
        joinGameButton.addActionListener(new ClientListener(this));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmLogout = JOptionPane.showConfirmDialog(UserHome.this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
                if (confirmLogout == JOptionPane.YES_OPTION) {
                    dispose();
                    LoginPage obj = new LoginPage();
                    obj.setTitle("Student-Login");
                    obj.setVisible(true);
                }
            }
        });
    }

    // Update scores from the database
    public void updateScores() {
        Connection conn = null;
        PreparedStatement stmt = null;
        tableModel.setRowCount(0); // Clear existing rows in the table model

        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);

            // Execute a query to get the scores
            stmt = conn.prepareStatement("SELECT * FROM leaderboard ORDER BY score DESC LIMIT 5");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String player = rs.getString("player_name");
                int score = rs.getInt("score");
                tableModel.addRow(new Object[]{player, score});
            }

            rs.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Clean-up environment
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
    }
    
    // Getter for the user session
    public String getUserSession() {
        return userSession;
    }

    // Main method
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                @SuppressWarnings("unused")
				UserHome frame = new UserHome("Test User");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
