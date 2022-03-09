/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Poruke;

import java.io.Serializable;

/**
 *
 * @author Ivan
 */
public class KomitentPoruka implements Serializable {
    
    private Integer idKom;
    private String naziv;
    private String adresa;
    private Integer idMes;

    public Integer getIdKom() {
        return idKom;
    }

    public void setIdKom(Integer idKom) {
        this.idKom = idKom;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Integer getIdMes() {
        return idMes;
    }

    public void setIdMes(Integer idMes) {
        this.idMes = idMes;
    }
    
    
    
}
