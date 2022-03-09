/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import Poruke.KomunikacijaJMS;
import Poruke.Poruka;
import entiteti.Filijala;
import entiteti.Mesto;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import nit.KomunikacionaNit;

/**
 *
 * @author Ivan
 */

@Path("razlika")
public class Razlika {
    
    @Resource(lookup = "qCSP1")
    Queue queueCSP1;
    
    @Resource(lookup = "qP1CS")
    Queue queueP1CS;
    
    @Resource(lookup = "qCSP2")
    Queue queueCSP2;
    
    @Resource(lookup = "qP2CS")
    Queue queueP2CS;
    
    @Resource(lookup = "qCSP3")
    Queue queueCSP3;
    
    @Resource(lookup = "qP3CS")
    Queue queueP3CS;
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    ConnectionFactory connFactory;
    
    @GET
    public Response dohvatiRazlike() {
        
        try {
            JMSContext context = KomunikacijaJMS.dohvPrimerak().getContext(connFactory);
            
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("Podsistem", 1);
            objMsg.setStringProperty("zahtev", "10");
            
            KomunikacionaNit nit = new KomunikacionaNit();
            nit.postaviParametre(objMsg, context, queueP1CS, queueCSP1);
            nit.start();
            
            synchronized (nit) {
                if (nit.aktivna() == true) nit.wait();
            }
            
            ObjectMessage rcvMsg = nit.dohvatiPoruku();
            Poruka poruka = (Poruka) rcvMsg.getObject();
            
            List<Mesto> mesta = poruka.getLista();
            List<String> povratna = new ArrayList<>();
            
            for (Mesto m : mesta) {
                String mes = m.getIdMes().toString() + "\t" + m.getNaziv() + "\t" + m.getPostBr() + "*";
                povratna.add(mes);
            }
            
            ObjectMessage obj2 = context.createObjectMessage();
            obj2.setIntProperty("Podsistem", 3);
            obj2.setStringProperty("zahtev", "21");
            
            KomunikacionaNit nit2 = new KomunikacionaNit();
            nit2.postaviParametre(obj2, context, queueP3CS, queueCSP3);
            nit2.start();
            
            synchronized (nit2) {
                if (nit2.aktivna() == true) nit2.wait();
            }
            
            ObjectMessage rcv2 = nit2.dohvatiPoruku();
            Poruka poruka2 = (Poruka) rcv2.getObject();
            
            List<String> povratna2 = poruka2.getLista();
            List<String> razlika = new ArrayList<>();
            
            if (povratna.size() == povratna2.size()) {
                
                for (int i = 0; i < povratna.size(); i++) {
                    if (!povratna.get(i).equals(povratna2.get(i))) razlika.add(povratna.get(i));
                }
                
            } else {
                
                if (povratna.size() > povratna2.size()) {
                    
                    for (int i = 0; i < povratna2.size(); i++) {
                        if (!povratna.get(i).equals(povratna2.get(i))) razlika.add(povratna.get(i));
                    }   
                    
                    for (int i = povratna2.size(); i < povratna.size(); i++) {
                        razlika.add(povratna.get(i));
                    }
                } else {
                    
                    for (int i = 0; i < povratna.size(); i++) {
                        if (!povratna.get(i).equals(povratna2.get(i))) razlika.add(povratna.get(i));
                    }   
                    
                    for (int i = povratna.size(); i < povratna2.size(); i++) {
                        razlika.add(povratna2.get(i));
                    }
                    
                }
                 
            }
            
            ObjectMessage obj3 = context.createObjectMessage();
            obj3.setIntProperty("Podsistem", 1);
            obj3.setStringProperty("zahtev", "11");
            
            KomunikacionaNit nit3 = new KomunikacionaNit();
            nit3.postaviParametre(obj3, context, queueP1CS, queueCSP1);
            nit3.start();
            
            synchronized (nit3) {
                if (nit3.aktivna() == true) nit3.wait();
            }
            
            ObjectMessage rcv3 = nit3.dohvatiPoruku();
            Poruka poruka3 = (Poruka) rcv3.getObject();
            
            List<Filijala> filijale = poruka3.getLista();
            List<String> povratna3 = new ArrayList<>();
            
            for (Filijala f : filijale) {
                String idk = f.getIdFil().toString(); 
                String naz = f.getNaziv();
                String adr = f.getAdresa();
                String idm = f.getMesto().getIdMes().toString();
                String fil = String.format("%-10s %-50s %-50s %-10s", idk, naz, adr, idm) + "*";
                
                //String mes = k.getIdKom().toString() + "\t" + k.getNaziv() + "\t\t" + k.getAdresa() + "\t\t" + k.getIdMes().getIdMes() + "*";
                povratna3.add(fil);
            }
            
            ObjectMessage obj4 = context.createObjectMessage();
            obj4.setIntProperty("Podsistem", 3);
            obj4.setStringProperty("zahtev", "22");
            
            KomunikacionaNit nit4 = new KomunikacionaNit();
            nit4.postaviParametre(obj4, context, queueP3CS, queueCSP3);
            nit4.start();
            
            synchronized (nit4) {
                if (nit4.aktivna() == true) nit4.wait();
            }
            
            ObjectMessage rcv4 = nit4.dohvatiPoruku();
            Poruka poruka4 = (Poruka) rcv4.getObject();
            
            List<String> povratna4 = poruka4.getLista();
            
            if (povratna3.size() == povratna4.size()) {
                
                for (int i = 0; i < povratna3.size(); i++) {
                    if (!povratna3.get(i).equals(povratna4.get(i))) razlika.add(povratna3.get(i));
                }
                
            } else {
                
                if (povratna3.size() > povratna4.size()) {
                    
                    for (int i = 0; i < povratna4.size(); i++) {
                        if (!povratna3.get(i).equals(povratna4.get(i))) razlika.add(povratna3.get(i));
                    }   
                    
                    for (int i = povratna4.size(); i < povratna3.size(); i++) {
                        razlika.add(povratna3.get(i));
                    }
                } else {
                    
                    for (int i = 0; i < povratna3.size(); i++) {
                        if (!povratna3.get(i).equals(povratna4.get(i))) razlika.add(povratna3.get(i));
                    }   
                    
                    for (int i = povratna3.size(); i < povratna4.size(); i++) {
                        razlika.add(povratna4.get(i));
                    }
                    
                }
                 
            }
            
            if (razlika.size() == 0) razlika.add("SVI SU ISTI*");
            
            return Response.status(Response.Status.ACCEPTED).entity(razlika).build();
        } catch (JMSException ex) {
            Logger.getLogger(Razlika.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Razlika.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    return Response.status(Response.Status.BAD_REQUEST).entity("Greska!").build();
    }
}
