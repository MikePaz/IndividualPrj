/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mike_pazarlis_individual_prj;

import java.util.*;
import repository.DatabaseManager;
import utils.AdminSession;
import utils.SystemUtils;
import utils.UserSession;

public class Mike_Pazarlis_individual_prj {

    public static void main(String[] args) {
        System.out.println("Starting the application ...");
        SystemUtils systemUtils = new SystemUtils(new DatabaseManager());
        while (true) {
            System.out.println("Please enter '1' for Admin Mode or '2' to login as a user:");
            Scanner scanner = new Scanner(System.in);
            int input = scanner.nextInt();
            if (input == 1) {

                new AdminSession(systemUtils);
            } else {
                new UserSession(systemUtils);
            }
        }
    }

}
