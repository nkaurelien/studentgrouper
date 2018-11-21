/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Outils;

import Controllers.Constante;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author nkaurelien
 */
public class Printer {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory(Constante.PERSISTANCE_UNIT_NAME);
    String sourceFileName = Constante.REPORT_ETUDIANTS;
    JRViewer viewer = null;
     
    public Printer() {

    }

    public JRViewer getViewer() {
        return viewer;
    }

    public void view() {
        try {
            Connection connexion = Db.getConnexion();
            //String jf = JasperFillManager.fillReportToFile(Constante.REPORT_ETUDIANTS_COMPILED, null,new JREmptyDataSource());
            JasperPrint jasperPrint = JasperFillManager.fillReport(Constante.REPORT_ETUDIANTS_COMPILED, null, connexion);
            // affichage du document
            JasperViewer.viewReport(jasperPrint, false); 
            // export en pdf
            //JasperExportManager.exportReportToPdfFile(jasperPrint, Constante.REPORT_ETUDIANTS_DESTINATION);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    
    public void viewInGroupeEtudiants(int id) {
        
        Map parameters = new HashMap();
        parameters.put("ID_DU_GROUPE", id);
        try {
            Connection connexion = Db.getConnexion();
            //String jf = JasperFillManager.fillReportToFile(Constante.REPORT_ETUDIANTS_COMPILED, null,new JREmptyDataSource());
            JasperPrint jasperPrint = JasperFillManager.fillReport(Constante.REPORT_ETUDIANTSINGROUP_COMPILED, parameters, connexion);
            // affichage du document
            JasperViewer.viewReport(jasperPrint, true); 
            // export en pdf
            //JasperExportManager.exportReportToPdfFile(jasperPrint, Constante.REPORT_ETUDIANTS_DESTINATION);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
   
    

    public void compile() {
        JasperPrint jp;
        InputStream reportStream = this.getClass().getResourceAsStream(Constante.REPORT_ETUDIANTS);
        HashMap map = new HashMap();
        try {
            JasperDesign jd = JRXmlLoader.load(reportStream);
            // Compile design to JasperReport  
            JasperReport jr = JasperCompileManager.compileReport(jd);
            // Creation d'un objet JasperPrint  
            // On passe les parametres du rapport et la source de donnees  
            jp = JasperFillManager.fillReport(jr, map, Db.getConnexion());
            JasperViewer jv = new JasperViewer(jp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
