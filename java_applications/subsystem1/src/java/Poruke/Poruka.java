/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Poruke;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Ivan
 */
public class Poruka implements Serializable {
    
    private String poruka = null;
    private List lista = null;

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public List getLista() {
        return lista;
    }

    public void setLista(List lista) {
        this.lista = lista;
    }
    
}
