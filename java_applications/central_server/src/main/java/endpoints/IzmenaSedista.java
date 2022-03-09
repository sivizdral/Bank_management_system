/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import Poruke.KomunikacijaJMS;
import Poruke.Poruka;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import nit.KomunikacionaNit;

/**
 *
 * @author Ivan
 */

@Path("sedista")
public class IzmenaSedista {
    
    @Resource(lookup = "qCSP1")
    Queue queueCSP1;
    
    @Resource(lookup = "qP1CS")
    Queue queueP1CS;

    @Resource(lookup = "jms/__defaultConnectionFactory")
    ConnectionFactory connFactory;
    
    @Resource(lookup = "qCSP2")
    Queue queueCSP2;
    
    @Resource(lookup = "qP2CS")
    Queue queueP2CS;
    
    @POST
    public Response izmeniSedisteKomitentu(@FormParam("komitent") String komitent, @FormParam("sediste") String sediste) {
        
        try {
            JMSContext context = KomunikacijaJMS.dohvPrimerak().getContext(connFactory);
            
            Poruka poruka = new Poruka();
            
            ObjectMessage objMsg = context.createObjectMessage(poruka);
            objMsg.setIntProperty("sediste", Integer.parseInt(sediste));
            objMsg.setIntProperty("komitent", Integer.parseInt(komitent));
            objMsg.setStringProperty("zahtev", "4");
            
            KomunikacionaNit nit = new KomunikacionaNit();
            nit.postaviParametre(objMsg, context, queueP1CS, queueCSP1);
            nit.start();
            
            synchronized (nit) {
                if (nit.aktivna() == true) nit.wait();
            }
            
            ObjectMessage rcvMsg = nit.dohvatiPoruku();
            Poruka poruka2 = (Poruka) rcvMsg.getObject();
            
            String povratni = poruka2.getPoruka();
            
            System.out.println(povratni);
            
            ObjectMessage obj2 = context.createObjectMessage(poruka);
            obj2.setIntProperty("sediste", Integer.parseInt(sediste));
            obj2.setIntProperty("komitent", Integer.parseInt(komitent));
            obj2.setStringProperty("zahtev", "19");
            
            KomunikacionaNit nit2 = new KomunikacionaNit();
            nit2.postaviParametre(obj2, context, queueP2CS, queueCSP2);
            nit2.start();
            
            synchronized (nit2) {
                if (nit2.aktivna() == true) nit2.wait();
            }
            
            ObjectMessage rcv2 = nit2.dohvatiPoruku();
            Poruka por2 = (Poruka) rcv2.getObject();
            
            String povr2 = por2.getPoruka();
            
            System.out.println(povr2);
            
            return Response.status(Response.Status.CREATED).entity(povr2).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Komitenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Komitenti.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.ACCEPTED).entity("Greska!").build();
    }
    
    
}
