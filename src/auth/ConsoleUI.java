package auth.ui;

import auth.*;
import auth.AuthService;
import auth.AuthServiceImpl;
import auth.DatabaseConfig;
import auth.PasswordService;
import auth.PasswordServiceImpl;
import auth.User;
import auth.UserDAO;


import java.sql.Connection;
import java.util.Scanner;

public class ConsoleUI {

    public static void main(String[] args) {

        Connection connection = DatabaseConfig.getConnection();
        UserDAO userDAO = new UserDAO(connection);
        PasswordService passwordService = new PasswordServiceImpl();
        AuthService authService = new AuthServiceImpl(userDAO, passwordService);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Username: ");
                String username = sc.nextLine();
                System.out.print("Password: ");
                String password = sc.nextLine();

                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordService.hash(password));

                System.out.println(
                        userDAO.saveUser(user) ? "User Registered" : "Failed"
                );

            } else if (choice == 2) {
                System.out.print("Username: ");
                String username = sc.nextLine();
                System.out.print("Password: ");
                String password = sc.nextLine();

                System.out.println(
                        authService.authenticate(username, password)
                                ? "Login Successful"
                                : "Invalid Credentials"
                );

            } else {
                break;
            }
        }
    }
}
