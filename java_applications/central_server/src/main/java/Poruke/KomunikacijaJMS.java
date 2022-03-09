/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Poruke;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ivan
 */
public class KomunikacijaJMS {
    
    static JMSContext context = null;
    
    private static KomunikacijaJMS komunikacija = null;
    
    protected KomunikacijaJMS() {
        
    }
    
    public static KomunikacijaJMS dohvPrimerak() {
        if (komunikacija == null) {
            komunikacija = new KomunikacijaJMS();
        }
        return komunikacija;
    }

    public JMSContext getContext(ConnectionFactory connFactory) {
        if (context == null) context = connFactory.createContext();
        return context;
    }
    
}
