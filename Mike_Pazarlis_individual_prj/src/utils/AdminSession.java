package utils;

import models.AdminUser;
import models.SimpleUser;

import java.util.Map;
import java.util.Scanner;

public class AdminSession {

    private Scanner scanner;
    private AdminUser adminUser;
    private boolean activeSession = true;
    private SystemUtils systemUtils;

    public AdminSession(SystemUtils systemUtils) {
        this.systemUtils = systemUtils;
        System.out.println("Starting admin session ..");
        scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        String adminUsername = scanner.nextLine().trim();
        System.out.println("Enter your password");
        String adminPassword = scanner.nextLine().trim();
        if (isValidAdmin(adminUsername, adminPassword)) {
            startAdminSession();
        }
    }

    private void startAdminSession() {
        int userActivity;
        while (activeSession) {
            printAdminUserMenu();
            userActivity = scanner.nextInt();
            if (userActivity == 1) {
                SimpleUser newUser = adminUser.createUser();
                System.out.println(newUser);
                systemUtils.addUser(newUser);
            }

            if (userActivity == 2) {
                systemUtils.userListPreview();
                adminUser.editUserInfo();
            }

            if (userActivity == 3) {
                systemUtils.userListPreview();
            }

            if (userActivity == 4) {
                systemUtils.userListPreview();
                adminUser.deleteUser();
            }
            if (userActivity == 5) {
                terminateSession();
            }
        }
    }

    private boolean isValidAdmin(String username, String password) {
        Map<String, AdminUser> adminUsers = systemUtils.getAdminUsersList();
        if (adminUsers.containsKey(username)
                && adminUsers.get(username).getPassword().equals(password)) {
            this.adminUser = adminUsers.get(username);
            System.out.println(String.format("Admin user '%s' logged into the system.", username));
            return true;
        } else {
            System.out.println(String.format(
                    "No admin user '%s' with password '%s' registered into the system.",
                    username,
                    password));
        }
        return false;
    }

    private void printAdminUserMenu() {
        System.out.println("\nPlease select one of the following interactions:");
        System.out.println("1. Create a new User.");
        System.out.println("2. Update a user's information");
        System.out.println("3. View the users registered in the system");
        System.out.println("4. Delete a user from the system.");
        System.out.println("5. Logout.");
    }

    private void terminateSession() {
        System.out.println(String.format("Terminating Session for admin '%s'", adminUser.getUsername()));
        activeSession = false;
    }
}
