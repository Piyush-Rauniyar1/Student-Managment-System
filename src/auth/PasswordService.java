package auth;

public interface PasswordService {
    boolean matches(String rawPassword, String hashedPassword);
    String hash(String rawPassword);
}
