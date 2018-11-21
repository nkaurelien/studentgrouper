/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import entities.Etudiant;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author nkaurelien
 */
public class XEtudiantJpaController extends EtudiantJpaController{

    public XEtudiantJpaController(EntityManagerFactory emf) {
        super(emf);
    }
    
    
    public Etudiant findByNomAndPrenom(Etudiant et) {
        EntityManager em = getEntityManager();
        Query query;
        Etudiant e;
        try {
            query = em.createNamedQuery("Etudiant.findByNomAndPrenom");
            query.setParameter("nom", et.getNom());
            query.setParameter("prenom", et.getPrenom());
            e = (Etudiant) query.getResultList().get(0);

            return e;
        } catch (Exception ex) {
            System.out.println( ex.getMessage());
            //e.printStackTrace();
            return null;
        }
    }
    
    public Etudiant findByNameAndGroupe(Etudiant et) {
        EntityManager em = getEntityManager();
        Query query;
        Etudiant e;
        try {
            query = em.createNamedQuery("Etudiant.findByNomAndGroupe");
            query.setParameter("nom", et.getNom());
            query.setParameter("groupe", et.getGroupe());
            e = (Etudiant) query.getResultList().get(0);
            System.out.println("etudiant trouve"+ e);
            return e;
        } catch (Exception ex) {
            System.out.println( ex.getMessage());
            //e.printStackTrace();
            return null;
        }
    }
    
     public Etudiant findByNameAndPrenomAndGroupe(Etudiant et) {
        EntityManager em = getEntityManager();
        Query query;
        Etudiant e;
        try {
            query = em.createNamedQuery("Etudiant.findByNomAndPrenomAndGroupe");
            query.setParameter("nom", et.getNom());
            query.setParameter("prenom", et.getPrenom());
            query.setParameter("groupe", et.getGroupe());
            e = (Etudiant) query.getResultList().get(0);
            System.out.println("etudiant trouve"+ e);
            return e;
        } catch (Exception ex) {
            System.out.println( ex.getMessage());
            //e.printStackTrace();
            return null;
        }
    }
    
}
