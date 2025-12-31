package auth;

public interface AuthService {
    String encode(String password);
    boolean matches(String rawPassword, String encodedPassword);
}
