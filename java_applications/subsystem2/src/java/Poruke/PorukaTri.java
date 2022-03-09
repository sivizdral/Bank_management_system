
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
public class PorukaTri implements Serializable {
    
    private List komitenti = null;
    private List filijale = null;
    private List mesta = null;

    public List getKomitenti() {
        return komitenti;
    }

    public void setKomitenti(List komitenti) {
        this.komitenti = komitenti;
    }

    public List getFilijale() {
        return filijale;
    }

    public void setFilijale(List filijale) {
        this.filijale = filijale;
    }

    public List getMesta() {
        return mesta;
    }

    public void setMesta(List mesta) {
        this.mesta = mesta;
    }
    
    
    
}
