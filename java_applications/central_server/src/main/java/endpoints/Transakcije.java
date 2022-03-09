/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import Poruke.KomunikacijaJMS;
import Poruke.Poruka;
import entiteti.Filijala;
import entiteti.Komitent;
import entiteti.Mesto;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import nit.KomunikacionaNit;

/**
 *
 * @author Ivan
 */

@Path("transakcije")
public class Transakcije {
    
    @Resource(lookup = "qCSP1")
    Queue queueCSP1;
    
    @Resource(lookup = "qP1CS")
    Queue queueP1CS;
    
    @Resource(lookup = "qCSP2")
    Queue queueCSP2;
    
    @Resource(lookup = "qP2CS")
    Queue queueP2CS;
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    ConnectionFactory connFactory;
    
    @POST
    public Response kreirajTransakciju(@FormParam("tip") String tip, @FormParam("racunsa") String racunSa, @FormParam("racunka") String racunKa,
            @FormParam("svrha") String svrha, @FormParam("iznos") String iznos, @FormParam("filijala") String filijala) {
        
        try {
            JMSContext context = KomunikacijaJMS.dohvPrimerak().getContext(connFactory);
            
            
            if (tip.equals("uplata") || tip.equals("isplata")) {
                
                ObjectMessage obj = context.createObjectMessage();
                obj.setStringProperty("zahtev", "11");
                
                KomunikacionaNit nit1 = new KomunikacionaNit();
                nit1.postaviParametre(obj, context, queueP1CS, queueCSP1);
                nit1.start();
                
                synchronized (nit1) {
                    if (nit1.aktivna() == true) nit1.wait();
                }
                
                ObjectMessage rcv = nit1.dohvatiPoruku();
                Poruka por = (Poruka) rcv.getObject();
                
                List<Filijala> filijale = por.getLista();
                
                Filijala fil = null;
                
                for (Filijala f : filijale) {
                    if (f.getIdFil() == Integer.parseInt(filijala)) fil = f;
                }
                
                if (fil == null) return Response.status(Response.Status.BAD_REQUEST).entity("Ne postoji data filijala!").build();
                
            }
            
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setStringProperty("tip", tip);
            objMsg.setStringProperty("racunsa", racunSa);
            objMsg.setStringProperty("racunka", racunKa);
            objMsg.setStringProperty("svrha", svrha);
            objMsg.setStringProperty("iznos", iznos);
            objMsg.setStringProperty("filijala", filijala);
            objMsg.setStringProperty("zahtev", "7");
            
            KomunikacionaNit nit = new KomunikacionaNit();
            nit.postaviParametre(objMsg, context, queueP2CS, queueCSP2);
            nit.start();
            
            synchronized (nit) {
                if (nit.aktivna() == true) nit.wait();
            }
            
            ObjectMessage rcvMsg = nit.dohvatiPoruku();
            Poruka poruka = (Poruka) rcvMsg.getObject();
            
            String povratni = poruka.getPoruka();
            
            System.out.println(povratni);
            
            return Response.status(Response.Status.CREATED).entity(povratni).build();
            
            } catch (JMSException ex) {
            Logger.getLogger(Komitenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Komitenti.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Greska!").build();
    }
    
    @GET
    @Path("{idRac}")
    public Response dohvatiTransakcije(@PathParam("idRac") String idRac) {
        try {
            JMSContext context = KomunikacijaJMS.dohvPrimerak().getContext(connFactory);
            
            ObjectMessage msg = context.createObjectMessage();
            msg.setIntProperty("racun", Integer.parseInt(idRac));
            msg.setStringProperty("zahtev", "14");
            
            KomunikacionaNit nit2 = new KomunikacionaNit();
            nit2.postaviParametre(msg, context, queueP2CS, queueCSP2);
            nit2.start();
            
            synchronized (nit2) {
                if (nit2.aktivna() == true) nit2.wait();
            }
            
            ObjectMessage rcv = nit2.dohvatiPoruku();
            Poruka por = (Poruka) rcv.getObject();
            
            List<String> povratna = por.getLista();
            String bz = String.format("%-10s %-10s %-10s %-10s %-10s %-10s %-20s %-10s %-10s %-10s", "IdTra", "Vrsta", "Svrha", "Iznos", "RBrKa", "RBrSa", "RacKa", "RacSa", "IdFil", "Vreme") + "*";

            
            String baza = bz + povratna;
            
            return Response.status(Response.Status.ACCEPTED).entity(baza).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Racuni.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Racuni.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Greska!").build();
    }
    
}
