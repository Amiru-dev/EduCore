package lk.corevision.fruitshop.model;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.JOptionPane;
import lk.corevision.fruitshop.validate.JOptionIcon;

/**
 * Utility class for generating QR codes. Provides functionality to create QR
 * code images for various purposes such as employee identification.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public final class QRCodeGenerator {

    private final String nic;
    private boolean generated = false;
    private String filePath;

    public QRCodeGenerator(String nic) {
        this.nic = nic;
        init();
    }

    public boolean isGenerated() {
        return generated;
    }

    public String getFilePath() {
        return filePath;
    }

    private void init() {
        String qrContent = this.nic;
        String homePath = System.getProperty("user.home");

        String folderPath = homePath
                + File.separator + "Documents"
                + File.separator + "FruitShopApp"
                + File.separator + "QR";

        File newFolder = new File(folderPath);

        String fileName = this.nic;

        if (!newFolder.exists() && !newFolder.mkdirs()) {
            JOptionPane.showMessageDialog(null, "Failed to create directory: " + folderPath, "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
            return;
        }

        this.filePath = folderPath + File.separator + fileName + ".png";

        int width = 300;
        int height = 300;

        try {
            generateQRCode(qrContent, this.filePath, width, height);
            this.generated = true;
            JOptionPane.showMessageDialog(null, "QR Code generated successfully.\nLocation: " + folderPath, "Information", JOptionPane.INFORMATION_MESSAGE, JOptionIcon.SUCCESS.setICon());

        } catch (WriterException | IOException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while generating QR Code.", "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        }
    }

    public static void generateQRCode(String content, String filePath, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

        Path path = new File(filePath).toPath();
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
