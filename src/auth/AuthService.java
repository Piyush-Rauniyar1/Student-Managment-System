package auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Authentication service for credential validation and password management
 */
/**
 * Service class for handling authentication and user registration logic.
 */
public class AuthService {
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Authenticate user with username and password
     *
     * @param username Username
     * @param password Password
     * @return true if authentication successful
     */
    /**
     * Authenticates a user with username and password.
     *
     * @param username The username to check.
     * @param password The raw password to verify.
     * @return true if credentials are valid, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return false;
        }

        User user = userDAO.findByUsername(username);

        if (user != null && user.isActive()) {
            String hashedPassword = hashPassword(password);
            return hashedPassword.equals(user.getPassword());
        }

        return false;
    }

    /**
     * Change user password
     *
     * @param userId      User ID
     * @param oldPassword Current password
     * @param newPassword New password
     * @return true if password changed successfully
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.length() < 8) {
            System.err.println("Password must be at least 8 characters long");
            return false;
        }

        User user = userDAO.findByUsername(getUsernameById(userId));

        if (user != null) {
            String hashedOldPassword = hashPassword(oldPassword);

            if (hashedOldPassword.equals(user.getPassword())) {
                String hashedNewPassword = hashPassword(newPassword);
                return userDAO.updatePassword(userId, hashedNewPassword);
            } else {
                System.err.println("Current password is incorrect");
            }
        }

        return false;
    }

    /**
     * Validate password strength
     *
     * @param password Password to validate
     * @return Strength level: 0-4
     */
    public int validatePasswordStrength(String password) {
        if (password == null || password.isEmpty())
            return 0;

        int strength = 0;

        if (password.length() >= 8)
            strength++;
        if (password.matches(".*[a-z].*"))
            strength++;
        if (password.matches(".*[A-Z].*"))
            strength++;
        if (password.matches(".*\\d.*"))
            strength++;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*"))
            strength++;

        return Math.min(strength, 4);
    }

    /**
     * Hash password using SHA-256
     *
     * @param password Plain text password
     * @return Hashed password
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Fallback (not recommended for production)
        }
    }

    /**
     * Get username by user ID
     *
     * @param userId User ID
     * @return Username
     */
    private String getUsernameById(int userId) {
        // This is a helper method - in a real application,
        // you might want to add this to UserDAO
        return null; // Implement as needed
    }

    /**
     * Register new user
     *
     * @param username Username
     * @param password Password
     * @param email    Email
     * @param role     User role
     * @return true if registration successful
     */
    /**
     * Registers a new user in the system.
     *
     * @param username Desired username.
     * @param password Desired password.
     * @param email    User's email.
     * @param role     User's role.
     * @return true if registration is successful, false otherwise.
     */
    public boolean registerUser(String username, String password, String email, String role) {
        // Check if username already exists
        if (userDAO.findByUsername(username) != null) {
            System.err.println("Username already exists");
            return false;
        }

        // Validate password strength
        if (validatePasswordStrength(password) < 2) {
            System.err.println("Password is too weak");
            return false;
        }

        // Hash password and create user
        String hashedPassword = hashPassword(password);
        User user = new User(username, hashedPassword, email, role);

        return userDAO.saveUser(user);
    }
}