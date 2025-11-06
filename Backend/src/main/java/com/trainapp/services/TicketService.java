package com.trainapp.services;

import com.trainapp.model.Connection;
import com.trainapp.model.Ticket;

public class TicketService {
    public TicketService(){
    }

    public Ticket createTicket(Connection connection){
        Ticket ticket = new Ticket(connection.getArrivalCity(), connection.getDepartureCity());
        return ticket;
    }
}
