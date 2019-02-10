package org.food.ministry.actors.user.messages;

public class LoginMessage extends AUserInfoMessage {

    public LoginMessage(int id, String username, String emailAddress, String password) {
        super(id, username, emailAddress, password);
    }
    
}
