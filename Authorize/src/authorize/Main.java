package authorize;

import com.dropbox.client2.exception.DropboxException;
import java.io.File;


/**
 * An example command-line application that runs through the web-based OAuth
 * flow (using {@link DbxWebAuth}).
 */
public class Main {
    static File authFile = null;
    public static void main(String[] args) throws DropboxException {
        //Unir referências de arquivo
        Auth a = new Auth();
        Uploader u = new Uploader();
        a.run();
        u.run();
    }
}
