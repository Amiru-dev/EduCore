package com.urbantech.smartcity360.decorator;

/**
 * Concrete Component - The base energy meter data stream
 * that provides raw data without any security enhancements.
 */
public class EnergyMeterStream implements DataStream {
    
    private String data;
    private final String meterId;
    
    public EnergyMeterStream(String meterId) {
        this.meterId = meterId;
        this.data = "";
    }
    
    @Override
    public String readData() {
        return "[RAW DATA from Meter-" + meterId + "] " + data;
    }
    
    @Override
    public void writeData(String data) {
        this.data = data;
    }
    
    @Override
    public String getDescription() {
        return "EnergyMeterStream(Meter-" + meterId + ")";
    }
    
    public String getMeterId() {
        return meterId;
    }
}
