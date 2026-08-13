package lk.corevision.fruitshop.validate;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import lk.corevision.fruitshop.gui.SignIn;

/**
 * Utility class for managing JOptionPane icons. Provides consistent icon
 * styling for dialog boxes and notifications.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public enum JOptionIcon {

    SUCCESS {
        @Override
        public Icon setICon() {
            try {
                InputStream imgStream = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/icons8-success-48.png");
                return new ImageIcon(ImageIO.read(imgStream));
            } catch (IOException e) {
                SignIn.getLoggerObject().log(Level.WARNING, "success image loading Exception found!", e);
            }
            return null;
        }

    }, WARNING {
        @Override
        public Icon setICon() {
            try {
                InputStream imgStream = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/icons8-warning-48.png");
                return new ImageIcon(ImageIO.read(imgStream));
            } catch (IOException e) {
                SignIn.getLoggerObject().log(Level.WARNING, "warning image loading Exception found!", e);
            }
            return null;
        }

    }, INFO {
        @Override
        public Icon setICon() {
            try {
                InputStream imgStream = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/icons8-info-48.png");
                return new ImageIcon(ImageIO.read(imgStream));
            } catch (IOException e) {
                SignIn.getLoggerObject().log(Level.WARNING, "info image loading Exception found!", e);
            }
            return null;
        }

    }, ERROR {
        @Override
        public Icon setICon() {
            try {
                InputStream imgStream = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/icons8-error-48.png");
                return new ImageIcon(ImageIO.read(imgStream));
            } catch (IOException e) {
                SignIn.getLoggerObject().log(Level.WARNING, "error image loading Exception found!", e);
            }
            return null;
        }
    };

    public Icon setICon() {
        return null;
    }
}
