package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.IMessage;

public class AUserInfoMessage implements IMessage {

    private int id;
    private String username;
    private String emailAddress;
    private String password;
    
    public AUserInfoMessage(int id, String username, String emailAddress, String password) {
        this.id = id;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }
}
