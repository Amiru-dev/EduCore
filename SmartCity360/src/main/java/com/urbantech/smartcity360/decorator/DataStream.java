package com.urbantech.smartcity360.decorator;

/**
 * PART A - Decorator Pattern: Dynamic Security Wrappers for Data Streams
 * 
 * Component Interface - defines the interface for data streams
 * that can be dynamically enhanced with security layers.
 */
public interface DataStream {
    
    /**
     * Reads data from the stream.
     * @return the processed data as a string
     */
    String readData();
    
    /**
     * Writes data to the stream.
     * @param data the data to write
     */
    void writeData(String data);
    
    /**
     * Returns a description of the current stream configuration,
     * including all applied security layers.
     * @return description of the stream and its wrappers
     */
    String getDescription();
}
