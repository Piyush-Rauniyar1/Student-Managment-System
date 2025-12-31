package auth;

public class User {
    int userID;
    String userName;
    String password;


    public User(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }
    public User(){

    }


    public int getUserId() {
        return userID;
    }
    public void setUserId(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return userName;
    }
    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
