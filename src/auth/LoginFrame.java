package auth;

import javax.swing.*;
import java.awt.*;

import enrollment.DashboardFrame;

/**
 * Login GUI using Java Swing
 */
/**
 * Login GUI using Java Swing.
 * Provides the interface for users to sign into the system.
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JCheckBox rememberMeCheckBox;
    private AuthService authService;

    public LoginFrame() {
        authService = new AuthService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("University Portal - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(246, 247, 248));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Login Card Panel
        JPanel cardPanel = createCardPanel();
        mainPanel.add(cardPanel);

        // Footer
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel footerLabel = new JLabel("© 2024 University Student System. All rights reserved.");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        footerLabel.setForeground(new Color(128, 128, 128));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(footerLabel);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(246, 247, 248));

        // Logo placeholder
        JLabel logoLabel = new JLabel("🎓");
        logoLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logoLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Title
        JLabel titleLabel = new JLabel("University Portal");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Student Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(80, 109, 149));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(subtitleLabel);

        return panel;
    }

    private JPanel createCardPanel() {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(232, 236, 243)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        // Info message
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(219, 234, 254));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(54, 126, 226)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        JLabel infoLabel = new JLabel("Please sign in to access your dashboard.");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(30, 64, 175));
        infoPanel.add(infoLabel);
        cardPanel.add(infoPanel);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Username field
        JLabel usernameLabel = new JLabel("Username / Student ID");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(usernameLabel);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 218, 230)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        cardPanel.add(usernameField);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(passwordLabel);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 218, 230)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        cardPanel.add(passwordField);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Remember me and forgot password
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setBackground(Color.WHITE);

        rememberMeCheckBox = new JCheckBox("Remember me");
        rememberMeCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
        rememberMeCheckBox.setBackground(Color.WHITE);
        optionsPanel.add(rememberMeCheckBox, BorderLayout.WEST);

        JLabel forgotLabel = new JLabel("<html><u>Forgot Password?</u></html>");
        forgotLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotLabel.setForeground(new Color(54, 126, 226));
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        optionsPanel.add(forgotLabel, BorderLayout.EAST);

        cardPanel.add(optionsPanel);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Login button
        loginButton = new JButton("Login");
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(54, 126, 226));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true); // Fix for macOS visibility
        loginButton.setContentAreaFilled(true);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());
        cardPanel.add(loginButton);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Register button
        registerButton = new JButton("👤 Create an Account");
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(Color.WHITE);
        registerButton.setForeground(new Color(80, 109, 149));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(209, 218, 230)));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> openRegistrationFrame());
        cardPanel.add(registerButton);

        return cardPanel;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (authService.authenticate(username, password)) {
            JOptionPane.showMessageDialog(this,
                    "Login successful! Welcome, " + username,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Open main dashboard
            dispose();
            new DashboardFrame();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password. Please try again.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegistrationFrame() {
        dispose();
        new RegistrationFrame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}