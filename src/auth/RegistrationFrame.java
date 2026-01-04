package auth;

import javax.swing.*;
import java.awt.*;

/**
 * User registration GUI
 */
/**
 * User registration GUI.
 * Allows new users to create an account in the system.
 */
public class RegistrationFrame extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private JButton createAccountButton;
    private JProgressBar strengthBar;
    private JLabel strengthLabel;
    private AuthService authService;

    public RegistrationFrame() {
        authService = new AuthService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("University SMS - User Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(540, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(246, 247, 248));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Form card
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel);

        // Footer
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel footerLabel = new JLabel("© 2023 University Student Management System. All rights reserved.");
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

        // Logo
        JLabel logoLabel = new JLabel("🎓");
        logoLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logoLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Title
        JLabel titleLabel = new JLabel("Uni SMS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel headingLabel = new JLabel("Create New User Account");
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(headingLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel subtitleLabel = new JLabel("Register a new student, faculty member, or staff administrator");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(107, 114, 128));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(subtitleLabel);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 218, 230)),
                BorderFactory.createEmptyBorder(25, 35, 25, 35)));

        // Username
        addFormField(formPanel, "Username", "👤", usernameField = new JTextField(), "e.g. jsmith24");
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Email
        addFormField(formPanel, "Academic Email", "📧", emailField = new JTextField(), "user@university.edu");
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password
        passwordField = new JPasswordField();
        addFormField(formPanel, "Password", "🔒", passwordField, "••••••••");

        // Password strength indicator
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        JPanel strengthPanel = new JPanel(new BorderLayout(5, 0));
        strengthPanel.setBackground(Color.WHITE);
        strengthPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        strengthBar = new JProgressBar(0, 4);
        strengthBar.setValue(1);
        strengthBar.setStringPainted(false);
        strengthBar.setPreferredSize(new Dimension(0, 6));
        strengthBar.setForeground(Color.RED);
        strengthPanel.add(strengthBar, BorderLayout.CENTER);

        strengthLabel = new JLabel("Weak");
        strengthLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        strengthPanel.add(strengthLabel, BorderLayout.EAST);

        formPanel.add(strengthPanel);

        JLabel hintLabel = new JLabel("Must be at least 8 characters with numbers.");
        hintLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        hintLabel.setForeground(new Color(107, 114, 128));
        formPanel.add(hintLabel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Confirm Password
        addFormField(formPanel, "Confirm Password", "🔄", confirmPasswordField = new JPasswordField(), "••••••••");
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Role selection
        JLabel roleLabel = new JLabel("Account Role");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(roleLabel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        String[] roles = { "Student", "Faculty Member", "Administrator" };
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(roleComboBox);

        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Create Account Button
        createAccountButton = new JButton("Submit");
        createAccountButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 14));
        createAccountButton.setBackground(new Color(54, 126, 226));
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setFocusPainted(false);
        createAccountButton.setBorderPainted(false);
        createAccountButton.setOpaque(true); // Fix for macOS visibility
        createAccountButton.setContentAreaFilled(true);
        createAccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccountButton.addActionListener(e -> handleRegistration());
        formPanel.add(createAccountButton);

        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Login link
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        JLabel loginLabel = new JLabel("Already have an account? ");
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        loginPanel.add(loginLabel);

        JLabel loginLink = new JLabel("<html><u>Log in here</u></html>");
        loginLink.setFont(new Font("Arial", Font.BOLD, 12));
        loginLink.setForeground(new Color(54, 126, 226));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new LoginFrame();
            }
        });
        loginPanel.add(loginLink);
        formPanel.add(loginPanel);

        // Add password strength listener
        passwordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateStrength();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateStrength();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateStrength();
            }
        });

        return formPanel;
    }

    private void addFormField(JPanel panel, String label, String icon, JTextField field, String placeholder) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(fieldLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        fieldPanel.add(iconLabel, BorderLayout.WEST);

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 218, 230)),
                BorderFactory.createEmptyBorder(5, 5, 5, 10)));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldPanel.add(field, BorderLayout.CENTER);

        panel.add(fieldPanel);
    }

    private void updateStrength() {
        String password = new String(passwordField.getPassword());
        int strength = authService.validatePasswordStrength(password);

        strengthBar.setValue(strength);

        switch (strength) {
            case 0:
            case 1:
                strengthBar.setForeground(Color.RED);
                strengthLabel.setText("Weak");
                strengthLabel.setForeground(Color.RED);
                break;
            case 2:
                strengthBar.setForeground(Color.ORANGE);
                strengthLabel.setText("Fair");
                strengthLabel.setForeground(Color.ORANGE);
                break;
            case 3:
                strengthBar.setForeground(new Color(255, 193, 7));
                strengthLabel.setText("Good");
                strengthLabel.setForeground(new Color(180, 130, 0));
                break;
            case 4:
                strengthBar.setForeground(Color.GREEN);
                strengthLabel.setText("Strong");
                strengthLabel.setForeground(new Color(0, 150, 0));
                break;
        }
    }

    private void handleRegistration() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = roleComboBox.getSelectedItem().toString().toLowerCase();

        // Validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (authService.validatePasswordStrength(password) < 2) {
            JOptionPane.showMessageDialog(this,
                    "Password is too weak. Must contain at least 8 characters with numbers.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Register user
        if (authService.registerUser(username, password, email, role)) {
            JOptionPane.showMessageDialog(this,
                    "Account created successfully! You can now log in.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new LoginFrame();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registration failed. Username may already exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}