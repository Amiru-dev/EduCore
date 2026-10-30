package com.urbantech.smartcity360.decorator;

/**
 * Concrete Decorator - Adds compression layer to the data stream.
 * Simulates data compression to reduce bandwidth usage.
 */
public class CompressionDecorator extends DataStreamDecorator {
    
    private final String compressionType;
    
    public CompressionDecorator(DataStream stream) {
        this(stream, "GZIP");
    }
    
    public CompressionDecorator(DataStream stream, String compressionType) {
        super(stream);
        this.compressionType = compressionType;
    }
    
    @Override
    public String readData() {
        String compressedData = super.readData();
        return decompress(compressedData);
    }
    
    @Override
    public void writeData(String data) {
        String compressed = compress(data);
        super.writeData(compressed);
    }
    
    @Override
    public String getDescription() {
        return "Compression[" + compressionType + "](" + super.getDescription() + ")";
    }
    
    /**
     * Simulates compression. In production, this would use
     * actual GZIP/LZ4/Snappy compression algorithms.
     */
    private String compress(String data) {
        int originalSize = data.length();
        // Simulate compression ratio
        int compressedSize = (int) (originalSize * 0.6);
        return "[COMPRESSED:" + compressionType + " ratio=60%] " + data;
    }
    
    /**
     * Simulates decompression.
     */
    private String decompress(String data) {
        return "[DECOMPRESSED:" + compressionType + "] " + data;
    }
    
    public String getCompressionType() {
        return compressionType;
    }
}
