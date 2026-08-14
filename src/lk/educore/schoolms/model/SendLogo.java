package lk.educore.schoolms.model;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

/**
 * Logo Resource Loader
 * Loads and provides access to application logo and icon resources
 */
public class SendLogo {

    /**
     * Load the main application logo
     */
    public static ImageIcon getLogo() {
        try {
            InputStream is = SendLogo.class.getResourceAsStream("/resources/logo.png");
            if (is != null) {
                byte[] imageData = new byte[is.available()];
                is.read(imageData);
                return new ImageIcon(imageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load the school logo
     */
    public static ImageIcon getSchoolLogo() {
        try {
            InputStream is = SendLogo.class.getResourceAsStream("/resources/school-logo.png");
            if (is != null) {
                byte[] imageData = new byte[is.available()];
                is.read(imageData);
                return new ImageIcon(imageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load success icon
     */
    public static ImageIcon getSuccessIcon() {
        try {
            InputStream is = SendLogo.class.getResourceAsStream("/resources/icons8-success-48.png");
            if (is != null) {
                byte[] imageData = new byte[is.available()];
                is.read(imageData);
                return new ImageIcon(imageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load warning icon
     */
    public static ImageIcon getWarningIcon() {
        try {
            InputStream is = SendLogo.class.getResourceAsStream("/resources/icons8-warning-48.png");
            if (is != null) {
                byte[] imageData = new byte[is.available()];
                is.read(imageData);
                return new ImageIcon(imageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load error icon
     */
    public static ImageIcon getErrorIcon() {
        try {
            InputStream is = SendLogo.class.getResourceAsStream("/resources/icons8-error-48.png");
            if (is != null) {
                byte[] imageData = new byte[is.available()];
                is.read(imageData);
                return new ImageIcon(imageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load info icon
     */
    public static ImageIcon getInfoIcon() {
        try {
            InputStream is = SendLogo.class.getResourceAsStream("/resources/icons8-info-48.png");
            if (is != null) {
                byte[] imageData = new byte[is.available()];
                is.read(imageData);
                return new ImageIcon(imageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load profile icon
     */
    public static ImageIcon getProfileIcon() {
        try {
            InputStream is = SendLogo.class.getResourceAsStream("/resources/other/profileIcon.png");
            if (is != null) {
                byte[] imageData = new byte[is.available()];
                is.read(imageData);
                return new ImageIcon(imageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load navigation icon by name
     */
    public static ImageIcon getNavIcon(String iconName) {
        try {
            InputStream is = SendLogo.class.getResourceAsStream("/resources/other/" + iconName);
            if (is != null) {
                byte[] imageData = new byte[is.available()];
                is.read(imageData);
                ImageIcon icon = new ImageIcon(imageData);
                // Scale to 20x20 for navigation
                Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Scale icon to specified dimensions
     */
    public static ImageIcon scaleIcon(ImageIcon icon, int width, int height) {
        if (icon != null) {
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }
}