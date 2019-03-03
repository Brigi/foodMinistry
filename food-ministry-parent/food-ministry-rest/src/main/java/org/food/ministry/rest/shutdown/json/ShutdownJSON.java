package org.food.ministry.rest.shutdown.json;

public class ShutdownJSON {
    
    private String reason;
    
    public ShutdownJSON() { }
    
    public ShutdownJSON(String reason) { 
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
