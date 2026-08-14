package lk.educore.schoolms.model;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.*;
import java.nio.file.*;
import java.util.Hashtable;

/**
 * QR Code Generator
 * Generates QR codes for student ID cards using ZXing library
 */
public class QRCodeGenerator {

    private static final String QR_OUTPUT_DIR = "QR/";

    /**
     * Generate QR code for student ID
     * @param studentId Student ID (e.g., STU-0001)
     * @param studentName Student full name
     * @return Path to generated QR code image
     */
    public static String generateStudentQR(String studentId, String studentName) {
        try {
            // Create QR directory if it doesn't exist
            File qrDir = new File(QR_OUTPUT_DIR);
            if (!qrDir.exists()) {
                qrDir.mkdirs();
            }

            // QR code content: student ID and name
            String qrContent = "StudentID:" + studentId + "|Name:" + studentName;

            // Configure QR code
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            // Generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200, hintMap);

            // Save QR code as PNG
            String fileName = QR_OUTPUT_DIR + studentId + ".png";
            Path path = Paths.get(fileName);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate QR code for teacher ID
     * @param teacherNIC Teacher NIC
     * @param teacherName Teacher full name
     * @return Path to generated QR code image
     */
    public static String generateTeacherQR(String teacherNIC, String teacherName) {
        try {
            // Create QR directory if it doesn't exist
            File qrDir = new File(QR_OUTPUT_DIR);
            if (!qrDir.exists()) {
                qrDir.mkdirs();
            }

            // QR code content: teacher NIC and name
            String qrContent = "TeacherNIC:" + teacherNIC + "|Name:" + teacherName;

            // Configure QR code
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            // Generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200, hintMap);

            // Save QR code as PNG
            String fileName = QR_OUTPUT_DIR + teacherNIC + ".png";
            Path path = Paths.get(fileName);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate QR code with custom content
     * @param content Content to encode in QR
     * @param fileName Output file name
     * @return Path to generated QR code image
     */
    public static String generateCustomQR(String content, String fileName) {
        try {
            // Create QR directory if it doesn't exist
            File qrDir = new File(QR_OUTPUT_DIR);
            if (!qrDir.exists()) {
                qrDir.mkdirs();
            }

            // Configure QR code
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            // Generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200, hintMap);

            // Save QR code as PNG
            String filePath = QR_OUTPUT_DIR + fileName;
            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Delete QR code file
     * @param fileName QR code file name
     */
    public static void deleteQR(String fileName) {
        try {
            File file = new File(QR_OUTPUT_DIR + fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if QR code exists
     * @param fileName QR code file name
     * @return true if exists, false otherwise
     */
    public static boolean qrExists(String fileName) {
        File file = new File(QR_OUTPUT_DIR + fileName);
        return file.exists();
    }
}