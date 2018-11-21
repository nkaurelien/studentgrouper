/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import entities.Groupe;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author nkaurelien
 */
public class XGroupeJpaController extends GroupeJpaController{

    public XGroupeJpaController(EntityManagerFactory emf) {
        super(emf);
    }
    
    
    public Groupe findByCode(String code) {
        EntityManager em = getEntityManager();
        Query query;
        Groupe g;
        try {
            query = em.createNamedQuery("Groupe.findByCode");
            query.setParameter("code", code);
            g = (Groupe) query.getResultList().get(0);

            return g;
        } catch (Exception e) {
            System.out.println( e.getMessage());
            //e.printStackTrace();
            return null;
        }
    }
}
