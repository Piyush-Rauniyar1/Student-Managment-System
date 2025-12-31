package auth;

public interface UserRepository {
    User findByUsername(String username);
    User findById(int userId);
    boolean saveUser(User user);
    boolean updatePassword(int userId, String password);
    boolean deleteUser(int userId);


}
