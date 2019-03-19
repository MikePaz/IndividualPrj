package utils;

import models.SimpleUser;

public class UserUtils {

    /**
     * View the transacted data between users - READ
     *
     */
    /**
     * View and Edit the transacted data between the users - READ_WRITE
     *
     */
    /**
     * View, Edit and Delete the transacted data between the users -
     * Read_WRITE_DELETE
     *
     */
    public static void printUserHeader() {
        System.out.println(String.format(" %15s %10s %30s %25s %10s %25s %10s", "ID", "|", "Username", "|", "Password", "|", "Role"));
        System.out.println(String.format("%s", "---------------------------------------------------------------------------------------------------------------------------------------------------"));
    }

    public static void formatUser(SimpleUser user, int id) {
        System.out.println(String.format("%16s %10s %30s %25s %10s %25s %10s", id + "", "|",
                user.getUsername(), "|", user.getPassword(), "|", user.getUserRole().toString()));
    }
}
