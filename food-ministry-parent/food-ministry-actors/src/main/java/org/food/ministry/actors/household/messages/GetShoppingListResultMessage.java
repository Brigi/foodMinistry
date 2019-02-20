package org.food.ministry.actors.household.messages;

import org.food.ministry.actors.messages.AResultMessage;

public class GetShoppingListResultMessage extends AResultMessage {

    private final long shoppingListId;
    
    public GetShoppingListResultMessage(long id, long originId, boolean successful, String errorMessage, long shoppingListId) {
        super(id, originId, successful, errorMessage);
        this.shoppingListId = shoppingListId;
    }

    public long getShoppingListId() {
        return shoppingListId;
    }
}
