/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JpaControllers;

import Controllers.exceptions.NonexistentEntityException;
import entities.Etudiant;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Groupe;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Oracle
 */
public class EtudiantJpaController implements Serializable {

    public EtudiantJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Etudiant etudiant) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Groupe groupe = etudiant.getGroupe();
            if (groupe != null) {
                groupe = em.getReference(groupe.getClass(), groupe.getId());
                etudiant.setGroupe(groupe);
            }
            em.persist(etudiant);
            if (groupe != null) {
                groupe.getEtudiantList().add(etudiant);
                groupe = em.merge(groupe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Etudiant etudiant) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Etudiant persistentEtudiant = em.find(Etudiant.class, etudiant.getId());
            Groupe groupeOld = persistentEtudiant.getGroupe();
            Groupe groupeNew = etudiant.getGroupe();
            if (groupeNew != null) {
                groupeNew = em.getReference(groupeNew.getClass(), groupeNew.getId());
                etudiant.setGroupe(groupeNew);
            }
            etudiant = em.merge(etudiant);
            if (groupeOld != null && !groupeOld.equals(groupeNew)) {
                groupeOld.getEtudiantList().remove(etudiant);
                groupeOld = em.merge(groupeOld);
            }
            if (groupeNew != null && !groupeNew.equals(groupeOld)) {
                groupeNew.getEtudiantList().add(etudiant);
                groupeNew = em.merge(groupeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = etudiant.getId();
                if (findEtudiant(id) == null) {
                    throw new NonexistentEntityException("The etudiant with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Etudiant etudiant;
            try {
                etudiant = em.getReference(Etudiant.class, id);
                etudiant.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The etudiant with id " + id + " no longer exists.", enfe);
            }
            Groupe groupe = etudiant.getGroupe();
            if (groupe != null) {
                groupe.getEtudiantList().remove(etudiant);
                groupe = em.merge(groupe);
            }
            em.remove(etudiant);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Etudiant> findEtudiantEntities() {
        return findEtudiantEntities(true, -1, -1);
    }

    public List<Etudiant> findEtudiantEntities(int maxResults, int firstResult) {
        return findEtudiantEntities(false, maxResults, firstResult);
    }

    private List<Etudiant> findEtudiantEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Etudiant.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Etudiant findEtudiant(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Etudiant.class, id);
        } finally {
            em.close();
        }
    }

    public int getEtudiantCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Etudiant> rt = cq.from(Etudiant.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
