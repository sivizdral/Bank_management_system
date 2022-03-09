/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import Poruke.KomunikacijaJMS;
import Poruke.Poruka;
import entiteti.Mesto;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import nit.KomunikacionaNit;

/**
 *
 * @author Ivan
 */

@Path("mesta")
public class Mesta {
    
    @Resource(lookup = "qCSP1")
    Queue queueCSP1;
    
    @Resource(lookup = "qP1CS")
    Queue queueP1CS;
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    ConnectionFactory connFactory;
    
    @POST
    public Response kreirajMesto(@FormParam("naziv") String naziv, @FormParam("postanskiBroj") String postanskiBroj) {
        
        try {
            System.out.println("PROSLO");
            JMSContext context = KomunikacijaJMS.dohvPrimerak().getContext(connFactory);
            
            Mesto mesto = new Mesto();
            mesto.setNaziv(naziv);
            mesto.setPostBr(postanskiBroj);
            
            ObjectMessage objMsg = context.createObjectMessage(mesto);
            objMsg.setIntProperty("Podsistem", 1);
            objMsg.setStringProperty("zahtev", "1");
            
            KomunikacionaNit nit = new KomunikacionaNit();
            nit.postaviParametre(objMsg, context, queueP1CS, queueCSP1);
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
    
    
    
}
