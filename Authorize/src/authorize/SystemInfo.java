/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authorize;

/**
 *
 * @author Guilherme
 */
public class SystemInfo {
    public static boolean isWindows() {
        return getOsName().toLowerCase().startsWith("windows");
    }
    
    private static String OS = null;
    public static String getOsName() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }
}
