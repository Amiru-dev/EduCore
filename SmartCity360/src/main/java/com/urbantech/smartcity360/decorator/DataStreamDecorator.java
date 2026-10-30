package com.urbantech.smartcity360.decorator;

/**
 * Base Decorator - Abstract class that wraps a DataStream 
 * and delegates calls to the wrapped stream.
 * All concrete decorators extend this class.
 */
public abstract class DataStreamDecorator implements DataStream {
    
    protected DataStream wrappedStream;
    
    public DataStreamDecorator(DataStream stream) {
        this.wrappedStream = stream;
    }
    
    @Override
    public String readData() {
        return wrappedStream.readData();
    }
    
    @Override
    public void writeData(String data) {
        wrappedStream.writeData(data);
    }
    
    @Override
    public String getDescription() {
        return wrappedStream.getDescription();
    }
}
