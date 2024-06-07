package com.bookingservice.storage.impl;

import com.bookingservice.models.Ticket;
import com.bookingservice.storage.interfaces.TicketStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory class to store Tickets and its state properties..
 */
public class InMemoryTicketStorage implements TicketStorage {
    private Map<String, Ticket> tickets = new HashMap<>();
    private static InMemoryTicketStorage instance;

    public static InMemoryTicketStorage getInstance() {
        if(instance == null){
            synchronized (InMemoryTicketStorage.class) {
                if(instance == null) {
                    instance = new InMemoryTicketStorage();
                }
            }
        }
        return instance;
    }

    @Override
    public Ticket getTicketByTicketId(String ticketId) {
        if(tickets.containsKey(ticketId))
        {
            return tickets.get(ticketId);
        }
        else
            return null;
    }

    @Override
    public void addTicket(Ticket ticket) {
        String ticketId = createTicketId(ticket.getUser().getEmail(),ticket.getSeatNumber(),ticket.getSeatSection(),ticket.getTrainId());
        ticket.setTicketId(ticketId);
        tickets.put(ticketId, ticket);
    }

    @Override
    public void removeTicket(String ticketId) {
        tickets.remove(ticketId);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return new ArrayList<>(tickets.values());
    }

    private String createTicketId(String email,String seatNumber, String seatSection,String trainId){
        return email + seatNumber + seatSection+trainId;
    }
}
