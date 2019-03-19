package utils;

import com.mysql.cj.TransactionEventHandler;
import io.CSVHandler;
import java.sql.Connection;
import models.*;
import repository.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import repository.TranasactionRepository;

public class SystemUtils {

    private Map<String, SimpleUser> usersList = new HashMap<>();
    private Map<String, AdminUser> adminUsersList = new HashMap<String, AdminUser>();
    private static DatabaseManager databaseManager;
    private TranasactionRepository tranasactionRepository;

    public SystemUtils(DatabaseManager databaseManager) {
        adminUsersList.put("admin", new AdminUser(0, "admin", "admin", this));
        this.databaseManager = databaseManager;
        this.tranasactionRepository = new TranasactionRepository(databaseManager);
    }

    public Map<String, AdminUser> getAdminUsersList() {
        return adminUsersList;
    }

    public Map<String, SimpleUser> getUsersList() {
        return usersList;
    }

    public void userListPreview() {
        System.out.println();
        UserUtils.printUserHeader();
        List<String> keys = new ArrayList<>(usersList.keySet());
        for (int i = 0; i < keys.size(); i ++) {
            UserUtils.formatUser(usersList.get(keys.get(i)), i + 1);
        }
    }

    public void printUsersCurrentlyOnline() {
        System.out.println();
        UserUtils.printUserHeader();
        List<String> keys = new ArrayList<>(usersList.keySet());
        for (int i = 0; i < keys.size(); i ++) {
            SimpleUser user = usersList.get(keys.get(i));
            if (user.isOnline()) {
                UserUtils.formatUser(user, i + 1);
            }
        }
    }

    public boolean isValidKey(int key) {
        if (key <= usersList.size()) {
            return true;
        } else {
            return false;
        }
    }

    public SimpleUser removeUserFromSystem(int key) {
        List<String> keys = new ArrayList<>(usersList.keySet());
        SimpleUser userToRemove = usersList.get(keys.get(key));
        usersList.remove(userToRemove.getUsername());
        return userToRemove;
    }

    public void addUser(SimpleUser user) {
        usersList.put(user.getUsername(), user);
        String query = "insert into user (username, password, role) values (  ?, ?, ? )";
        try {
            System.out.println(String.format("Saving user '%s' to the database.", user.getUsername()));
            PreparedStatement preparedStatement = databaseManager
                    .getDatabaseConnection()
                    .prepareStatement(query);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, 0);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(String.format("Failed to store user '%s'.", user.getUsername()));
            e.printStackTrace();
        }
        System.out.println("Created User:\n");
        System.out.println(user);
    }

    public Message sendMessage(User user) {
        Connection connection = databaseManager.getDatabaseConnection();
        Scanner scanner = new Scanner(System.in);
        int receiverID = -1;
        while (receiverID < 0 || receiverID > usersList.size()) {

            userListPreview();
            System.out.println("\nPlease select the id of the user you want to sent a message to:");
            receiverID = scanner.nextInt() - 1;
        }

        List<String> keys = new ArrayList<>(usersList.keySet());
        SimpleUser receiver = usersList.get(keys.get(receiverID));

        System.out.println(String.format(
                "Please enter the message you wish to sent to user '%s'. [max 250 characters]:",
                receiver.getUsername()));
        scanner.nextLine();
        String text = scanner.nextLine();
        while (text.length() > 250) {
            System.out.println("Message can't be more than 250 characters.");
            System.out.println(String.format(
                    "Please enter the message you wish to sent to user '%s'. [max 250 characters]:",
                    receiver.getUsername()));
            text = scanner.nextLine();
        }

        Message message = new Message(new Date(), user.getId(), receiver.getId(), text);

        String query = "INSERT  INTO message (date, sender, receiver, text) VALUES  ( ?, ?, ?, ? )";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, new java.sql.Date(message.getDate().getTime()));
            preparedStatement.setObject(2, message.getSender());
            preparedStatement.setObject(3, message.getReceiver());
            preparedStatement.setString(4, message.getText());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(String.format("Failed to execute query '%s'.", query));
            e.printStackTrace();
        }
        CSVHandler handler = new CSVHandler();
        handler.writeData(user.getUsername() + ".csv", message.toString());
        receiver.addToMailbox(message);
        return message;
    }

    public SimpleUser editUserInfo(int key) {
        List<String> keys = new ArrayList<>(usersList.keySet());
        SimpleUser userToEdit = usersList.get(keys.get(key));
        Scanner scanner = new Scanner(System.in);
        int input = - 1;
        while (input != 4) {
            System.out.println("User Info:");
            System.out.println("\t1.Username");
            System.out.println("\t2.Password");
            System.out.println("\t3.User Role");
            System.out.println("\t4.Exit");
            System.out.println("\nPlease select which user info you want to edit");
            input = scanner.nextInt();

            if (input == 1) {
                System.out.println("Please enter a new username:");
                String newUsername = scanner.nextLine();
                userToEdit.setUsername(newUsername);
                System.out.println(String.format("Username updated to '%s.'", newUsername));
            }

            if (input == 2) {
                System.out.println("Please enter a new password:");
                String newPassword = scanner.nextLine();
                userToEdit.setPassword(newPassword);
                System.out.println(String.format("Password updated to '%s.'", newPassword));
            }

            if (input == 3) {
                AdminUser admin = adminUsersList.get(adminUsersList.keySet().toArray()[0]);
                admin.printRoles();
                scanner.nextLine();
                String newUserRole = scanner.nextLine();
                Role role = admin.roleParser(newUserRole);
                userToEdit.setUserRole(role);
                System.out.println(String.format("User Role updated to '%s.'", role));
            }
        }
        return userToEdit;
    }

    public void deleteMessage(SimpleUser user) {

        tranasactionRepository.viewTransactions(user);
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the message you want to delete");

        int input = scanner.nextInt();
        tranasactionRepository.deleteTransaction(user, input);
    }

    public void viewMailBox(SimpleUser user) {

        PreparedStatement ps = null;
        String query = "Select * from message ;";
        ResultSet rs = null;

        Connection connection = databaseManager.getDatabaseConnection();
        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            System.out.println("Retrieved Results:");
            while (rs.next()) {
                System.out.println("\t" + rs.getInt("id") + "\t" + rs.getDate("date") + "\t" + rs.getInt("receiver") + "\t" + rs.getString("text"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SystemUtils.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void editMessage(SimpleUser user) {

        // tranasactionRepository.viewTransactions(user);
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = databaseManager.getDatabaseConnection();
            ps = connection.prepareStatement("Select * from message");
            rs = ps.executeQuery();

            System.out.println("Retrieved Results:");
            while (rs.next()) {
                System.out.println("\t" + rs.getInt("id") + "\t" + rs.getDate("date") + "\t" + rs.getInt("receiver") + "\t" + rs.getString("text"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SystemUtils.class.getName()).log(Level.SEVERE, null, ex);

        }
        System.out.println("Choose the id of the message you want to edit");
        Scanner scanner = new Scanner(System.in);
        Scanner scanner1 = new Scanner(System.in);
        int input = scanner.nextInt();

        System.out.println("What to write");
        String text = scanner1.nextLine();

        tranasactionRepository.updateTransaction(user, input, text);
    }

}
