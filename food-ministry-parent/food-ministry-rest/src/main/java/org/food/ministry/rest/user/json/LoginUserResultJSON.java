package org.food.ministry.rest.user.json;

public class LoginUserResultJSON {

    private long userId;
    
    public LoginUserResultJSON() { }
    
    public LoginUserResultJSON(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
