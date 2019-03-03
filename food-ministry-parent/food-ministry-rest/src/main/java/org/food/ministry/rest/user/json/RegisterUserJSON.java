package org.food.ministry.rest.user.json;

public class RegisterUserJSON extends AUserInfo {
    
    private String username;

    public RegisterUserJSON() { }
    
    public RegisterUserJSON(String username, String emailAddress, String password) {
        super(emailAddress, password);
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
