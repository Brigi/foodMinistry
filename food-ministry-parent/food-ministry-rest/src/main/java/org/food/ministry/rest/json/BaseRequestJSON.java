package org.food.ministry.rest.json;

public class BaseRequestJSON {

    private long userId;
    
    public BaseRequestJSON() { }
    
    public BaseRequestJSON(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
