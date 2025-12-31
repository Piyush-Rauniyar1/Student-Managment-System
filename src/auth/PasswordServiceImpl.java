package auth;

public class PasswordServiceImpl implements PasswordService {


    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return hash(rawPassword).equals(hashedPassword);
    }

    @Override
    public String hash(String rawPassword) {
        return Integer.toString(rawPassword.hashCode());
    }
}
