/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oracle
 */
@Entity
@Table(name = "groupe", catalog = "etudiant", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"code"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Groupe.findAll", query = "SELECT g FROM Groupe g"),
    @NamedQuery(name = "Groupe.findById", query = "SELECT g FROM Groupe g WHERE g.id = :id"),
    @NamedQuery(name = "Groupe.findByCode", query = "SELECT g FROM Groupe g WHERE g.code = :code"),
    @NamedQuery(name = "Groupe.findByCodeAndEnabled", query = "SELECT g FROM Groupe g WHERE g.code = :code and g.etat = true"),
    @NamedQuery(name = "Groupe.findByCodeAndDisabled", query = "SELECT g FROM Groupe g WHERE g.code = :code and g.etat = false"),
    @NamedQuery(name = "Groupe.findByLibelle", query = "SELECT g FROM Groupe g WHERE g.libelle = :libelle"),
    @NamedQuery(name = "Groupe.findByEtat", query = "SELECT g FROM Groupe g WHERE g.etat = :etat")})

public class Groupe implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "code", length = 10)
    private String code;
    @Column(name = "libelle", length = 100)
    private String libelle;
    @Basic(optional = false)
    @Column(name = "etat", nullable = false)
    private boolean etat;
    @OneToMany(mappedBy = "groupe", fetch = FetchType.EAGER)
    private List<Etudiant> etudiantList;

    public Groupe() {
    }

    public Groupe(Integer id) {
        this.id = id;
    }

    public Groupe(Integer id, boolean etat) {
        this.id = id;
        this.etat = etat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean getEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    @XmlTransient
    public List<Etudiant> getEtudiantList() {
        return etudiantList;
    }

    public void setEtudiantList(List<Etudiant> etudiantList) {
        this.etudiantList = etudiantList;
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
        if (!(object instanceof Groupe)) {
            return false;
        }
        Groupe other = (Groupe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
           //return "entities.Groupe[ id=" + id + " ]";
           return code + " - "+ libelle ;

    }
    
    
}
