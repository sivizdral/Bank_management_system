/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Poruke;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Ivan
 */
public class TransakcijaPoruka implements Serializable {
    
    private Integer idTra;
    private Date DatumVreme;
    private String vrsta;
    private Integer iznos;
    private Integer redniBrKa;
    private Integer redniBrSa;
    private String svrha;
    private Integer idRacKa;
    private Integer idRacSa;
    private Integer idFil;

    public Integer getIdTra() {
        return idTra;
    }

    public void setIdTra(Integer idTra) {
        this.idTra = idTra;
    }

    public Date getDatumVreme() {
        return DatumVreme;
    }

    public void setDatumVreme(Date DatumVreme) {
        this.DatumVreme = DatumVreme;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public Integer getIznos() {
        return iznos;
    }

    public void setIznos(Integer iznos) {
        this.iznos = iznos;
    }

    public Integer getRedniBrKa() {
        return redniBrKa;
    }

    public void setRedniBrKa(Integer redniBrKa) {
        this.redniBrKa = redniBrKa;
    }

    public Integer getRedniBrSa() {
        return redniBrSa;
    }

    public void setRedniBrSa(Integer redniBrSa) {
        this.redniBrSa = redniBrSa;
    }

    public String getSvrha() {
        return svrha;
    }

    public void setSvrha(String svrha) {
        this.svrha = svrha;
    }

    public Integer getIdRacKa() {
        return idRacKa;
    }

    public void setIdRacKa(Integer idRacKa) {
        this.idRacKa = idRacKa;
    }

    public Integer getIdRacSa() {
        return idRacSa;
    }

    public void setIdRacSa(Integer idRacSa) {
        this.idRacSa = idRacSa;
    }

    public Integer getIdFil() {
        return idFil;
    }

    public void setIdFil(Integer idFil) {
        this.idFil = idFil;
    }
    
    
    
}
