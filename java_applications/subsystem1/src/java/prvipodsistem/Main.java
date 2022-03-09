/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prvipodsistem;

import Poruke.Poruka;
import entiteti.Filijala;
import entiteti.Komitent;
import entiteti.Mesto;
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

    @Resource(lookup = "qCSP1")
    static Queue queueCSP1;
    
    @Resource(lookup = "qP1CS")
    static Queue queueP1CS;
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "qP3P1")
    static Queue queueP3P1;
    
    @Resource(lookup = "qP1P3")
    static Queue queueP1P3;
    
    
    
    public String dodajMesto(Mesto mesto) {
    try{
            em.getTransaction().begin();
            em.persist(mesto);
            em.getTransaction().commit();
        }
        catch(EntityExistsException e){
            return "Mesto vec postoji!";
        } finally {
            if (em.getTransaction().isActive())
            em.getTransaction().rollback();
        }
        return "Mesto je kreirano!";
    }
    
    public String dodajFilijalu(Filijala filijala, int mestoKomitent) {
    try{
        Mesto mesto = null;
        List<Mesto> lista = dohvatiSvaMesta();
        for(Mesto m : lista) {
            if (m.getIdMes() == mestoKomitent) mesto = m;
        }
        
        if (mesto == null) return "Dato sediste ne postoji u bazi!";
        filijala.setMesto(mesto);
        
            em.getTransaction().begin();
            em.persist(filijala);
            em.getTransaction().commit();
        }
        catch(EntityExistsException e){
            return "Filijala vec postoji!";
        } finally {
            if (em.getTransaction().isActive())
            em.getTransaction().rollback();
        }
    
        return "Filijala je kreirana!";
    }
    
    public String dodajKomitenta(Komitent komitent, int mestoKomitent) {
    try{
        Mesto mesto = null;
        List<Mesto> lista = dohvatiSvaMesta();
        for(Mesto m : lista) {
            if (m.getIdMes() == mestoKomitent) mesto = m;
        }
        
        if (mesto == null) return "Dato sediste ne postoji u bazi!";
        komitent.setIdMes(mesto);
        
        System.out.println("USAOKOMITENT");
            em.getTransaction().begin();
            em.merge(komitent);
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
    
    public List<Mesto> dohvatiSvaMesta() {
        TypedQuery<Mesto> nq = em.createNamedQuery("Mesto.findAll", Mesto.class);
        List<Mesto> lista = nq.getResultList();
        return lista;
    }
    
    public List<Filijala> dohvatiSveFilijale() {
        TypedQuery<Filijala> nq = em.createNamedQuery("Filijala.findAll", Filijala.class);
        List<Filijala> lista = nq.getResultList();
        return lista;
    }
    
    public List<Komitent> dohvatiSveKomitente() {
        TypedQuery<Komitent> nq = em.createNamedQuery("Komitent.findAll", Komitent.class);
        List<Komitent> lista = nq.getResultList();
        return lista;
    }
    
    public String promeniSedisteKomitentu(int novoMesto, int idKomitent) {
        try {
            Mesto mesto = null;
            List<Mesto> lista = dohvatiSvaMesta();
            for(Mesto m : lista) {
                if (m.getIdMes() == novoMesto) mesto = m;
            }

            if (mesto == null) return "Dato sediste ne postoji u bazi!";

            Komitent komitent = null;
            List<Komitent> listakom = dohvatiSveKomitente();
            for (Komitent k : listakom) {
                if (k.getIdKom() == idKomitent) komitent = k;
            }

            if (komitent == null) return "Dati komitent ne postoji u bazi!";

            Komitent kom = em.find(Komitent.class, idKomitent);

            em.getTransaction().begin();
            kom.setIdMes(mesto);
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive())
            em.getTransaction().rollback();
        }
        return "Komitentu je izmenjeno sediste!";
        
    }
    
    public void obrada() {
        
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queueCSP1);
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
                    case "1": {
                        Mesto mesto = (Mesto) objMsg.getObject();
                        povratni = dodajMesto(mesto);
                    }
                        break;
                    case "2": {
                        Filijala filijala = (Filijala) objMsg.getObject();
                        int mestoKomitent = objMsg.getIntProperty("mesto");
                        povratni = dodajFilijalu(filijala, mestoKomitent);
                    }
                        break;
                    case "3": {
                        Komitent komitent = (Komitent) objMsg.getObject();
                        int mestoKomitent = objMsg.getIntProperty("mesto");
                        povratni = dodajKomitenta(komitent, mestoKomitent);
                    }
                        break; 
                    case "4": {
                        int novoMesto = objMsg.getIntProperty("sediste");
                        int idKomitent = objMsg.getIntProperty("komitent");
                        povratni = promeniSedisteKomitentu(novoMesto, idKomitent);
                    } break;
                    case "10": {
                        povratak = dohvatiSvaMesta();
                    }
                    break;
                    case "11": {
                        povratak = dohvatiSveFilijale();
                    } break;
                    case "12": {
                        povratak = dohvatiSveKomitente();
                    } break;
                }   
                
                Poruka poruka = new Poruka();
                poruka.setLista(povratak);
                poruka.setPoruka(povratni);
                
                System.out.println(povratni);
                
                ObjectMessage nmsg = context.createObjectMessage(poruka);
                nmsg.setStringProperty("zahtev", zahtev);
                producer.send(queueP1CS, nmsg);
                
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("PrviPodsistemPU");
        em = emf.createEntityManager();
        
        NitOsluskivanjaPrvi nit = new NitOsluskivanjaPrvi(emf, em, connectionFactory, queueP1P3, queueP3P1);
        nit.start();
        
        Main main = new Main();
        main.obrada();
    }
    
}