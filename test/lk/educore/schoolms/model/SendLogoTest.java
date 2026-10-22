package lk.educore.schoolms.model;

import org.junit.Test;

import javax.swing.ImageIcon;

import static org.junit.Assert.*;

/**
 * Unit tests for SendLogo resource and image loading utility.
 * Verifies logo retrieval, status icons, navigation scaling, and null safety.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class SendLogoTest {

    @Test
    public void testGetLogo() {
        ImageIcon logo = SendLogo.getLogo();
        assertNotNull("Main logo must be loaded from resources", logo);
        assertTrue("Logo width must be positive", logo.getIconWidth() > 0);
        assertTrue("Logo height must be positive", logo.getIconHeight() > 0);
    }

    @Test
    public void testGetSchoolLogo() {
        ImageIcon schoolLogo = SendLogo.getSchoolLogo();
        // school-logo.png is optional; if present, verify dimensions, otherwise safely null
        if (schoolLogo != null) {
            assertTrue("School logo width must be positive", schoolLogo.getIconWidth() > 0);
        }
    }

    @Test
    public void testStatusIcons() {
        ImageIcon success = SendLogo.getSuccessIcon();
        assertNotNull("Success icon should be loaded", success);
        assertTrue(success.getIconWidth() > 0);

        ImageIcon warning = SendLogo.getWarningIcon();
        assertNotNull("Warning icon should be loaded", warning);
        assertTrue(warning.getIconWidth() > 0);

        ImageIcon error = SendLogo.getErrorIcon();
        assertNotNull("Error icon should be loaded", error);
        assertTrue(error.getIconWidth() > 0);

        ImageIcon info = SendLogo.getInfoIcon();
        assertNotNull("Info icon should be loaded", info);
        assertTrue(info.getIconWidth() > 0);
    }

    @Test
    public void testGetProfileIcon() {
        ImageIcon profile = SendLogo.getProfileIcon();
        assertNotNull("Profile icon should be loaded", profile);
        assertTrue(profile.getIconWidth() > 0);
    }

    @Test
    public void testGetNavIcon() {
        // Nav icon should be scaled to 20x20
        ImageIcon navIcon = SendLogo.getNavIcon("student.png");
        assertNotNull("Nav icon student.png should be loaded", navIcon);
        assertEquals("Nav icon should have width 20", 20, navIcon.getIconWidth());
        assertEquals("Nav icon should have height 20", 20, navIcon.getIconHeight());
    }

    @Test
    public void testScaleIcon() {
        ImageIcon original = SendLogo.getLogo();
        assertNotNull(original);

        ImageIcon scaled = SendLogo.scaleIcon(original, 48, 48);
        assertNotNull(scaled);
        assertEquals(48, scaled.getIconWidth());
        assertEquals(48, scaled.getIconHeight());

        // Null safety
        assertNull(SendLogo.scaleIcon(null, 48, 48));
    }
}
