package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Role;
import models.SimpleUser;

public class TranasactionRepository {

    private DatabaseManager dbManager;

    public TranasactionRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;

    }

    public void viewTransactions(SimpleUser user) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getDatabaseConnection();
            ps = connection.prepareStatement("Select * from message");
            rs = ps.executeQuery();

            System.out.println("Retrieved Results:");
            while (rs.next()) {
                System.out.println("\t" + rs.getInt("id") + "\t" + rs.getDate("date") + "\t" + rs.getInt("receiver") + "\t" + rs.getString("text"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TranasactionRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateTransaction(SimpleUser user, int messageID, String text) {

        if (user.getUserRole() == Role.READ) {
            System.out.println("You are not authorized to do this action");
        } else {
            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                connection = dbManager.getDatabaseConnection();

                ps = connection.prepareStatement("Update message set text = ? where id = ?");
                ps.setString(1, text);
                ps.setInt(2, messageID);

                int result = ps.executeUpdate();

                System.out.println("Message was edited successfully");

            } catch (SQLException ex) {
                Logger.getLogger(TranasactionRepository.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void deleteTransaction(SimpleUser user, int messageID) {
        System.out.println(user);
        if (user.getUserRole() != Role.Read_WRITE_DELETE) {
            System.out.println("You are not authorized to delete a message");
        } else {

            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                connection = dbManager.getDatabaseConnection();

                ps = connection.prepareStatement("Delete from message where id = ?;");
                ps.setInt(1, messageID);

                int result = ps.executeUpdate();
                System.out.println("Result returned" + result);

            } catch (SQLException ex) {
                Logger.getLogger(TranasactionRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
