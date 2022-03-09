/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import Poruke.KomunikacijaJMS;
import Poruke.Poruka;
import entiteti.Komitent;
import entiteti.Mesto;
import entiteti.Racun;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import nit.KomunikacionaNit;

/**
 *
 * @author Ivan
 */

@Path("zatvaranje")
public class ZatvaranjeRacuna {
    
    @Resource(lookup = "qCSP2")
    Queue queueCSP2;
    
    @Resource(lookup = "qP2CS")
    Queue queueP2CS;
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    ConnectionFactory connFactory;
    
    @POST
    @Path("{idRac}")
    public Response zatvoriRacun(@PathParam("idRac") int idRac) {
        
        try {
            System.out.println("PROSLO");
            JMSContext context = KomunikacijaJMS.dohvPrimerak().getContext(connFactory);
            
            ObjectMessage objMsg3 = context.createObjectMessage();
            objMsg3.setIntProperty("racun", idRac);
            objMsg3.setStringProperty("zahtev", "6");
            
            KomunikacionaNit nit3 = new KomunikacionaNit();
            nit3.postaviParametre(objMsg3, context, queueP2CS, queueCSP2);
            nit3.start();
            
            synchronized (nit3) {
                if (nit3.aktivna() == true) nit3.wait();
            }
            
            ObjectMessage rcvMsg3 = nit3.dohvatiPoruku();
            Poruka poruka3 = (Poruka) rcvMsg3.getObject();
            
            String povratni = poruka3.getPoruka();
            
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
