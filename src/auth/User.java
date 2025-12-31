package auth;

public class User {
    int userID;
    String userName;
    String password;

    public User(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
    }

    public int getUserId() {
        return userID;
    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
