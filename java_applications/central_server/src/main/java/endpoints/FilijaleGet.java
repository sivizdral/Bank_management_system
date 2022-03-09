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

@Path("getfilijale")
public class FilijaleGet {
    
    @Resource(lookup = "qCSP1")
    Queue queueCSP1;
    
    @Resource(lookup = "qP1CS")
    Queue queueP1CS;
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    ConnectionFactory connFactory;
    
    @GET
    public Response dohvatiSveFilijale(){
        try {
            JMSContext context = KomunikacijaJMS.dohvPrimerak().getContext(connFactory);
            
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("Podsistem", 1);
            objMsg.setStringProperty("zahtev", "11");
            
            KomunikacionaNit nit = new KomunikacionaNit();
            nit.postaviParametre(objMsg, context, queueP1CS, queueCSP1);
            nit.start();
            
            synchronized (nit) {
                if (nit.aktivna() == true) nit.wait();
            }
            
            ObjectMessage rcvMsg = nit.dohvatiPoruku();
            Poruka poruka = (Poruka) rcvMsg.getObject();
            
            List<Filijala> lista = poruka.getLista();
            List<String> povratna = new ArrayList<String>();
            
            for (Filijala f : lista) {
                String idk = f.getIdFil().toString(); 
                String naz = f.getNaziv();
                String adr = f.getAdresa();
                String idm = f.getMesto().getIdMes().toString();
                String fil = String.format("%-10s %-50s %-50s %-10s", idk, naz, adr, idm) + "*";
                
                //String mes = k.getIdKom().toString() + "\t" + k.getNaziv() + "\t\t" + k.getAdresa() + "\t\t" + k.getIdMes().getIdMes() + "*";
                povratna.add(fil);
            }
            
            for (String p : povratna) {
                System.out.println(p);
            }
            
            String bz = String.format("%-10s %-50s %-50s %-10s", "IdFil", "Naziv", "Adresa", "IdMes") + "*";
            
            String baza = bz + povratna;
            
            return Response.status(Response.Status.ACCEPTED).entity(baza).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Greska!").build();
    }
    
    
}
