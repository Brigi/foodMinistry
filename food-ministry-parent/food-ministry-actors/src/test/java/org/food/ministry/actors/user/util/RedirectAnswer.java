package org.food.ministry.actors.user.util;

import org.mockito.stubbing.Answer;

public class RedirectAnswer<T> {

    private T storedObject;
    
    public T getStoredObject() {
        return storedObject;
    }

    public void setStoredObject(T storedObject) {
        this.storedObject = storedObject;
    }
    
    public Answer<Object> setRedirectedUserAnswer() {
        return (invocation) -> {
            T objectToStore = (T) invocation.getArgument(0);
            setStoredObject(objectToStore);
            return null;
        };
    }

    public Answer<T> getRedirectedUserAnswer() {
        return (invocation) -> {
            return getStoredObject();
        };
    }
}
