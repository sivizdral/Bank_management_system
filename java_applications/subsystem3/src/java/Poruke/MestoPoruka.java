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
public class MestoPoruka implements Serializable {
    
    private Integer idMes;
    private String Naziv;
    private String PostanskiBroj;

    public Integer getIdMes() {
        return idMes;
    }

    public void setIdMes(Integer idMes) {
        this.idMes = idMes;
    }

    public String getNaziv() {
        return Naziv;
    }

    public void setNaziv(String Naziv) {
        this.Naziv = Naziv;
    }

    public String getPostanskiBroj() {
        return PostanskiBroj;
    }

    public void setPostanskiBroj(String PostanskiBroj) {
        this.PostanskiBroj = PostanskiBroj;
    }
    
}
