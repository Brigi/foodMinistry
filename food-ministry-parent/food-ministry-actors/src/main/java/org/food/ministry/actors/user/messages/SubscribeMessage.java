package org.food.ministry.actors.user.messages;

public class SubscribeMessage extends AUserInfoMessage {

    public SubscribeMessage(int id, String username, String emailAddress, String password) {
        super(id, username, emailAddress, password);
    }

}
