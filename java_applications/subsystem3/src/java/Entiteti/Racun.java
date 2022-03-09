/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entiteti;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ivan
 */
@Entity
@Table(name = "racun")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Racun.findAll", query = "SELECT r FROM Racun r"),
    @NamedQuery(name = "Racun.findByIdRac", query = "SELECT r FROM Racun r WHERE r.idRac = :idRac"),
    @NamedQuery(name = "Racun.findByStanje", query = "SELECT r FROM Racun r WHERE r.stanje = :stanje"),
    @NamedQuery(name = "Racun.findByIdKom", query = "SELECT r FROM Racun r WHERE r.idKom = :idKom"),
    @NamedQuery(name = "Racun.findByDozvMinus", query = "SELECT r FROM Racun r WHERE r.dozvMinus = :dozvMinus"),
    @NamedQuery(name = "Racun.findByStatus", query = "SELECT r FROM Racun r WHERE r.status = :status"),
    @NamedQuery(name = "Racun.findByDatumVreme", query = "SELECT r FROM Racun r WHERE r.datumVreme = :datumVreme"),
    @NamedQuery(name = "Racun.findByIdMes", query = "SELECT r FROM Racun r WHERE r.idMes = :idMes"),
    @NamedQuery(name = "Racun.findByBrTransakcija", query = "SELECT r FROM Racun r WHERE r.brTransakcija = :brTransakcija")})
public class Racun implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdRac")
    private Integer idRac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Stanje")
    private int stanje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKom")
    private int idKom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DozvMinus")
    private int dozvMinus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "Status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumVreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdMes")
    private int idMes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BrTransakcija")
    private int brTransakcija;

    public Racun() {
    }

    public Racun(Integer idRac) {
        this.idRac = idRac;
    }

    public Racun(Integer idRac, int stanje, int idKom, int dozvMinus, String status, Date datumVreme, int idMes, int brTransakcija) {
        this.idRac = idRac;
        this.stanje = stanje;
        this.idKom = idKom;
        this.dozvMinus = dozvMinus;
        this.status = status;
        this.datumVreme = datumVreme;
        this.idMes = idMes;
        this.brTransakcija = brTransakcija;
    }

    public Integer getIdRac() {
        return idRac;
    }

    public void setIdRac(Integer idRac) {
        this.idRac = idRac;
    }

    public int getStanje() {
        return stanje;
    }

    public void setStanje(int stanje) {
        this.stanje = stanje;
    }

    public int getIdKom() {
        return idKom;
    }

    public void setIdKom(int idKom) {
        this.idKom = idKom;
    }

    public int getDozvMinus() {
        return dozvMinus;
    }

    public void setDozvMinus(int dozvMinus) {
        this.dozvMinus = dozvMinus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Date datumVreme) {
        this.datumVreme = datumVreme;
    }

    public int getIdMes() {
        return idMes;
    }

    public void setIdMes(int idMes) {
        this.idMes = idMes;
    }

    public int getBrTransakcija() {
        return brTransakcija;
    }

    public void setBrTransakcija(int brTransakcija) {
        this.brTransakcija = brTransakcija;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRac != null ? idRac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Racun)) {
            return false;
        }
        Racun other = (Racun) object;
        if ((this.idRac == null && other.idRac != null) || (this.idRac != null && !this.idRac.equals(other.idRac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entiteti.Racun[ idRac=" + idRac + " ]";
    }
    
}
