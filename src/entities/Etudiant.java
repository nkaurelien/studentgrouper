/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oracle
 */
@Entity
@Table(name = "etudiant", catalog = "etudiant", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom", "prenom", "date_naiss"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Etudiant.findAll", query = "SELECT e FROM Etudiant e"),
    @NamedQuery(name = "Etudiant.findById", query = "SELECT e FROM Etudiant e WHERE e.id = :id"),
    @NamedQuery(name = "Etudiant.findByNomAndPrenom", query = "SELECT e FROM Etudiant e WHERE e.nom = :nom and e.prenom = :prenom"),
    @NamedQuery(name = "Etudiant.findByNomAndGroupe", query = "SELECT e FROM Etudiant e WHERE e.nom = :nom and e.groupe = :groupe"),
    @NamedQuery(name = "Etudiant.findByNomAndPrenomAndGroupe", query = "SELECT e FROM Etudiant e WHERE e.nom = :nom and e.prenom = :prenom and e.groupe = :groupe"),
    @NamedQuery(name = "Etudiant.findByNom", query = "SELECT e FROM Etudiant e WHERE e.nom = :nom"),
    @NamedQuery(name = "Etudiant.findByPrenom", query = "SELECT e FROM Etudiant e WHERE e.prenom = :prenom"),
    @NamedQuery(name = "Etudiant.findBySexe", query = "SELECT e FROM Etudiant e WHERE e.sexe = :sexe"),
    @NamedQuery(name = "Etudiant.findByDateNaiss", query = "SELECT e FROM Etudiant e WHERE e.dateNaiss = :dateNaiss")})
public class Etudiant implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "nom", length = 50)
    private String nom;
    @Column(name = "prenom", length = 50)
    private String prenom;
    @Basic(optional = false)
    @Column(name = "sexe", nullable = false, length = 1)
    private String sexe;
    @Basic(optional = false)
    @Column(name = "date_naiss", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateNaiss;
    @JoinColumn(name = "groupe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Groupe groupe;

    public Etudiant() {
    }

    public Etudiant(Integer id) {
        this.id = id;
    }

    public Etudiant(Integer id, String sexe, Date dateNaiss) {
        this.id = id;
        this.sexe = sexe;
        this.dateNaiss = dateNaiss;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Date getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(Date dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Etudiant)) {
            return false;
        }
        Etudiant other = (Etudiant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Etudiant[ id=" + id + " ]";
    }
    
}
