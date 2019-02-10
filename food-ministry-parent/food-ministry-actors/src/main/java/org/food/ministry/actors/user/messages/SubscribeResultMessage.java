package org.food.ministry.actors.user.messages;

public class SubscribeResultMessage extends AResultMessage {

    public SubscribeResultMessage(int id, int originId, boolean successful, String errorMessage) {
        super(id, originId, successful, errorMessage);
    }

}
