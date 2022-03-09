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

@Path("racuni")
public class Racuni {
    
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
    public Response kreirajRacun(@FormParam("komitent") String kom, @FormParam("dozvoljeniMinus") String dozvMinus, @FormParam("mesto") String mes) {
        
        try {
            System.out.println("PROSLO");
            JMSContext context = KomunikacijaJMS.dohvPrimerak().getContext(connFactory);
            
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("Podsistem", 1);
            objMsg.setStringProperty("zahtev", "12");
            
            KomunikacionaNit nit = new KomunikacionaNit();
            nit.postaviParametre(objMsg, context, queueP1CS, queueCSP1);
            nit.start();
            
            synchronized (nit) {
                if (nit.aktivna() == true) nit.wait();
            }
            
            ObjectMessage rcvMsg = nit.dohvatiPoruku();
            Poruka poruka = (Poruka) rcvMsg.getObject();
            
            List<Komitent> listakom = poruka.getLista();
            
            Boolean postoji = false;
            for (Komitent k : listakom) {
                if (k.getIdKom() == Integer.parseInt(kom)) postoji = true;
            }
            
            if (postoji == false) return Response.status(Response.Status.BAD_REQUEST).entity("Ne postoji zadati komitent!").build();
            
            ObjectMessage objMsg2 = context.createObjectMessage();
            objMsg2.setIntProperty("Podsistem", 1);
            objMsg2.setStringProperty("zahtev", "10");
            
            KomunikacionaNit nit2 = new KomunikacionaNit();
            nit2.postaviParametre(objMsg2, context, queueP1CS, queueCSP1);
            nit2.start();
            
            synchronized (nit2) {
                if (nit2.aktivna() == true) nit2.wait();
            }
            
            ObjectMessage rcvMsg2 = nit2.dohvatiPoruku();
            Poruka poruka2 = (Poruka) rcvMsg2.getObject();
            
            List<Mesto> listames = poruka2.getLista();
            
            postoji = false;
            for (Mesto m : listames) {
                if (m.getIdMes() == Integer.parseInt(mes)) postoji = true;
            }
            
            if (postoji == false) return Response.status(Response.Status.BAD_REQUEST).entity("Ne postoji zadato mesto!").build();
            
            Racun racun = new Racun();
            racun.setDozvMinus(Integer.parseInt(dozvMinus));
            racun.setStanje(0);
            racun.setStatus("a");
            Timestamp time = new Timestamp(System.currentTimeMillis());
            racun.setDatumVreme(time);
            racun.setIdMes(Integer.parseInt(mes));
            racun.setBrTransakcija(0);
            
            ObjectMessage objMsg3 = context.createObjectMessage(racun);
            objMsg3.setIntProperty("komitent", Integer.parseInt(kom));
            objMsg3.setStringProperty("zahtev", "5");
            
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
    
    @GET
    @Path("{idKom}")
    public Response dohvatiRacuneZaKomitenta(@PathParam("idKom") String idKom) {
        
        try {
            JMSContext context = KomunikacijaJMS.dohvPrimerak().getContext(connFactory);
            
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("Podsistem", 1);
            objMsg.setStringProperty("zahtev", "12");
            
            KomunikacionaNit nit = new KomunikacionaNit();
            nit.postaviParametre(objMsg, context, queueP1CS, queueCSP1);
            nit.start();
            
            synchronized (nit) {
                if (nit.aktivna() == true) nit.wait();
            }
            
            ObjectMessage rcvMsg = nit.dohvatiPoruku();
            Poruka poruka = (Poruka) rcvMsg.getObject();
            
            List<Komitent> listakom = poruka.getLista();
            
            Boolean postoji = false;
            for (Komitent k : listakom) {
                if (k.getIdKom() == Integer.parseInt(idKom)) postoji = true;
            }
            
            if (postoji == false) return Response.status(Response.Status.BAD_REQUEST).entity("Ne postoji zadati komitent!").build();
            
            ObjectMessage msg = context.createObjectMessage();
            msg.setIntProperty("komitent", Integer.parseInt(idKom));
            msg.setStringProperty("zahtev", "13");
            
            KomunikacionaNit nit2 = new KomunikacionaNit();
            nit2.postaviParametre(msg, context, queueP2CS, queueCSP2);
            nit2.start();
            
            synchronized (nit2) {
                if (nit2.aktivna() == true) nit2.wait();
            }
            
            ObjectMessage rcv = nit2.dohvatiPoruku();
            Poruka por = (Poruka) rcv.getObject();
            
            List<String> povratna = por.getLista();
            
            String bz = String.format("%-10s %-10s %-10s %-10s %-10s %-10s %-20s %-10s", "IdRac", "Status", "Stanje", "DozvMinus", "BrojTr", "IdKom", "Vreme", "Mesto") + "*";
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
