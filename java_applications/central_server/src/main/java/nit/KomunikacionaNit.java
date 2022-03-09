/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nit;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;

/**
 *
 * @author Ivan
 */
public class KomunikacionaNit extends Thread {
    
    private Boolean aktivna = true;
    private ObjectMessage objMsg;
    private ObjectMessage received;
    private JMSContext context;
    private JMSConsumer consumer;
    private JMSProducer producer;
    private Destination producer_destination;
    private Destination consumer_source;
    
    public ObjectMessage dohvatiPoruku() { return received; }
    
    public Boolean aktivna() { return aktivna; }
    
    public void postaviParametre(ObjectMessage msg, JMSContext con, Destination consumer_source, Destination producer_destination) {
        objMsg = msg;
        context = con;
        producer = context.createProducer();
        consumer = context.createConsumer(consumer_source);
        this.consumer_source = consumer_source;
        this.producer_destination = producer_destination;
    }
    
    @Override
    public void run() {      
        producer.send(producer_destination, objMsg);
        received = (ObjectMessage) consumer.receive();
        synchronized (this) {
            aktivna = false;
            this.notify();
        }
    }
    
}
