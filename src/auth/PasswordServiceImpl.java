package auth;

public class PasswordEncoder implements PasswordService {

    @Override
    public String encode(String password) {
        return password;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
