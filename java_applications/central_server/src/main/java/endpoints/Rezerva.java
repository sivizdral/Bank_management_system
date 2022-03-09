/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import Poruke.KomunikacijaJMS;
import Poruke.Poruka;
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

@Path("rezerva")
public class Rezerva {
    
    @Resource(lookup = "qCSP3")
    Queue queueCSP3;
    
    @Resource(lookup = "qP3CS")
    Queue queueP3CS;
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    ConnectionFactory connFactory;
    
    
    @GET
    public Response dohvatiRezervu() {
        
        try {
            
            JMSContext context = KomunikacijaJMS.dohvPrimerak().getContext(connFactory);
            
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("Podsistem", 3);
            objMsg.setStringProperty("zahtev", "15");
            
            KomunikacionaNit nit = new KomunikacionaNit();
            nit.postaviParametre(objMsg, context, queueP3CS, queueCSP3);
            nit.start();
            
            synchronized (nit) {
                if (nit.aktivna() == true) nit.wait();
            }
            
            ObjectMessage rcvMsg = nit.dohvatiPoruku();
            Poruka poruka = (Poruka) rcvMsg.getObject();
            
            List<String> povratna = poruka.getLista();
            String baza = "" + povratna;
            
            return Response.status(Response.Status.ACCEPTED).entity(baza).build();
            
            
        } catch (JMSException ex) {
            Logger.getLogger(Rezerva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Rezerva.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.BAD_REQUEST).entity("Greska!").build();
        
    }
    
}
