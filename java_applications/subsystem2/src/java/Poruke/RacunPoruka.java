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
public class RacunPoruka implements Serializable {
    
    private Integer idRac;
    private Integer stanje;
    private Integer idKom;
    private Integer dozvMinus;
    private String status;
    private Date DatumVreme;
    private Integer idMes;
    private Integer brTransakcija;

    public Integer getIdRac() {
        return idRac;
    }

    public void setIdRac(Integer idRac) {
        this.idRac = idRac;
    }

    public Integer getStanje() {
        return stanje;
    }

    public void setStanje(Integer stanje) {
        this.stanje = stanje;
    }

    public Integer getIdKom() {
        return idKom;
    }

    public void setIdKom(Integer idKom) {
        this.idKom = idKom;
    }

    public Integer getDozvMinus() {
        return dozvMinus;
    }

    public void setDozvMinus(Integer dozvMinus) {
        this.dozvMinus = dozvMinus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDatumVreme() {
        return DatumVreme;
    }

    public void setDatumVreme(Date DatumVreme) {
        this.DatumVreme = DatumVreme;
    }

    public Integer getIdMes() {
        return idMes;
    }

    public void setIdMes(Integer idMes) {
        this.idMes = idMes;
    }

    public Integer getBrTransakcija() {
        return brTransakcija;
    }

    public void setBrTransakcija(Integer brTransakcija) {
        this.brTransakcija = brTransakcija;
    }
    
    
    
    
    
}
