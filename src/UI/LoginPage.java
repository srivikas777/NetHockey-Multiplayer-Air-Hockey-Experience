package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Class for the login page
public class LoginPage extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createUserButton;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JPanel contentPane;

    static String username;

    // Constructor
    public LoginPage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("User Login");
        initializeComponents();
        setUpLayout();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to initialize components
    private void initializeComponents() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new GridBagLayout());

        lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));

        lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));

        usernameField = new JTextField();
        usernameField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setColumns(10);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userName = usernameField.getText();
                username = userName;
                String password = new String(passwordField.getPassword());
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/game_database?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                            "root","");

                    PreparedStatement st = connection.prepareStatement("Select name, password from players where name=? and password=?");

                    st.setString(1, userName);
                    st.setString(2, password);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        dispose();
                        UserHome ah = new UserHome(userName);
                        ah.setTitle("Welcome");
                        ah.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(loginButton, "Wrong Username & Password");
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });

        createUserButton = new JButton("Create User");
        createUserButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        createUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField newUser = new JTextField();
                JPasswordField newPassword = new JPasswordField();
                Object[] message = {
                        "Username:", newUser,
                        "Password:", newPassword
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Create New User", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/game_database?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                                "root","");

                        PreparedStatement st = connection.prepareStatement("INSERT INTO players(name, password) VALUES(?, ?)");

                        st.setString(1, newUser.getText());
                        st.setString(2, new String(newPassword.getPassword()));
                        st.executeUpdate();

                        JOptionPane.showMessageDialog(createUserButton, "New user created successfully");
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        });
    }

    // Method to set up layout
    private void setUpLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(new JLabel("Login"), gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        contentPane.add(lblUsername, gbc);

        gbc.gridx = 1;
        contentPane.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(lblPassword, gbc);

        gbc.gridx = 1;
        contentPane.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        contentPane.add(loginButton, gbc);

        gbc.gridy = 4;
        contentPane.add(createUserButton, gbc);

        setContentPane(contentPane);
    }

    // Main method for testing
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                @SuppressWarnings("unused")
				LoginPage frame = new LoginPage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Getter method for username
    @SuppressWarnings("unused")
	private String getUsername() {
        return username;
    }
}
