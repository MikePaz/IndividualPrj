package models;

import utils.UserUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleUser extends User implements Serializable {

    private Role userRole;
    private List<Message> mailbox;
    private boolean isOnlineStatus = false;

    public SimpleUser(int id, String username, String password, Role userRole) {
        super(id, username, password);
        this.userRole = userRole;
        this.mailbox = new ArrayList<Message>();
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public boolean isOnline() {
        return isOnlineStatus;
    }

    public void changeStatus() {
        isOnlineStatus =  ! isOnlineStatus;
    }

    public void addToMailbox(Message message) {
        this.mailbox.add(message);
    }

    @Override
    public String toString() {
        UserUtils.printUserHeader();
        UserUtils.formatUser(this, 0);
        return "";
    }
}
