/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StraniEntiteti;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
    @NamedQuery(name = "Racun.findByDozvMinus", query = "SELECT r FROM Racun r WHERE r.dozvMinus = :dozvMinus"),
    @NamedQuery(name = "Racun.findByStatus", query = "SELECT r FROM Racun r WHERE r.status = :status"),
    @NamedQuery(name = "Racun.findByDatumVreme", query = "SELECT r FROM Racun r WHERE r.datumVreme = :datumVreme"),
    @NamedQuery(name = "Racun.findByIdMes", query = "SELECT r FROM Racun r WHERE r.idMes = :idMes"),
    @NamedQuery(name = "Racun.findByBrTransakcija", query = "SELECT r FROM Racun r WHERE r.brTransakcija = :brTransakcija")})
public class Racun implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdRac")
    private Integer idRac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Stanje")
    private int stanje;
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
    @OneToMany(mappedBy = "idRacKa")
    private List<Transakcija> transakcijaList;
    @OneToMany(mappedBy = "idRacSa")
    private List<Transakcija> transakcijaList1;
    @JoinColumn(name = "IdKom", referencedColumnName = "IdKom")
    @ManyToOne(optional = false)
    private Komitent idKom;

    public Racun() {
    }

    public Racun(Integer idRac) {
        this.idRac = idRac;
    }

    public Racun(Integer idRac, int stanje, int dozvMinus, String status, Date datumVreme, int idMes, int brTransakcija) {
        this.idRac = idRac;
        this.stanje = stanje;
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

    @XmlTransient
    public List<Transakcija> getTransakcijaList() {
        return transakcijaList;
    }

    public void setTransakcijaList(List<Transakcija> transakcijaList) {
        this.transakcijaList = transakcijaList;
    }

    @XmlTransient
    public List<Transakcija> getTransakcijaList1() {
        return transakcijaList1;
    }

    public void setTransakcijaList1(List<Transakcija> transakcijaList1) {
        this.transakcijaList1 = transakcijaList1;
    }

    public Komitent getIdKom() {
        return idKom;
    }

    public void setIdKom(Komitent idKom) {
        this.idKom = idKom;
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
        return "entiteti.Racun[ idRac=" + idRac + " ]";
    }
    
}
