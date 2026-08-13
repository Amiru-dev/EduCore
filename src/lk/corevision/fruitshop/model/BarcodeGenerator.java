package lk.corevision.fruitshop.model;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import lk.corevision.fruitshop.validate.JOptionIcon;

/**
 * Utility class for generating Barcode PDFs
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class BarcodeGenerator {

    public static void generateBarcodePDF(String stockId, String name, String price, int qty) {
        try {
            Code128Bean code128 = new Code128Bean();
            code128.setHeight(25f);
            code128.setModuleWidth(0.5f);

            code128.setQuietZone(10);
            code128.doQuietZone(true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(baos, "image/x-png", 400, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            code128.generateBarcode(canvas, stockId.trim());
            canvas.finish();

            //write to png file
            FileOutputStream fos = new FileOutputStream("barcode.png");
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();

            //write to pdf
            Image png = Image.getInstance(baos.toByteArray());
            png.setAbsolutePosition(0, 705);
            png.scalePercent(25);

            Document document = new Document();

            PdfPTable table = new PdfPTable(1);
            table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            for (int aw = 0; aw < qty; aw++) {
                Paragraph p = new Paragraph();

                p.add(new Chunk(name.trim(), new Font(Font.FontFamily.TIMES_ROMAN, 25f, Font.BOLD)));
                p.add(new Chunk("\n\n Price : LKR " + price.trim() + "", new Font(Font.FontFamily.TIMES_ROMAN, 25f, Font.BOLD)));
                PdfPTable intable = new PdfPTable(1);
                intable.getDefaultCell().setBorder(PdfPCell.CHUNK);

                intable.addCell(p);
                intable.addCell(png);
                intable.getDefaultCell().setBorder(0);

                table.addCell(intable);
            }

            // Ensure the directory exists
            String userHome = System.getProperty("user.home");
            java.io.File barcodeDir = new java.io.File(userHome, "Documents" + java.io.File.separator + "FruitShopApp" + java.io.File.separator + "Barcodes");

            if (!barcodeDir.exists()) {
                barcodeDir.mkdirs();
            }
            java.io.File pdfFile = new java.io.File(barcodeDir, "barcodes_" + stockId.trim() + ".pdf");

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

            document.open();
            document.add(table);

            document.close();
            writer.close();

        } catch (DocumentException | HeadlessException | IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Unable to generate barcode pdf", "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
            e.printStackTrace();
        }
    }
}
