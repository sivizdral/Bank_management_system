/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trecipodsistem;

import Entiteti.Filijala;
import Entiteti.Komitent;
import Entiteti.Mesto;
import Entiteti.Racun;
import Entiteti.Transakcija;
import Poruke.FilijalaPoruka;
import Poruke.KomitentPoruka;
import Poruke.MestoPoruka;
import Poruke.PorukaDva;
import Poruke.PorukaTri;
import Poruke.RacunPoruka;
import Poruke.TransakcijaPoruka;
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
public class NitZaCuvanje extends Thread {
    
    static EntityManagerFactory emf;
    static EntityManager em;
    static ConnectionFactory cf;
    static Queue queueP1P3;
    static Queue queueP3P1;
    static Queue queueP2P3;
    static Queue queueP3P2;
    
    public NitZaCuvanje(EntityManagerFactory emf, EntityManager em, ConnectionFactory cf, Queue qP1P3, Queue qP3P1, Queue qP2P3, Queue qP3P2) {
        this.emf = emf;
        this.em = em;
        this.cf = cf;
        queueP1P3 = qP1P3;
        queueP3P1 = qP3P1;
        queueP2P3 = qP2P3;
        queueP3P2 = qP3P2;
    }
    
    @Override
    public void run() {
        
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer1 = context.createConsumer(queueP1P3);
        JMSConsumer consumer2 = context.createConsumer(queueP2P3);
        
        
        while (true) {
            
            try {
                
                ObjectMessage msg1 = context.createObjectMessage();
                msg1.setIntProperty("p3zahtev", 1);
                producer.send(queueP3P1, msg1);
                
                ObjectMessage rcv1 = (ObjectMessage) consumer1.receive();
                int velmesta = rcv1.getIntProperty("velicina");
                
                List<MestoPoruka> porukemesto = new ArrayList<>();
                
                for (int i = 0; i < velmesta; i++) {
                    ObjectMessage rcv = (ObjectMessage) consumer1.receive();
                    MestoPoruka mp = (MestoPoruka) rcv.getObject();
                    porukemesto.add(mp);
                }
                
                ObjectMessage rcv2 = (ObjectMessage) consumer1.receive();
                int velkomitenata = rcv2.getIntProperty("velicina");
                
                List<KomitentPoruka> porukekomitent = new ArrayList<>();
                
                for (int i = 0; i < velkomitenata; i++) {
                    ObjectMessage rcv = (ObjectMessage) consumer1.receive();
                    KomitentPoruka kp = (KomitentPoruka) rcv.getObject();
                    porukekomitent.add(kp);
                }
                
                ObjectMessage rcv3 = (ObjectMessage) consumer1.receive();
                int velfilijala = rcv3.getIntProperty("velicina");
                
                List<FilijalaPoruka> porukefilijale = new ArrayList<>();
                
                for (int i = 0; i < velfilijala; i++) {
                    ObjectMessage rcv = (ObjectMessage) consumer1.receive();
                    FilijalaPoruka fp = (FilijalaPoruka) rcv.getObject();
                    porukefilijale.add(fp);
                }
                
                ObjectMessage msg2 = context.createObjectMessage();
                msg2.setIntProperty("p3zahtev", 2);
                producer.send(queueP3P2, msg2);

                ObjectMessage rcv4 = (ObjectMessage) consumer2.receive();
                int velracuna = rcv4.getIntProperty("velicina");
                
                List<RacunPoruka> porukeracun = new ArrayList<>();
                
                for (int i = 0; i < velracuna; i++) {
                    ObjectMessage rcv = (ObjectMessage) consumer2.receive();
                    RacunPoruka rp = (RacunPoruka) rcv.getObject();
                    porukeracun.add(rp);
                }
                
                ObjectMessage rcv5 = (ObjectMessage) consumer2.receive();
                int veltransakcija = rcv5.getIntProperty("velicina");
                
                List<TransakcijaPoruka> poruketransakcije = new ArrayList<>();
                
                for (int i = 0; i < veltransakcija; i++) {
                    ObjectMessage rcv = (ObjectMessage) consumer2.receive();
                    TransakcijaPoruka tp = (TransakcijaPoruka) rcv.getObject();
                    poruketransakcije.add(tp);
                }
                
                em.getTransaction().begin();
                em.createQuery("DELETE FROM Transakcija").executeUpdate();
                em.getTransaction().commit();
                
                em.getTransaction().begin();
                em.createQuery("DELETE FROM Racun").executeUpdate();
                em.getTransaction().commit();
                
                em.getTransaction().begin();
                em.createQuery("DELETE FROM Filijala").executeUpdate();
                em.getTransaction().commit();
                
                em.getTransaction().begin();
                em.createQuery("DELETE FROM Komitent").executeUpdate();
                em.getTransaction().commit();
                
                em.getTransaction().begin();
                em.createQuery("DELETE FROM Mesto").executeUpdate();
                em.getTransaction().commit();
                        
                 
                
                for(MestoPoruka mp : porukemesto) {
                    
                    Mesto novom = new Mesto();
                    novom.setIdMes(mp.getIdMes());
                    novom.setNaziv(mp.getNaziv());
                    novom.setPostBr(mp.getPostanskiBroj());
                    
                    em.getTransaction().begin();
                    em.persist(novom);
                    em.getTransaction().commit();
                }
                
                for(KomitentPoruka kp : porukekomitent) {
                    
                    Komitent novok = new Komitent();
                    novok.setIdKom(kp.getIdKom());
                    novok.setNaziv(kp.getNaziv());
                    novok.setAdresa(kp.getAdresa());
                    novok.setIdMes(kp.getIdMes());
                    
                    em.getTransaction().begin();
                    em.persist(novok);
                    em.getTransaction().commit();
                }
                
                for(FilijalaPoruka fp : porukefilijale) {
                    
                    Filijala novof = new Filijala();
                    novof.setIdFil(fp.getIdFil());
                    novof.setNaziv(fp.getNaziv());
                    novof.setAdresa(fp.getAdresa());
                    novof.setMesto(fp.getIdMes());
                    
                    em.getTransaction().begin();
                    em.persist(novof);
                    em.getTransaction().commit();
                }
               
                for(RacunPoruka rp : porukeracun) {
                    
                    Racun novor = new Racun();
                    novor.setIdRac(rp.getIdRac());
                    novor.setIdKom(rp.getIdKom());
                    novor.setIdMes(rp.getIdMes());
                    novor.setDatumVreme(rp.getDatumVreme());
                    novor.setDozvMinus(rp.getDozvMinus());
                    novor.setBrTransakcija(rp.getBrTransakcija());
                    novor.setStanje(rp.getStanje());
                    novor.setStatus(rp.getStatus());
                    
                    em.getTransaction().begin();
                    em.persist(novor);
                    em.getTransaction().commit();
                }
                
                for(TransakcijaPoruka tp : poruketransakcije) {
                    
                    Transakcija novot = new Transakcija();
                    novot.setIdTra(tp.getIdTra());
                    novot.setIdFil(tp.getIdFil());
                    novot.setIdRacKa(tp.getIdRacKa());
                    novot.setIdRacSa(tp.getIdRacSa());
                    novot.setRedniBrKa(tp.getRedniBrKa());
                    novot.setRedniBrSa(tp.getRedniBrSa());
                    novot.setDatumVreme(tp.getDatumVreme());
                    novot.setIznos(tp.getIznos());
                    novot.setSvrha(tp.getSvrha());
                    novot.setVrsta(tp.getVrsta());
                    
                    em.getTransaction().begin();
                    em.persist(novot);
                    em.getTransaction().commit();
                }
                
                Thread.sleep(120000);
                
            } catch (InterruptedException ex) {
                Logger.getLogger(NitZaCuvanje.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JMSException ex) {
                Logger.getLogger(NitZaCuvanje.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    
}
