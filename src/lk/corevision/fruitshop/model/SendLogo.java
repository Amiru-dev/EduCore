package lk.corevision.fruitshop.model;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Utility class for accessing application logo resources. Provides methods to
 * load the application logo for use in emails and reports.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class SendLogo {
//    private static String filePath="https://i.ibb.co/QPMJHHT/logo.png";

    private static final String FILEPATH = "/lk/corevision/fruitshop/resources/logo.png";

    /**
     * Gets the input stream for the logo file resource.
     *
     * @return InputStream containing the logo image data
     * @throws java.io.UnsupportedEncodingException
     */
    public static InputStream getFilePath() throws UnsupportedEncodingException {
        return SendLogo.class.getResourceAsStream(FILEPATH);
    }

}
