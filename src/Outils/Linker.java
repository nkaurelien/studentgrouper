package Outils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
*
* @author nkumbe aurelien
* @version 1.0
* @since 2017-02-01
*/

public class Linker {
    
    public static void openUrlTODefaultBrowser(String url){
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (IOException ex) {
            Logger.getLogger(Linker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void mailTo(String email){
        if (email.isEmpty()) {
            System.out.println("Aucun email donnez, alors l'email par default sera utilisé");
        }else{
            try {
                java.awt.Desktop.getDesktop().mail(java.net.URI.create("mailto:" + email));
            } catch (IOException ex) {
                Logger.getLogger(Linker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void mailTo() {
        mailTo(Constante.AUTHOR_EMAIL);
    }
    
    /**
    * Cette methode permet de lire un fichier.
     *
     * @param filepathname chemin d'acces au fichier a lire.
     * @exception IOException On input error.
     * @see IOException
     */
    public static void openFile(String filepathname) throws IOException{
        File file = new File(filepathname);
        java.awt.Desktop.getDesktop().open(file);
    }
    
}
