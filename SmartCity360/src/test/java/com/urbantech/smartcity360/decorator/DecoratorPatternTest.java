package com.urbantech.smartcity360.decorator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Part A: Decorator Pattern Tests")
public class DecoratorPatternTest {

    @Test
    @DisplayName("A.1: Base EnergyMeterStream reads and writes raw data")
    void testBaseStream() {
        DataStream stream = new EnergyMeterStream("MTR-100");
        stream.writeData("kWh=450.5;voltage=230");

        String readResult = stream.readData();
        assertTrue(readResult.contains("RAW DATA from Meter-MTR-100"));
        assertTrue(readResult.contains("kWh=450.5;voltage=230"));
        assertEquals("EnergyMeterStream(Meter-MTR-100)", stream.getDescription());
    }

    @Test
    @DisplayName("A.2: EncryptionDecorator wraps stream and encrypts/decrypts")
    void testEncryptionDecorator() {
        DataStream base = new EnergyMeterStream("MTR-101");
        DataStream encrypted = new EncryptionDecorator(base, "AES-256");

        encrypted.writeData("kWh=100");
        String readResult = encrypted.readData();

        assertTrue(readResult.contains("[DECRYPTED:AES-256]"));
        assertTrue(encrypted.getDescription().contains("Encryption[AES-256]"));
        assertTrue(encrypted.getDescription().contains("EnergyMeterStream(Meter-MTR-101)"));
    }

    @Test
    @DisplayName("A.3: AuthenticationDecorator allows read with valid token and denies with invalid")
    void testAuthenticationDecorator() {
        DataStream base = new EnergyMeterStream("MTR-102");
        DataStream authenticatedStream = new AuthenticationDecorator(base, "VALID_TOKEN_123");

        String allowedResult = authenticatedStream.readData();
        assertTrue(allowedResult.contains("[AUTH:VERIFIED]"));

        DataStream deniedStream = new AuthenticationDecorator(base, "bad");
        String deniedResult = deniedStream.readData();
        assertTrue(deniedResult.contains("[AUTH:DENIED]"));
        assertThrows(SecurityException.class, () -> deniedStream.writeData("data"));
    }

    @Test
    @DisplayName("A.4: CompressionDecorator compresses and decompresses")
    void testCompressionDecorator() {
        DataStream base = new EnergyMeterStream("MTR-103");
        DataStream compressed = new CompressionDecorator(base, "GZIP");

        compressed.writeData("SampleEnergyPayload");
        String readResult = compressed.readData();

        assertTrue(readResult.contains("[DECOMPRESSED:GZIP]"));
        assertTrue(compressed.getDescription().contains("Compression[GZIP]"));
    }

    @Test
    @DisplayName("A.5: Stacking multiple security wrappers dynamically at runtime")
    void testStackedDecorators() {
        DataStream stream = new EnergyMeterStream("MTR-200");
        // Layer 1: Auth
        stream = new AuthenticationDecorator(stream, "AUTH_KEY_999");
        // Layer 2: Encrypt
        stream = new EncryptionDecorator(stream, "AES-256");
        // Layer 3: Compress
        stream = new CompressionDecorator(stream, "GZIP");

        stream.writeData("SensitiveSmartCityEnergyData");
        String result = stream.readData();

        // Must traverse all layers: Compression -> Encryption -> Auth -> Base
        assertTrue(result.contains("[DECOMPRESSED:GZIP]"));
        assertTrue(result.contains("[DECRYPTED:AES-256]"));
        assertTrue(result.contains("[AUTH:VERIFIED]"));
        assertTrue(result.contains("RAW DATA from Meter-MTR-200"));

        String desc = stream.getDescription();
        assertEquals("Compression[GZIP](Encryption[AES-256](Authentication(EnergyMeterStream(Meter-MTR-200))))", desc);
    }
}
