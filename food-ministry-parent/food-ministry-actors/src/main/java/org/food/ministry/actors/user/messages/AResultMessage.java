package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.IMessage;

public class AResultMessage implements IMessage {

    private int id;
    private int originId;
    private boolean successful;    
    private String errorMessage;
    
    public AResultMessage(int id, int originId, boolean successful, String errorMessage) {
        this.id = id;
        this.originId = originId;
        this.successful = successful;
        this.errorMessage = errorMessage;
    }

    public int getId() {
        return id;
    }

    public int getOriginId() {
        return originId;
    }
    
    public boolean isSuccessful() {
        return successful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
