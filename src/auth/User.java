package auth;

import java.io.Serializable;

/**
 * User Entity class representing a system user.
 * Stores credentials, role, and details for authentication.
 */
public class User implements Serializable {
    /**
     * Unique identifier for the user.
     */
    private int userId;
    /**
     * The user's unique username for login.
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;
    /**
     * The user's email address.
     */
    private String email;
    /**
     * The role assigned to the user (e.g., "admin", "student", "guest").
     */
    private String role;
    /**
     * Indicates whether the user account is active or not.
     */
    private boolean active;

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Parameterized constructor for creating a user with basic authentication
     * details.
     *
     * @param userId   The unique identifier for the user.
     * @param username The user's username.
     * @param password The user's password.
     */
    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    /**
     * Parameterized constructor for creating a user with full details, setting
     * active status to true by default.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @param email    The user's email address.
     * @param role     The user's role (e.g., admin, student).
     */
    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.active = true;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", active=" + active +
                '}';
    }
}