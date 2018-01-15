package authorize;

import com.dropbox.client2.exception.DropboxException;


/**
 * An example command-line application that runs through the web-based OAuth
 * flow (using {@link DbxWebAuth}).
 */
public class Main {
    public static void main(String[] args) throws DropboxException {
        //Unir referências de arquivo
        Auth a = new Auth();
        Uploader u = new Uploader();
        a.run();
        u.run();
    }
}
