/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drugipodsistem;

import Poruke.Poruka;
import entiteti.Komitent;
import entiteti.Racun;
import entiteti.Transakcija;
import static java.lang.Math.abs;
import java.sql.Timestamp;
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
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Ivan
 */
public class Main {
    
    static EntityManagerFactory emf;
    static EntityManager em;
    
    /*@PersistenceContext(unitName = "PrviPodsistemPU")
    EntityManager em;*/

    @Resource(lookup = "qCSP2")
    static Queue queueCSP2;
    
    @Resource(lookup = "qP2CS")
    static Queue queueP2CS;
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "qP3P2")
    static Queue queueP3P2;
    
    @Resource(lookup = "quP2P3")
    static Queue queueP2P3;
    
    public String dodajRacun(Racun racun, int idKomitent) {
    try{
        Komitent komitent = em.find(Komitent.class, idKomitent);
        racun.setIdKom(komitent);
        
            em.getTransaction().begin();
            em.persist(racun);
            em.getTransaction().commit();
        }
        catch(EntityExistsException e){
            return "Racun vec postoji!";
        } finally {
            if (em.getTransaction().isActive())
            em.getTransaction().rollback();
        }
        return "Racun je kreiran!";
    }
    
    public String zatvoriRacun(int brRacuna) {
        TypedQuery<Racun> nq = em.createNamedQuery("Racun.findByIdRac", Racun.class).setParameter("idRac", brRacuna);
        List<Racun> racuni = nq.getResultList();
        
        if (racuni.size() != 1) return "Pogresan ID racuna!";
        Racun racun = racuni.get(0);
        
        try {
            em.getTransaction().begin();
            racun.setStanje(0);
            racun.setDozvMinus(0);
            racun.setStatus("b");
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive())
            em.getTransaction().rollback();
        }
        
        return "Uspesno zatvoren racun!";
    }
    
    public String kreirajTransakciju(Transakcija t, int racunSa, int racunKa, String tip) {
        
        if (tip.equals("uplata")) {
            
            TypedQuery<Racun> nq = em.createNamedQuery("Racun.findByIdRac", Racun.class).setParameter("idRac", racunKa);
            List<Racun> racuni = nq.getResultList();
        
            if (racuni.size() != 1) return "Pogresan ID racuna ka!";
            Racun racun = racuni.get(0);
            
            t.setIdRacKa(racun);
            t.setRedniBrKa(racun.getBrTransakcija() + 1);
            t.setVrsta("u");
            
            try {
                em.getTransaction().begin();
                racun.setStanje(racun.getStanje() + t.getIznos());
                if (racun.getStatus().equals("b") && racun.getStanje() < 0 && abs(racun.getStanje()) < racun.getDozvMinus()) {
                    racun.setStatus("a");
                }
                if (racun.getStatus().equals("b") && racun.getStanje() >= 0) {
                    racun.setStatus("a");
                }
                racun.setBrTransakcija(racun.getBrTransakcija() + 1);
                em.persist(t);
                em.getTransaction().commit();
            } finally {
                if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            }
            
        } else if (tip.equals("isplata")) {
            
            TypedQuery<Racun> nq = em.createNamedQuery("Racun.findByIdRac", Racun.class).setParameter("idRac", racunSa);
            List<Racun> racuni = nq.getResultList();
        
            if (racuni.size() != 1) return "Pogresan ID racuna sa!";
            Racun racun = racuni.get(0);
            
            if (racun.getStatus().equals("b")) return "Racun je blokiran, isplata nije moguca!";
            //if (racun.getStanje() + racun.getDozvMinus() < t.getIznos()) return "Na racunu nema dovoljno sredstava za transakciju!";
            
            t.setIdRacSa(racun);
            t.setRedniBrSa(racun.getBrTransakcija() + 1);
            t.setVrsta("i");
            
            try {
                em.getTransaction().begin();
                if (racun.getStanje() >= t.getIznos()) {
                    racun.setStanje(racun.getStanje() - t.getIznos());
                } else if (racun.getStanje() + racun.getDozvMinus() >= t.getIznos()) {
                    racun.setStanje(racun.getStanje() - t.getIznos());
                } else {
                    racun.setStanje(racun.getStanje() - t.getIznos());
                    racun.setStatus("b");
                }
                racun.setBrTransakcija(racun.getBrTransakcija() + 1);
                em.persist(t);
                em.getTransaction().commit();
            } finally {
                if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            }
            
        } else if (tip.equals("prenos")) {
            
            TypedQuery<Racun> nq = em.createNamedQuery("Racun.findByIdRac", Racun.class).setParameter("idRac", racunKa);
            List<Racun> racuni = nq.getResultList();
        
            if (racuni.size() != 1) return "Pogresan ID racuna ka!";
            Racun racKa = racuni.get(0);
            
            TypedQuery<Racun> nq2 = em.createNamedQuery("Racun.findByIdRac", Racun.class).setParameter("idRac", racunSa);
            List<Racun> racuni2 = nq2.getResultList();
        
            if (racuni2.size() != 1) return "Pogresan ID racuna sa!";
            Racun racSa = racuni2.get(0);
            
            if (racSa.getStatus().equals("b")) return "Racun je blokiran, prenos nije moguc!";
            
            t.setIdRacKa(racKa);
            t.setRedniBrKa(racKa.getBrTransakcija() + 1);
            t.setIdRacSa(racSa);
            t.setRedniBrSa(racSa.getBrTransakcija() + 1);
            t.setVrsta("p");
            
            try {
                em.getTransaction().begin();
                if (racSa.getStanje() >= t.getIznos()) {
                    racSa.setStanje(racSa.getStanje() - t.getIznos());
                } else if (racSa.getStanje() + racSa.getDozvMinus() >= t.getIznos()) {
                    racSa.setStanje(racSa.getStanje() - t.getIznos());
                } else {
                    racSa.setStanje(racSa.getStanje() - t.getIznos());
                    racSa.setStatus("b");
                }
                racSa.setBrTransakcija(racSa.getBrTransakcija() + 1);
                
                racKa.setStanje(racKa.getStanje() + t.getIznos());
                if (racKa.getStatus().equals("b") && racKa.getStanje() < 0 && abs(racKa.getStanje()) < racKa.getDozvMinus()) {
                    racKa.setStatus("a");
                }
                if (racKa.getStatus().equals("b") && racKa.getStanje() >= 0) {
                    racKa.setStatus("a");
                }
                racKa.setBrTransakcija(racKa.getBrTransakcija() + 1);

                em.persist(t);
                em.getTransaction().commit();
            } finally {
                if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            }
            
    
        
    }
        return "Transakcija je kreirana!";
    }
    
    public String dodajKomitenta(Komitent komitent) {
    try{
        
        System.out.println("USAOKOMITENT");
            em.getTransaction().begin();
            em.persist(komitent);
            em.getTransaction().commit();
            
        }
        catch(EntityExistsException e){
            return "Komitent vec postoji!";
        } finally {
            if (em.getTransaction().isActive())
            em.getTransaction().rollback();
        }
        return "Komitent je kreiran!";
    }
    
    public List<String> dohvatiSveRacune(int idKom) {
        Komitent kom = em.find(Komitent.class, idKom);
        TypedQuery<Racun> nq = em.createQuery("SELECT r FROM Racun r WHERE r.idKom = :idKom", Racun.class).setParameter("idKom",kom);
        List<Racun> racuni = nq.getResultList();
        
            List<String> povratna = new ArrayList<String>();
            
            for (Racun r : racuni) {
                String idr = r.getIdRac().toString();
                String st = r.getStatus();
                String stn = (new Integer(r.getStanje())).toString();
                String min = (new Integer(r.getDozvMinus())).toString();
                String br = (new Integer(r.getBrTransakcija())).toString();
                String komit = r.getIdKom().getIdKom().toString();
                String dat = r.getDatumVreme().toString();
                String mes = (new Integer(r.getIdMes())).toString();
                String rac = String.format("%-10s %-10s %-10s %-10s %-10s %-10s %-20s %-10s", idr, st, stn, min, br, komit, dat, mes) + "*";
                povratna.add(rac);
            }
            
            for (String p : povratna) {
                System.out.println(p);
            }
        
        return povratna;
    }
    
    public List<String> dohvatiSveTransakcijeZaRacun(int idRac) {
        TypedQuery<Racun> nq = em.createNamedQuery("Racun.findByIdRac", Racun.class).setParameter("idRac", idRac);
        List<Racun> lista = nq.getResultList();
        
        if (lista.size() != 1) return null;
        Racun racun = lista.get(0);
        
        TypedQuery<Transakcija> nqr = em.createQuery("SELECT t FROM Transakcija t WHERE t.idRacSa = :idRac OR t.idRacKa = :idRac", Transakcija.class).setParameter("idRac", racun);
        List<Transakcija> transakcije = nqr.getResultList();
        
        List<String> povratna = new ArrayList<String>();
            
            for (Transakcija t : transakcije) {
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
                Racun kr = t.getIdRacKa();
                int racka;
                if (kr == null) racka = 0;
                else racka = kr.getIdRac();
                int racsa;
                Racun ks = t.getIdRacSa();
                if (ks == null) racsa = 0;
                else racsa = ks.getIdRac();
                
                String rac = String.format("%-10s %-10s %-10s %-10s %-10s %-10s %-20s %-10s %-10s %-10s", idt, vrsta, svrha, iznos, ka, sa, racka, racsa, fil, dat) + "*";
                povratna.add(rac);
            }
            
            for (String p : povratna) {
                System.out.println(p);
            }
        
        
        
        return povratna;
    }
    
    public String izmeniSedisteKomitentu(int sediste, int komitent) {
        
        Komitent kom = em.find(Komitent.class, komitent);
        try {
        System.out.println("USAOIZMENASEDISTA");
            em.getTransaction().begin();
            kom.setIdMes(sediste);
            em.getTransaction().commit();
            
        } finally {
            if (em.getTransaction().isActive())
            em.getTransaction().rollback();
        }
        return "Komitentu je izmenjeno sediste!";
        
    }
    
    
    
    public void obrada() {
        
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queueCSP2);
        JMSProducer producer=context.createProducer();
        
        while (consumer.receiveNoWait() != null);
        
        while (true) {
            try {
                ObjectMessage objMsg = (ObjectMessage) consumer.receive();
                System.out.println("PODSISTEM USAO");
                String zahtev = objMsg.getStringProperty("zahtev");
                System.out.println(zahtev);
                String povratni = null;
                List povratak = null;
                
                switch(zahtev) {
                    case "5": {
                        int idKomitent = objMsg.getIntProperty("komitent");
                        Racun racun = (Racun) objMsg.getObject();
                        povratni = dodajRacun(racun, idKomitent);
                    }
                        break;
                    case "6": {
                        int brojRacuna = objMsg.getIntProperty("racun");
                        povratni = zatvoriRacun(brojRacuna);
                    }
                        break;
                    case "7": {
                        String tip = objMsg.getStringProperty("tip");
                        String rsa = objMsg.getStringProperty("racunsa");
                        int racunSa = 0;
                        if (rsa.length() != 0) racunSa = Integer.parseInt(rsa);
                        String rka = objMsg.getStringProperty("racunka");
                        int racunKa = 0;
                        if (rka.length() != 0) racunKa = Integer.parseInt(rka);
                        String svrha = objMsg.getStringProperty("svrha");
                        int iznos = Integer.parseInt(objMsg.getStringProperty("iznos"));
                        String fil = objMsg.getStringProperty("filijala");
                        int idFilijale = 0;
                        if (fil.length() != 0) idFilijale = Integer.parseInt(fil);
                        
                        Transakcija t = new Transakcija();
                        if (tip.equals("uplata") || tip.equals("isplata")) t.setIdFil(idFilijale);
                        t.setIznos(iznos);
                        t.setSvrha(svrha);
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        t.setDatumVreme(time);

                        povratni = kreirajTransakciju(t, racunSa, racunKa, tip);
                    }
                        break; 
                    case "13": {
                        int idKomitenta = objMsg.getIntProperty("komitent");
                        povratak = dohvatiSveRacune(idKomitenta);
                    }
                    break;
                    case "14": {
                        int idRacuna = objMsg.getIntProperty("racun");
                        povratak = dohvatiSveTransakcijeZaRacun(idRacuna);
                    } break;
                    case "19": {
                        int sediste = objMsg.getIntProperty("sediste");
                        int komitent = objMsg.getIntProperty("komitent");
                        povratni = izmeniSedisteKomitentu(sediste,komitent);
                    } break;
                    case "20": {
                        String naziv = objMsg.getStringProperty("naziv");
                        String adresa = objMsg.getStringProperty("adresa");
                        int mesto = objMsg.getIntProperty("mesto");
                        
                        Komitent komitent = new Komitent();
                        komitent.setAdresa(adresa);
                        komitent.setNaziv(naziv);
                        komitent.setIdMes(mesto);
                        
                        povratni = dodajKomitenta(komitent);
                    } break;
                    
                }   
                
                Poruka poruka = new Poruka();
                poruka.setLista(povratak);
                poruka.setPoruka(povratni);
                
                System.out.println(povratni);
                
                ObjectMessage nmsg = context.createObjectMessage(poruka);
                nmsg.setStringProperty("zahtev", zahtev);
                producer.send(queueP2CS, nmsg);
                
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("DrugiPodsistemPU");
        em = emf.createEntityManager();
        
        NitOsluskivanjaDrugi nit = new NitOsluskivanjaDrugi(emf, em, connectionFactory, queueP2P3, queueP3P2);
        nit.start();
        
        Main main = new Main();
        main.obrada();
    }
    
}