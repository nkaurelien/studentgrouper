/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Etudiant;
import entities.Groupe;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Oracle
 */
public class GroupeJpaController implements Serializable {

    public GroupeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Groupe groupe) {
        if (groupe.getEtudiantList() == null) {
            groupe.setEtudiantList(new ArrayList<Etudiant>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Etudiant> attachedEtudiantList = new ArrayList<Etudiant>();
            for (Etudiant etudiantListEtudiantToAttach : groupe.getEtudiantList()) {
                etudiantListEtudiantToAttach = em.getReference(etudiantListEtudiantToAttach.getClass(), etudiantListEtudiantToAttach.getId());
                attachedEtudiantList.add(etudiantListEtudiantToAttach);
            }
            groupe.setEtudiantList(attachedEtudiantList);
            em.persist(groupe);
            for (Etudiant etudiantListEtudiant : groupe.getEtudiantList()) {
                Groupe oldGroupeOfEtudiantListEtudiant = etudiantListEtudiant.getGroupe();
                etudiantListEtudiant.setGroupe(groupe);
                etudiantListEtudiant = em.merge(etudiantListEtudiant);
                if (oldGroupeOfEtudiantListEtudiant != null) {
                    oldGroupeOfEtudiantListEtudiant.getEtudiantList().remove(etudiantListEtudiant);
                    oldGroupeOfEtudiantListEtudiant = em.merge(oldGroupeOfEtudiantListEtudiant);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Groupe groupe) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Groupe persistentGroupe = em.find(Groupe.class, groupe.getId());
            List<Etudiant> etudiantListOld = persistentGroupe.getEtudiantList();
            List<Etudiant> etudiantListNew = groupe.getEtudiantList();
            List<Etudiant> attachedEtudiantListNew = new ArrayList<Etudiant>();
            for (Etudiant etudiantListNewEtudiantToAttach : etudiantListNew) {
                etudiantListNewEtudiantToAttach = em.getReference(etudiantListNewEtudiantToAttach.getClass(), etudiantListNewEtudiantToAttach.getId());
                attachedEtudiantListNew.add(etudiantListNewEtudiantToAttach);
            }
            etudiantListNew = attachedEtudiantListNew;
            groupe.setEtudiantList(etudiantListNew);
            groupe = em.merge(groupe);
            for (Etudiant etudiantListOldEtudiant : etudiantListOld) {
                if (!etudiantListNew.contains(etudiantListOldEtudiant)) {
                    etudiantListOldEtudiant.setGroupe(null);
                    etudiantListOldEtudiant = em.merge(etudiantListOldEtudiant);
                }
            }
            for (Etudiant etudiantListNewEtudiant : etudiantListNew) {
                if (!etudiantListOld.contains(etudiantListNewEtudiant)) {
                    Groupe oldGroupeOfEtudiantListNewEtudiant = etudiantListNewEtudiant.getGroupe();
                    etudiantListNewEtudiant.setGroupe(groupe);
                    etudiantListNewEtudiant = em.merge(etudiantListNewEtudiant);
                    if (oldGroupeOfEtudiantListNewEtudiant != null && !oldGroupeOfEtudiantListNewEtudiant.equals(groupe)) {
                        oldGroupeOfEtudiantListNewEtudiant.getEtudiantList().remove(etudiantListNewEtudiant);
                        oldGroupeOfEtudiantListNewEtudiant = em.merge(oldGroupeOfEtudiantListNewEtudiant);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = groupe.getId();
                if (findGroupe(id) == null) {
                    throw new NonexistentEntityException("The groupe with id " + id + " no longer exists.");
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
            Groupe groupe;
            try {
                groupe = em.getReference(Groupe.class, id);
                groupe.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The groupe with id " + id + " no longer exists.", enfe);
            }
            List<Etudiant> etudiantList = groupe.getEtudiantList();
            for (Etudiant etudiantListEtudiant : etudiantList) {
                etudiantListEtudiant.setGroupe(null);
                etudiantListEtudiant = em.merge(etudiantListEtudiant);
            }
            em.remove(groupe);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Groupe> findGroupeEntities() {
        return findGroupeEntities(true, -1, -1);
    }

    public List<Groupe> findGroupeEntities(int maxResults, int firstResult) {
        return findGroupeEntities(false, maxResults, firstResult);
    }

    private List<Groupe> findGroupeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Groupe.class));
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

    public Groupe findGroupe(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Groupe.class, id);
        } finally {
            em.close();
        }
    }


    public int getGroupeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Groupe> rt = cq.from(Groupe.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
