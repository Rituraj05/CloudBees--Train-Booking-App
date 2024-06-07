package com.bookingservice.storage.interfaces;

import com.bookingservice.models.Ticket;

import java.util.List;
public interface TicketStorage {
    Ticket getTicketByTicketId(String tickedId);
    void addTicket(Ticket ticket);
    void removeTicket(String ticketId);
    List<Ticket> getAllTickets();
}