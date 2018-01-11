package authorize;

import com.dropbox.client2.exception.DropboxException;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.json.JsonReader;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guilherme
 */
public class AuthAndUpload {

    private static final String APP_NAME = "apseeUploader";
    private String accessToken;
    private String userId;
    private final String DATA_INFO_PATH = "src/resrc/test.app";
    private DbxAppInfo appInfo;
    
    private static String OS = null;
    public static String getOsName() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

    private static String createFolderIfNotExists(String path) {
        java.io.File file = new java.io.File(path);
        if (!file.exists()) 
            file.mkdirs();
        return path;
    }

    public static boolean isWindows() {
        return getOsName().toLowerCase().startsWith("windows");
    }

    private static String getStorageFullPath() {
        if (isWindows()) 
            return System.getProperty("user.home") + "\\.credentials\\" + APP_NAME + "\\";
        else 
            return System.getProperty("user.home") + "/.credentials/" + APP_NAME + "/";
    }
    
    private void getAuth(){   
        try {
            appInfo = DbxAppInfo.Reader.readFromFile(DATA_INFO_PATH);
        } catch (JsonReader.FileLoadException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        DbxRequestConfig requestConfig = new DbxRequestConfig("examples-authorize");
        DbxWebAuth webAuth = new DbxWebAuth(requestConfig, appInfo);
        DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder()
                .withNoRedirect()
                .build();

        String authorizeUrl = webAuth.authorize(webAuthRequest);
        try {
            Desktop.getDesktop().browse(new URI(authorizeUrl));
        } catch (URISyntaxException | IOException ex) { 
            ex.printStackTrace();
        }
       
        try {
            System.out.println("Copie aqui o seu código: ");
            String code = new BufferedReader(new InputStreamReader(System.in)).readLine();
            code = code.trim();
            DbxAuthFinish authFinish = webAuth.finishFromCode(code);
            accessToken = authFinish.getAccessToken();
            userId = authFinish.getUserId();
        } catch (IOException | DbxException ex) {
            System.err.println(ex.getMessage());
            System.exit(1); 
        }
    }
      
    private void saveAuthDataToFile(String path){
        DbxAuthInfo authInfo = new DbxAuthInfo(accessToken, appInfo.getHost());
        File output = new File(path);
        output.setWritable(Boolean.TRUE, Boolean.TRUE);
        try {
            DbxAuthInfo.Writer.writeToFile(authInfo, output);
            System.out.println("Credenciais salvas em \"" + output.getAbsolutePath() + "\".");
        } catch (IOException ex) {
            try {
                System.err.println("Não pôde salvar arquivo. Lançando em stderr:");
                DbxAuthInfo.Writer.writeToStream(authInfo, System.err);
                System.err.println();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    private static final String STORAGE_PATH = createFolderIfNotExists(getStorageFullPath());
    public void run() {
        getAuth();
        saveAuthDataToFile(STORAGE_PATH + "credential.auth");
    }
}
