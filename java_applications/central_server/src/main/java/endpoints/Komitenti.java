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
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import nit.KomunikacionaNit;

/**
 *
 * @author Ivan
 */

@Path("komitenti")
public class Komitenti {
    
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
    public Response kreirajKomitenta(@FormParam("naziv") String naziv, @FormParam("adresa") String adresa, @FormParam("sediste") String sediste) {
        try {

            System.out.println("PROSLO");
            JMSContext context = KomunikacijaJMS.dohvPrimerak().getContext(connFactory);
            
            Komitent komitent = new Komitent();
            komitent.setNaziv(naziv);
            komitent.setAdresa(adresa);
            
            ObjectMessage objMsg = context.createObjectMessage(komitent);
            objMsg.setIntProperty("mesto", Integer.parseInt(sediste));
            objMsg.setStringProperty("zahtev", "3");
            
            KomunikacionaNit nit = new KomunikacionaNit();
            nit.postaviParametre(objMsg, context, queueP1CS, queueCSP1);
            nit.start();
            
            synchronized (nit) {
                if (nit.aktivna() == true) nit.wait();
            }
            
            ObjectMessage rcvMsg = nit.dohvatiPoruku();
            Poruka poruka = (Poruka) rcvMsg.getObject();
            
            String povratni = poruka.getPoruka();
            
            System.out.println("POVRATNA PORUKA");
            
            if (povratni.equals("Komitent je kreiran!")) {
                ObjectMessage obj = context.createObjectMessage();
                obj.setIntProperty("mesto", Integer.parseInt(sediste));
                obj.setStringProperty("naziv", naziv);
                obj.setStringProperty("adresa", adresa);
                obj.setStringProperty("zahtev", "20");
                
                KomunikacionaNit nit2 = new KomunikacionaNit();
                nit2.postaviParametre(obj, context, queueP2CS, queueCSP2);
                nit2.start();

                synchronized (nit2) {
                    if (nit2.aktivna() == true) nit2.wait();
                }
                
                ObjectMessage rcv = nit2.dohvatiPoruku();
                Poruka por = (Poruka) rcv.getObject();
            
                String pov = por.getPoruka();
            
                System.out.println(pov);
            }
            //String povratni = "SVE KUL";
            return Response.status(Response.Status.CREATED).entity(povratni).build();
            
            } catch (JMSException ex) {
            Logger.getLogger(Komitenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Komitenti.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.ACCEPTED).entity("Greska!").build();
    }
    
    
}
