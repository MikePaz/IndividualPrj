package utils;

import java.sql.SQLException;
import models.SimpleUser;

import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Role;

public class UserSession {

    private Scanner scanner;
    private SimpleUser user;
    private SystemUtils systemUtils;
    private boolean activeSession = true;

    public UserSession(SystemUtils systemUtils) {
        this.systemUtils = systemUtils;
        System.out.println("Starting user session ..");
        scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        String username = scanner.nextLine().trim();
        System.out.println("Enter your password");
        String password = scanner.nextLine().trim();
        if (checkCredentials(username, password)) {
            startUserSession();
        }
    }

    private void startUserSession() {
        user.changeStatus();
        int userActivity;
        while (activeSession) {
            printUserMenu();
            userActivity = scanner.nextInt();
            if (userActivity == 1) {
                systemUtils.printUsersCurrentlyOnline();
            }
            if (userActivity == 2) {

                systemUtils.sendMessage(user);

            }

            if (userActivity == 3) {
                systemUtils.viewMailBox(user);
            }

            if (userActivity == 4) {

                systemUtils.deleteMessage(user);
            }

            if (userActivity == 5) {

                systemUtils.editMessage(user);

            }

            if (userActivity == 6) {
                terminateSession();
            }
        }
    }

    private boolean checkCredentials(String username, String password) {
        Map<String, SimpleUser> users = systemUtils.getUsersList();
        System.out.println(users.size());
        users.keySet().forEach(System.out::println);
        System.out.println(username);
        System.out.println(password);
        if (users.containsKey(username)
                && users.get(username).getPassword().equals(password)) {
            this.user = users.get(username);
            System.out.println(String.format("User '%s' logged into the system.", username));
            return true;
        } else {
            System.out.println(String.format(
                    "No user '%s' with password '%s' exists into the system.",
                    username,
                    password));
        }
        return false;

    }

    private void printUserMenu() {
        System.out.println("\nPlease select one of the following interactions:");
        System.out.println("1. See all users currently online.");
        System.out.println("2. Sent a message");
        System.out.println("3. Mailbox");
        System.out.println("4. Delete a message");
        System.out.println("5. Edit a message");
        System.out.println("6. Logout");

    }

    private void terminateSession() {
        System.out.println(String.format("Terminating Session for user '%s'", user.getUsername()));
        activeSession = false;
        user.changeStatus();
    }
}
