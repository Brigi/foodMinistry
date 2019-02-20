package org.food.ministry.actors.recipespool.messages;

import org.food.ministry.actors.messages.AResultMessage;

public class DeleteRecipeResultMessage extends AResultMessage {
    
    public DeleteRecipeResultMessage(long id, long originId, boolean successful, String errorMessage) {
        super(id, originId, successful, errorMessage);
    }
}
