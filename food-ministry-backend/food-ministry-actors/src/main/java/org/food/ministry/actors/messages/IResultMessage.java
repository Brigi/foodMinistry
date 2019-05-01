package org.food.ministry.actors.messages;

public interface IResultMessage extends IMessage {
    
    long getId();
    
    long getOriginId();
    
    boolean isSuccessful();
    
    String getErrorMessage();
}
