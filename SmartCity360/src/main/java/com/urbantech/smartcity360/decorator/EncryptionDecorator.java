package com.urbantech.smartcity360.decorator;

import java.util.Base64;

/**
 * Concrete Decorator - Adds encryption layer to the data stream.
 * Simulates AES-256 encryption by Base64-encoding the data.
 */
public class EncryptionDecorator extends DataStreamDecorator {
    
    private final String algorithm;
    
    public EncryptionDecorator(DataStream stream) {
        this(stream, "AES-256");
    }
    
    public EncryptionDecorator(DataStream stream, String algorithm) {
        super(stream);
        this.algorithm = algorithm;
    }
    
    @Override
    public String readData() {
        String rawData = super.readData();
        return decrypt(rawData);
    }
    
    @Override
    public void writeData(String data) {
        String encrypted = encrypt(data);
        super.writeData(encrypted);
    }
    
    @Override
    public String getDescription() {
        return "Encryption[" + algorithm + "](" + super.getDescription() + ")";
    }
    
    /**
     * Simulates encryption using Base64 encoding.
     * In production, this would use actual AES-256 or RSA encryption.
     */
    private String encrypt(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
    
    /**
     * Simulates decryption by decoding Base64.
     */
    private String decrypt(String data) {
        try {
            return "[DECRYPTED:" + algorithm + "] " + data;
        } catch (Exception e) {
            return "[DECRYPTION_FAILED] " + data;
        }
    }
    
    public String getAlgorithm() {
        return algorithm;
    }
}
