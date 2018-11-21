/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package studentgrouper;

import Outils.AudioPlayer;
import Outils.Linker;
import gui.MainWindows;
import java.awt.Frame;

/**
 *
 * @author Oracle
 */
public class StudentGrouper{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("lancement de l application");
        MainWindows m = new MainWindows();
        //m.setTitle("StudentGrouper : createur de groupe d'exposée");
        //m.setSize(600, 500);
        m.setVisible(true);
        
    }
    
}
