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
public class PorukaDva implements Serializable {
    
    private List racuni = null;
    private List transakcije = null;

    public List getRacuni() {
        return racuni;
    }

    public void setRacuni(List racuni) {
        this.racuni = racuni;
    }

    public List getTransakcije() {
        return transakcije;
    }

    public void setTransakcije(List transakcije) {
        this.transakcije = transakcije;
    }
    
    
}
