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
import Poruke.Poruka;
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
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author Ivan
 */
public class Main {
    
    @Resource(lookup = "qP3P1")
    static Queue queueP3P1;
    
    @Resource(lookup = "qP1P3")
    static Queue queueP1P3;
    
    @Resource(lookup = "qCSP3")
    static Queue queueCSP3;
    
    @Resource(lookup = "qP3CS")
    static Queue queueP3CS;
    
    @Resource(lookup = "qP3P2")
    static Queue queueP3P2;
    
    @Resource(lookup = "quP2P3")
    static Queue queueP2P3;
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;
    
    static EntityManagerFactory emf;
    static EntityManager em;
    
    public List<String> dohvatiSve() {
        
        TypedQuery<Komitent> nqk = em.createNamedQuery("Komitent.findAll", Komitent.class);
        List<Komitent> listakom = nqk.getResultList();
        
        TypedQuery<Mesto> nqm = em.createNamedQuery("Mesto.findAll", Mesto.class);
        List<Mesto> listames = nqm.getResultList();
        
        TypedQuery<Filijala> nqf = em.createNamedQuery("Filijala.findAll", Filijala.class);
        List<Filijala> listafil = nqf.getResultList();
        
        TypedQuery<Racun> nqr = em.createNamedQuery("Racun.findAll", Racun.class);
        List<Racun> listarac = nqr.getResultList();
        
        TypedQuery<Transakcija> nqt = em.createNamedQuery("Transakcija.findAll", Transakcija.class);
        List<Transakcija> listatra = nqt.getResultList();
        
        List<String> stringovi = new ArrayList<>();
        
        String bzk = String.format("%-10s %-50s %-50s %-10s", "IdKom", "Naziv", "Adresa", "IdMes") + "*";
        stringovi.add(bzk);
        
        for (Komitent k : listakom) {
            String idk = k.getIdKom().toString(); 
            String naz = k.getNaziv();
            String adr = k.getAdresa();
            String idm = (new Integer(k.getIdMes())).toString();
            String mes = String.format("%-3s %-20s %-50s %-3s", idk, naz, adr, idm) + "*";

            //String mes = k.getIdKom().toString() + "\t" + k.getNaziv() + "\t\t" + k.getAdresa() + "\t\t" + k.getIdMes().getIdMes() + "*";
            stringovi.add(mes);
        }
        
        
        stringovi.add("=========================================================================================*");
        stringovi.add("IdMes\t Naziv\t PostanskiBroj*");
        
        for (Mesto m : listames) {
            String mes = m.getIdMes().toString() + "\t" + m.getNaziv() + "\t" + m.getPostBr() + "*";
            stringovi.add(mes);
        }
        
        String bzf = String.format("%-10s %-50s %-50s %-10s", "IdFil", "Naziv", "Adresa", "IdMes") + "*";
        stringovi.add("=========================================================================================*");
        stringovi.add(bzf);
        
        for (Filijala f : listafil) {
            String idk = f.getIdFil().toString(); 
            String naz = f.getNaziv();
            String adr = f.getAdresa();
            String idm = (new Integer(f.getMesto())).toString();
            String fil = String.format("%-10s %-50s %-50s %-10s", idk, naz, adr, idm) + "*";

            //String mes = k.getIdKom().toString() + "\t" + k.getNaziv() + "\t\t" + k.getAdresa() + "\t\t" + k.getIdMes().getIdMes() + "*";
            stringovi.add(fil);
        }
         
        String bzr = String.format("%-10s %-10s %-10s %-10s %-10s %-10s %-20s %-10s", "IdRac", "Status", "Stanje", "DozvMinus", "BrojTr", "IdKom", "Vreme", "Mesto") + "*";
        stringovi.add("=========================================================================================*");
        stringovi.add(bzr);
        
        for (Racun r : listarac) {
            String idr = r.getIdRac().toString();
            String st = r.getStatus();
            String stn = (new Integer(r.getStanje())).toString();
            String min = (new Integer(r.getDozvMinus())).toString();
            String br = (new Integer(r.getBrTransakcija())).toString();
            String komit = (new Integer(r.getIdKom())).toString();
            String dat = r.getDatumVreme().toString();
            String mes = (new Integer(r.getIdMes())).toString();
            String rac = String.format("%-10s %-10s %-10s %-10s %-10s %-10s %-20s %-10s", idr, st, stn, min, br, komit, dat, mes) + "*";
            stringovi.add(rac);
        }
        
        String bzt = String.format("%-10s %-10s %-10s %-10s %-10s %-10s %-20s %-10s %-10s %-10s", "IdTra", "Vrsta", "Svrha", "Iznos", "RBrKa", "RBrSa", "RacKa", "RacSa", "IdFil", "Vreme") + "*";
        stringovi.add("=========================================================================================*");
        stringovi.add(bzt);
        
        for (Transakcija t : listatra) {
            String idt = t.getIdTra().toString();
            String vrsta = t.getVrsta();
            String svrha = t.getSvrha();
            String iznos = (new Integer(t.getIznos())).toString();
            String dat = t.getDatumVreme().toString();

            Integer ka = t.getRedniBrKa();
            Integer sa = t.getRedniBrSa();
            if (ka == null) ka = 0;
            if (sa == null) sa = 0;
            Integer fil = t.getIdFil();
            if (fil == null) fil = 0;
            Integer kr = t.getIdRacKa();
            if (kr == null) kr = 0;
            Integer ks = t.getIdRacSa();
            if (ks == null) ks = 0;

            String rac = String.format("%-10s %-10s %-10s %-10s %-10s %-10s %-20s %-10s %-10s %-10s", idt, vrsta, svrha, iznos, ka, sa, kr, ks, fil, dat) + "*";
            stringovi.add(rac);
        }
        
        stringovi.add("=========================================================================================*");
        
        return stringovi;
    }
    
