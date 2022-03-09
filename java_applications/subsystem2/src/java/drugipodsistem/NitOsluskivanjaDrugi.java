/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drugipodsistem;

import Poruke.PorukaDva;
import Poruke.RacunPoruka;
import Poruke.TransakcijaPoruka;
import entiteti.Racun;
import entiteti.Transakcija;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ivan
 */
public class NitOsluskivanjaDrugi extends Thread {
    
    static EntityManagerFactory emf;
    static EntityManager em;
    static ConnectionFactory cf;
    static Queue queueP2P3;
    static Queue queueP3P2;
    
    public NitOsluskivanjaDrugi(EntityManagerFactory emf, EntityManager em, ConnectionFactory cf, Queue qP2P3, Queue qP3P2) {
        this.emf = emf;
        this.em = em;
        this.cf = cf;
        queueP2P3 = qP2P3;
        queueP3P2 = qP3P2;
    }
    
    @Override
    public void run() {
        
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueP3P2);
        
        while (true) {
            
            try {
                ObjectMessage msg = (ObjectMessage) consumer.receive();
                
                List<Racun> racuni = em.createNamedQuery("Racun.findAll", Racun.class).getResultList();
                List<RacunPoruka> listaracuna = new ArrayList<>();
                
                int size = racuni.size();
                ObjectMessage odg1 = context.createObjectMessage();
                odg1.setIntProperty("velicina", size);
                producer.send(queueP2P3, odg1);
                
                for(Racun r : racuni) {
                    RacunPoruka rp = new RacunPoruka();
                    rp.setIdRac(r.getIdRac());
                    rp.setIdMes(r.getIdMes());
                    rp.setIdKom(r.getIdKom().getIdKom());
                    rp.setStanje(r.getStanje());
                    rp.setStatus(r.getStatus());
                    rp.setBrTransakcija(r.getBrTransakcija());
                    rp.setDozvMinus(r.getDozvMinus());
                    rp.setDatumVreme(r.getDatumVreme());
                    
                    ObjectMessage odg = context.createObjectMessage(rp);
                    producer.send(queueP2P3, odg);
                }
                
                List<Transakcija> transakcije = em.createNamedQuery("Transakcija.findAll", Transakcija.class).getResultList();
                List<TransakcijaPoruka> listatransakcija = new ArrayList<>();
                
                int size2 = transakcije.size();
                ObjectMessage odg2 = context.createObjectMessage();
                odg2.setIntProperty("velicina", size2);
                producer.send(queueP2P3, odg2);
                
                for(Transakcija t : transakcije) {
                    TransakcijaPoruka tp = new TransakcijaPoruka();
                    tp.setIdTra(t.getIdTra());
                    tp.setIdFil(t.getIdFil());
                    tp.setDatumVreme(t.getDatumVreme());
                    if (t.getIdRacKa() != null) tp.setIdRacKa(t.getIdRacKa().getIdRac());
                    else tp.setIdRacKa(null);
                    if (t.getIdRacSa() != null) tp.setIdRacSa(t.getIdRacSa().getIdRac());
                    else tp.setIdRacSa(null);
                    tp.setRedniBrKa(t.getRedniBrKa());
                    tp.setRedniBrSa(t.getRedniBrSa());
                    tp.setIznos(t.getIznos());
                    tp.setSvrha(t.getSvrha());
                    tp.setVrsta(t.getVrsta());
                    
                    ObjectMessage odg = context.createObjectMessage(tp);
                    producer.send(queueP2P3, odg);
                }
                
//                PorukaDva por = new PorukaDva();
//                por.setRacuni(racuni);
//                por.setTransakcije(transakcije);
//
//                ObjectMessage odg = context.createObjectMessage();
//                odg.setObject(por);
                

                //producer.send(queueP2P3, odg);
            } catch (JMSException ex) {
                Logger.getLogger(NitOsluskivanjaDrugi.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            
        }
        
    }
    
}
