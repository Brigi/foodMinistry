package org.food.ministry.actors.user.messages;

public class LoginResultMessage extends AResultMessage{

    public LoginResultMessage(int id, int originId, boolean successful, String errorMessage) {
        super(id, originId, successful, errorMessage);
    }
    
}
