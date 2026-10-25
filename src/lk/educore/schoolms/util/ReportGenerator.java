package lk.educore.schoolms.util;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * ReportGenerator - Utility class for generating JasperReports
 * Provides methods to compile, fill, and display reports
 */
public class ReportGenerator {

    private static final String REPORTS_PATH = "reports/";

    /**
     * Generate and display a report in a viewer window
     * @param reportName Name of the report file (without .jasper or .jrxml extension)
     * @param parameters Map of parameters to pass to the report
     */
    public static void generateReport(String reportName, Map<String, Object> parameters) {
        generateReport(reportName, parameters, null);
    }

    /**
     * Generate and display a report in a viewer window with custom title
     * @param reportName Name of the report file (without .jasper or .jrxml extension)
     * @param parameters Map of parameters to pass to the report
     * @param title Custom title for the report viewer window
     */
    public static void generateReport(String reportName, Map<String, Object> parameters, String title) {
        try {
            File jasperFile = new File(REPORTS_PATH + reportName + ".jasper");
            File jrxmlFile = new File(REPORTS_PATH + reportName + ".jrxml");

            JasperReport jasperReport;
            if (jasperFile.exists()) {
                jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
            } else if (jrxmlFile.exists()) {
                try {
                    jasperReport = JasperCompileManager.compileReport(jrxmlFile.getAbsolutePath());
                } catch (Exception e) {
                    System.err.println("Error compiling report: " + e.getMessage());
                    System.err.println("File: " + jrxmlFile.getAbsolutePath());
                    System.err.println("Full stack trace:");
                    e.printStackTrace();
                    
                    JOptionIcon.showErrorDialog(null, "Error compiling report. Check console for details.\nFile: " + jrxmlFile.getAbsolutePath());
                    return;
                }
            } else {
                JOptionIcon.showErrorDialog(null, "Report template not found: " + reportName);
                return;
            }

            // Get database connection from MySQL class
            Connection connection = MySQL.getConnection();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // Check if report has pages
            if (jasperPrint.getPages().isEmpty()) {
                JOptionIcon.showWarningDialog(null, "The report has no records to display.");
                connection.close();
                return;
            }

            // Display the report
            displayReport(jasperPrint, title != null ? title : reportName);

            connection.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(null, "Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generate and export a report to PDF
     * @param reportName Name of the report file (without .jasper or .jrxml extension)
     * @param parameters Map of parameters to pass to the report
     * @param outputFile Output file path for the PDF
     */
    public static void exportToPdf(String reportName, Map<String, Object> parameters, String outputFile) {
        try {
            File jasperFile = new File(REPORTS_PATH + reportName + ".jasper");
            File jrxmlFile = new File(REPORTS_PATH + reportName + ".jrxml");

            JasperReport jasperReport;
            if (jasperFile.exists()) {
                jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
            } else if (jrxmlFile.exists()) {
                try {
                    jasperReport = JasperCompileManager.compileReport(jrxmlFile.getAbsolutePath());
                } catch (Exception e) {
                    System.err.println("Error compiling report: " + e.getMessage());
                    System.err.println("File: " + jrxmlFile.getAbsolutePath());
                    System.err.println("Full stack trace:");
                    e.printStackTrace();
                    
                    JOptionIcon.showErrorDialog(null, "Error compiling report. Check console for details.\nFile: " + jrxmlFile.getAbsolutePath());
                    return;
                }
            } else {
                JOptionIcon.showErrorDialog(null, "Report template not found: " + reportName);
                return;
            }

            // Get database connection from MySQL class
            Connection connection = MySQL.getConnection();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // Export to PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);

            connection.close();

            JOptionIcon.showSuccessDialog(null, "Report exported to PDF:\n" + outputFile);

            // Open the PDF file
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(outputFile));
            }

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(null, "Error exporting report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Display a JasperPrint in a viewer window
     * @param jasperPrint The JasperPrint object to display
     * @param title Title for the viewer window
     */
    private static void displayReport(JasperPrint jasperPrint, String title) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            JRViewer viewer = new JRViewer(jasperPrint);
            frame.add(viewer, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }

    private static String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Compile all JRXML files to JASPER files
     * Call this method after designing new reports in Jaspersoft Studio
     */
    public static void compileAllReports() {
        File reportsDir = new File(REPORTS_PATH);
        if (!reportsDir.exists()) {
            JOptionIcon.showErrorDialog(null, "Reports directory not found: " + REPORTS_PATH);
            return;
        }

        File[] jrxmlFiles = reportsDir.listFiles((dir, name) -> name.endsWith(".jrxml"));
        if (jrxmlFiles == null || jrxmlFiles.length == 0) {
            JOptionIcon.showErrorDialog(null, "No JRXML files found in reports directory");
            return;
        }

        int successCount = 0;
        int failCount = 0;

        for (File jrxmlFile : jrxmlFiles) {
            try {
                String jasperFileName = jrxmlFile.getName().replace(".jrxml", ".jasper");
                File jasperFile = new File(reportsDir, jasperFileName);

                JasperCompileManager.compileReportToFile(jrxmlFile.getAbsolutePath(), jasperFile.getAbsolutePath());
                successCount++;

            } catch (Exception e) {
                failCount++;
                System.err.println("Error compiling " + jrxmlFile.getName() + ": " + e.getMessage());
            }
        }

        JOptionIcon.showSuccessDialog(null,
            "Report compilation complete:\n" +
            "Success: " + successCount + "\n" +
            "Failed: " + failCount
        );
    }
}
