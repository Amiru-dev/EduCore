package lk.educore.schoolms.model;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * Barcode Generator Generates barcodes for library books using Barcode4J
 * library
 */
public class BarcodeGenerator {

    private static final String BARCODE_OUTPUT_DIR = "Barcodes/";

    /**
     * Generate Code 128 barcode for library book
     *
     * @param bookId Book ID or ISBN
     * @return Path to generated barcode image
     */
    public static String generateBookBarcode(String bookId) {
        try {
            // Create barcode directory if it doesn't exist
            File barcodeDir = new File(BARCODE_OUTPUT_DIR);
            if (!barcodeDir.exists()) {
                barcodeDir.mkdirs();
            }

            // Configure Code 128 barcode
            Code128Bean barcodeBean = new Code128Bean();
            barcodeBean.setHeight(15f);
            barcodeBean.setModuleWidth(0.3);
            barcodeBean.doQuietZone(true);
            barcodeBean.setQuietZone(10);

            // Generate barcode
            String fileName = BARCODE_OUTPUT_DIR + bookId + ".png";
            File outputFile = new File(fileName);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                    outputStream, "image/x-png", 300, BufferedImage.TYPE_BYTE_BINARY, false, 0
            );

            barcodeBean.generateBarcode(canvas, bookId);
            canvas.finish();
            outputStream.close();

            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate Code 39 barcode
     *
     * @param data Data to encode
     * @param fileName Output file name
     * @return Path to generated barcode image
     */
    public static String generateCode39Barcode(String data, String fileName) {
        try {
            // Create barcode directory if it doesn't exist
            File barcodeDir = new File(BARCODE_OUTPUT_DIR);
            if (!barcodeDir.exists()) {
                barcodeDir.mkdirs();
            }

            // Configure Code 39 barcode
            Code39Bean barcodeBean = new Code39Bean();
            barcodeBean.setHeight(15f);
            barcodeBean.setModuleWidth(0.3);
            barcodeBean.doQuietZone(true);
            barcodeBean.setQuietZone(10);

            // Generate barcode
            String filePath = BARCODE_OUTPUT_DIR + fileName;
            File outputFile = new File(filePath);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                    outputStream, "image/x-png", 300, BufferedImage.TYPE_BYTE_BINARY, false, 0
            );

            barcodeBean.generateBarcode(canvas, data);
            canvas.finish();
            outputStream.close();

            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate EAN-13 barcode
     *
     * @param data 12 or 13 digit data
     * @param fileName Output file name
     * @return Path to generated barcode image
     */
    public static String generateEAN13Barcode(String data, String fileName) {
        try {
            // Create barcode directory if it doesn't exist
            File barcodeDir = new File(BARCODE_OUTPUT_DIR);
            if (!barcodeDir.exists()) {
                barcodeDir.mkdirs();
            }

            // Configure EAN-13 barcode
            EAN13Bean barcodeBean = new EAN13Bean();
            barcodeBean.setHeight(15f);
            barcodeBean.setModuleWidth(0.3);
            barcodeBean.doQuietZone(true);
            barcodeBean.setQuietZone(10);

            // Generate barcode
            String filePath = BARCODE_OUTPUT_DIR + fileName;
            File outputFile = new File(filePath);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                    outputStream, "image/x-png", 300, BufferedImage.TYPE_BYTE_BINARY, false, 0
            );

            barcodeBean.generateBarcode(canvas, data);
            canvas.finish();
            outputStream.close();

            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Delete barcode file
     *
     * @param fileName Barcode file name
     */
    public static void deleteBarcode(String fileName) {
        try {
            File file = new File(BARCODE_OUTPUT_DIR + fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if barcode exists
     *
     * @param fileName Barcode file name
     * @return true if exists, false otherwise
     */
    public static boolean barcodeExists(String fileName) {
        File file = new File(BARCODE_OUTPUT_DIR + fileName);
        return file.exists();
    }

    /**
     * Generate barcode with custom dimensions
     *
     * @param data Data to encode
     * @param fileName Output file name
     * @param height Barcode height
     * @param moduleWidth Module width
     * @return Path to generated barcode image
     */
    public static String generateCustomBarcode(String data, String fileName, float height, float moduleWidth) {
        try {
            // Create barcode directory if it doesn't exist
            File barcodeDir = new File(BARCODE_OUTPUT_DIR);
            if (!barcodeDir.exists()) {
                barcodeDir.mkdirs();
            }

            // Configure Code 128 barcode with custom dimensions
            Code128Bean barcodeBean = new Code128Bean();
            barcodeBean.setHeight(height);
            barcodeBean.setModuleWidth(moduleWidth);
            barcodeBean.doQuietZone(true);
            barcodeBean.setQuietZone(10);

            // Generate barcode
            String filePath = BARCODE_OUTPUT_DIR + fileName;
            File outputFile = new File(filePath);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                    outputStream, "image/x-png", 300, BufferedImage.TYPE_BYTE_BINARY, false, 0
            );

            barcodeBean.generateBarcode(canvas, data);
            canvas.finish();
            outputStream.close();

            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
