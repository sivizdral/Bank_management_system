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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import nit.KomunikacionaNit;

/**
 *
 * @author Ivan
 */

@Path("filijale")
public class Filijale {
    
    @Resource(lookup = "qCSP1")
    Queue queueCSP1;
    
    @Resource(lookup = "qP1CS")
    Queue queueP1CS;

    @Resource(lookup = "jms/__defaultConnectionFactory")
    ConnectionFactory connFactory;
    
    @POST
    public Response kreirajFilijalu(@FormParam("naziv") String naziv, @FormParam("adresa") String adresa, @FormParam("mesto") String mesto) { 
        
        try {
            JMSContext context = KomunikacijaJMS.dohvPrimerak().getContext(connFactory);
            
            Filijala filijala = new Filijala();
            filijala.setNaziv(naziv);
            filijala.setAdresa(adresa);
            
            ObjectMessage objMsg = context.createObjectMessage(filijala);
            objMsg.setIntProperty("mesto", Integer.parseInt(mesto));
            objMsg.setStringProperty("zahtev", "2");
            
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
            Logger.getLogger(Filijale.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Filijale.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.ACCEPTED).entity("Greska!").build();
    }
    
}
