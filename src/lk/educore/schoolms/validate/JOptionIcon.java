package lk.educore.schoolms.validate;

import lk.educore.schoolms.model.SendLogo;
import javax.swing.*;
import java.awt.*;

/**
 * JOptionIcon
 * Provides custom icons for JOptionPane dialogs
 */
public class JOptionIcon {

    /**
     * Show error message dialog with custom icon
     */
    public static void showErrorDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE, 
            SendLogo.getErrorIcon());
    }

    /**
     * Show warning message dialog with custom icon
     */
    public static void showWarningDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, 
            message, 
            "Warning", 
            JOptionPane.WARNING_MESSAGE, 
            SendLogo.getWarningIcon());
    }

    /**
     * Show information message dialog with custom icon
     */
    public static void showInfoDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, 
            message, 
            "Information", 
            JOptionPane.INFORMATION_MESSAGE, 
            SendLogo.getInfoIcon());
    }

    /**
     * Show success message dialog with custom icon
     */
    public static void showSuccessDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, 
            message, 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE, 
            SendLogo.getSuccessIcon());
    }

    /**
     * Show confirmation dialog with custom icon
     */
    public static int showConfirmDialog(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, 
            message, 
            "Confirm", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            SendLogo.getInfoIcon());
    }

    /**
     * Show confirmation dialog with custom title and icon
     */
    public static int showConfirmDialog(Component parent, String message, String title) {
        return JOptionPane.showConfirmDialog(parent, 
            message, 
            title, 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            SendLogo.getInfoIcon());
    }

    /**
     * Show input dialog with custom icon
     */
    public static String showInputDialog(Component parent, String message) {
        Object result = JOptionPane.showInputDialog(parent,
            message,
            "Input",
            JOptionPane.QUESTION_MESSAGE,
            SendLogo.getInfoIcon(),
            null,
            null);
        return result != null ? result.toString() : null;
    }

    /**
     * Show input dialog with custom icon and initial value
     */
    public static String showInputDialog(Component parent, String message, String initialValue) {
        Object result = JOptionPane.showInputDialog(parent,
            message,
            "Input",
            JOptionPane.QUESTION_MESSAGE,
            SendLogo.getInfoIcon(),
            null,
            initialValue);
        return result != null ? result.toString() : null;
    }

    /**
     * Show option dialog with custom icon
     */
    public static int showOptionDialog(Component parent, String message, String title, Object[] options) {
        return JOptionPane.showOptionDialog(parent, 
            message, 
            title, 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            SendLogo.getInfoIcon(), 
            options, 
            options[0]);
    }

    /**
     * Show custom message dialog with specified icon
     */
    public static void showMessageDialog(Component parent, String message, String title, int messageType, Icon icon) {
        JOptionPane.showMessageDialog(parent, message, title, messageType, icon);
    }
}