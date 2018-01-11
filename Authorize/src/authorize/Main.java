package authorize;

import com.dropbox.client2.exception.DropboxException;


/**
 * An example command-line application that runs through the web-based OAuth
 * flow (using {@link DbxWebAuth}).
 */
public class Main {
    public static void main(String[] args) throws DropboxException {
        AuthAndUpload a = new AuthAndUpload();
        a.run();
    }
}
