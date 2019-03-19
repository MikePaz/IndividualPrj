package models;

import utils.SystemUtils;

import java.util.Scanner;
import java.util.stream.IntStream;

public class AdminUser extends User {

    private SystemUtils systemUtils;

    public AdminUser(int id, String username, String password, SystemUtils systemUtils) {
        super(id, username, password);
        this.systemUtils = systemUtils;
    }

    public SimpleUser createUser() {
        Scanner scanner = new Scanner(System.in);
        printMessage();
        String[] credentials = scanner.nextLine().split(",");
        while (credentials.length != 3) {
            printMessage();
            credentials = scanner.nextLine().split(",");
        }
        Role role = roleParser(credentials[2]);
        if (role == null) {
            role = Role.READ;
            System.out.println("Invalid role id specified, creating user as with 'READ' role.\n"
                    + "You can change the user role from the edit page.");
        }
        return new SimpleUser(systemUtils.getAdminUsersList().size() + 1,
                credentials[0],
                credentials[1],
                role);
    }

    public boolean deleteUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease select the id of the user you want to remove from the system:");
        int input = scanner.nextInt() - 1;
        if (systemUtils.isValidKey(input)) {
            SimpleUser deletedUser = systemUtils.removeUserFromSystem(input);
            System.out.println(String.format("User '%s' is removed from the system.", deletedUser.getUsername()));
            return true;
        } else {
            System.out.println(String.format("No user exists with identifier '%d'.", input));
            return false;
        }
    }

    public void editUserInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease select the id of the user you want to edit:");
        int input = scanner.nextInt() - 1;
        if (systemUtils.isValidKey(input)) {
            SimpleUser editedUser = systemUtils.editUserInfo(input);
            System.out.println(String.format("User's '%s' information where updated successfully.", editedUser.getUsername()));
        } else {
            System.out.println(String.format("No user exists with identifier '%d'.", input));
        }
    }

    private void printMessage() {
        System.out.println("Please enter the user credentials comma separated\n\t[username,password,role]");
        printRoles();
    }

    public void printRoles() {
        System.out.println(String.format("Select one of the id to assign a user role:"));
        IntStream.range(0, Role.values().length)
                .mapToObj(i -> String.format("\t%s\t%d", Role.values()[i], i))
                .forEach(System.out::println);
    }

    public Role roleParser(String roleID) {
        int role = -1;
        try {
            role = Integer.parseInt(roleID);
        } catch (NumberFormatException e) {
            System.out.println(String.format("Invalid Role id: %s", roleID));
        }
        switch (role) {
            case 0:
                return Role.values()[0];
            case 1:
                return Role.values()[1];
            case 2:
                return Role.values()[2];
            case 3:
                return Role.values()[3];
            default:
                return null;
        }
    }
}
