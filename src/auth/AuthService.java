package auth;

public interface AuthService {
    boolean authenticate(String username, String password);
    boolean changePassword(int userId, String oldPassword, String newPassword);
}
