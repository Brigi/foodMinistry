package org.food.ministry.rest.user.json;

public class LoginUserJSON extends AUserInfo {

    public LoginUserJSON() { }
    
    public LoginUserJSON(String emailAddress, String password) {
        super(emailAddress, password);
    }
}
