/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prvipodsistem;

import Poruke.FilijalaPoruka;
import Poruke.KomitentPoruka;
import Poruke.MestoPoruka;
import Poruke.PorukaTri;
import entiteti.Filijala;
import entiteti.Komitent;
import entiteti.Mesto;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
public class NitOsluskivanjaPrvi extends Thread {
    
    
    
    static EntityManagerFactory emf;
    static EntityManager em;
    static ConnectionFactory cf;
    static Queue queueP1P3;
    static Queue queueP3P1;
    
    private byte[] convertToBytes(Object object) throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(bos)) {
        out.writeObject(object);
        return bos.toByteArray();
    } 
}
    
    public NitOsluskivanjaPrvi(EntityManagerFactory emf, EntityManager em, ConnectionFactory cf, Queue qP1P3, Queue qP3P1) {
        this.emf = emf;
        this.em = em;
        this.cf = cf;
        queueP1P3 = qP1P3;
        queueP3P1 = qP3P1;
    }
    
    @Override
    public void run() {
        
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueP3P1);
        
        while (true) {
            
            try {
                ObjectMessage msg = (ObjectMessage) consumer.receive();
                
                List<Mesto> mesta = em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList();
                List<MestoPoruka> listamesta = new ArrayList<>();
                
                int size = mesta.size();
                ObjectMessage odg1 = context.createObjectMessage();
                odg1.setIntProperty("velicina", size);
                producer.send(queueP1P3, odg1);
                
                for(Mesto m : mesta) {
                    MestoPoruka mp = new MestoPoruka();
                    mp.setIdMes(m.getIdMes());
                    mp.setNaziv(m.getNaziv());
                    mp.setPostanskiBroj(m.getPostBr());
                    ObjectMessage odg = context.createObjectMessage(mp);
                    producer.send(queueP1P3, odg);
                }
                
                List<Komitent> komitenti = em.createNamedQuery("Komitent.findAll", Komitent.class).getResultList();
                List<KomitentPoruka> listakomitenata = new ArrayList<>();
                
                int sizek = komitenti.size();
                ObjectMessage odg2 = context.createObjectMessage();
                odg2.setIntProperty("velicina", sizek);
                producer.send(queueP1P3, odg2);
                
                for(Komitent k : komitenti) {
                    KomitentPoruka kp = new KomitentPoruka();
                    kp.setAdresa(k.getAdresa());
                    kp.setIdMes(k.getIdMes().getIdMes());
                    kp.setNaziv(k.getNaziv());
                    kp.setIdKom(k.getIdKom());
                    ObjectMessage odg = context.createObjectMessage(kp);
                    producer.send(queueP1P3, odg);
                }
                
                List<Filijala> filijale = em.createNamedQuery("Filijala.findAll", Filijala.class).getResultList();
                List<FilijalaPoruka> listafilijala = new ArrayList<>();
                
                int sizef = filijale.size();
                ObjectMessage odg3 = context.createObjectMessage();
                odg3.setIntProperty("velicina", sizef);
                producer.send(queueP1P3, odg3);
                
                for(Filijala f : filijale) {
                    FilijalaPoruka fp = new FilijalaPoruka();
                    fp.setAdresa(f.getAdresa());
                    fp.setIdMes(f.getMesto().getIdMes());
                    fp.setNaziv(f.getNaziv());
                    fp.setIdFil(f.getIdFil());
                    ObjectMessage odg = context.createObjectMessage(fp);
                    producer.send(queueP1P3, odg);
                }
//                
//                PorukaTri por = new PorukaTri();
//                por.setMesta(mesta);
//                por.setKomitenti(komitenti);
//                por.setFilijale(filijale);
//
//                ObjectMessage odg = context.createObjectMessage();
//                odg.setObject(por);
//                
//                producer.send(queueP1P3, odg);
            } catch (JMSException ex) {
                Logger.getLogger(NitOsluskivanjaPrvi.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                
        }
        
    }
    
}
