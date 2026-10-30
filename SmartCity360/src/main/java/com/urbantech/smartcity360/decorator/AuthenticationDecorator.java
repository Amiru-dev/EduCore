package com.urbantech.smartcity360.decorator;

/**
 * Concrete Decorator - Adds authentication layer to the data stream.
 * Validates access tokens before allowing read/write operations.
 */
public class AuthenticationDecorator extends DataStreamDecorator {
    
    private final String authToken;
    private boolean isAuthenticated;
    
    public AuthenticationDecorator(DataStream stream, String authToken) {
        super(stream);
        this.authToken = authToken;
        this.isAuthenticated = false;
    }
    
    @Override
    public String readData() {
        if (authenticate()) {
            return "[AUTH:VERIFIED] " + super.readData();
        } else {
            return "[AUTH:DENIED] Access denied - invalid credentials";
        }
    }
    
    @Override
    public void writeData(String data) {
        if (authenticate()) {
            super.writeData("[AUTHENTICATED] " + data);
        } else {
            throw new SecurityException("Authentication failed. Cannot write to stream.");
        }
    }
    
    @Override
    public String getDescription() {
        return "Authentication(" + super.getDescription() + ")";
    }
    
    /**
     * Simulates token-based authentication.
     * In production, this would validate JWT tokens or API keys.
     */
    private boolean authenticate() {
        // Simulate authentication - in production, validate against auth server
        isAuthenticated = authToken != null && !authToken.isEmpty() && authToken.length() >= 6;
        return isAuthenticated;
    }
    
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