    public List<String> dohvatiMesta() {
        
        TypedQuery<Mesto> nqm = em.createNamedQuery("Mesto.findAll", Mesto.class);
        List<Mesto> listames = nqm.getResultList();
        
        List<String> stringovi = new ArrayList<>();
        
        for (Mesto m : listames) {
            String mes = m.getIdMes().toString() + "\t" + m.getNaziv() + "\t" + m.getPostBr() + "*";
            stringovi.add(mes);
        }
        
        return stringovi;
    }  
    
    public List<String> dohvatiFilijale() {
        
        TypedQuery<Filijala> nqf = em.createNamedQuery("Filijala.findAll", Filijala.class);
        List<Filijala> listafil = nqf.getResultList();
        
        List<String> stringovi = new ArrayList<>();
        
        for (Filijala f : listafil) {
            String idk = f.getIdFil().toString(); 
            String naz = f.getNaziv();
            String adr = f.getAdresa();
            String idm = (new Integer(f.getMesto())).toString();
            String fil = String.format("%-10s %-50s %-50s %-10s", idk, naz, adr, idm) + "*";

            //String mes = k.getIdKom().toString() + "\t" + k.getNaziv() + "\t\t" + k.getAdresa() + "\t\t" + k.getIdMes().getIdMes() + "*";
            stringovi.add(fil);
        }
        
        return stringovi;
    }
    
    public void obrada() {
        
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queueCSP3);
        JMSProducer producer=context.createProducer();
        
        while (true) {
            
            try {
                ObjectMessage objMsg = (ObjectMessage) consumer.receive();
                System.out.println("PODSISTEM 3 USAO");
                String zahtev = objMsg.getStringProperty("zahtev");
                System.out.println(zahtev);
                String povratni = null;
                List povratak = null;
                
                switch(zahtev) {
                    case "15": {
                        povratak = dohvatiSve();
                        System.out.println("PROSAO POSLE DOHVATANJA");
                    } break;
                    case "21": {
                        povratak = dohvatiMesta();
                        System.out.println("DOHVATIO SVA MESTA");
                    } break;
                    case "22": {
                        povratak = dohvatiFilijale();
                        System.out.println("DOHVATIO SVE FILIJALE");
                    } break;
                }
                
                Poruka poruka = new Poruka();
                poruka.setLista(povratak);
                poruka.setPoruka(povratni);
                
                System.out.println(povratak);
                
                ObjectMessage nmsg = context.createObjectMessage(poruka);
                nmsg.setStringProperty("zahtev", zahtev);
                producer.send(queueP3CS, nmsg);
                
                
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
    }

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("TreciPodsistemPU");
        em = emf.createEntityManager();
        
        NitZaCuvanje nit = new NitZaCuvanje(emf, em, connectionFactory, queueP1P3, queueP3P1, queueP2P3, queueP3P2);
        nit.start();
        
        Main main = new Main();
        main.obrada();
    }
    
}
