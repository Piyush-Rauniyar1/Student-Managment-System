package auth;

public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private PasswordService passwordService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null &&
                passwordService.matches(password, user.getPassword());
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        String hashed = passwordService.hash(newPassword);
        return userRepository.updatePassword(userId, hashed);
    }
}
