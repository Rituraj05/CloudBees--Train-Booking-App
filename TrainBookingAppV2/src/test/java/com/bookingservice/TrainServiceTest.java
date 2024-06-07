package com.bookingservice;

import com.bookingservice.enums.Location;
import com.bookingservice.models.Seat;
import com.bookingservice.models.Ticket;
import com.bookingservice.models.User;
import com.bookingservice.storage.interfaces.TicketStorage;
import com.bookingservice.storage.interfaces.TrainStorage;
import com.bookingservice.storage.interfaces.UserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.bookingservice.service.TrainService;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;

import java.util.*;


public class TrainServiceTest {

    private TrainService trainService;
    private UserStorage userStorage;
    private TicketStorage ticketStorage;
    private TrainStorage trainStorage;

    @BeforeEach
    void setUp() {
        userStorage = Mockito.mock(UserStorage.class);
        ticketStorage = Mockito.mock(TicketStorage.class);
        trainStorage = Mockito.mock(TrainStorage.class);
        trainService = new TrainService(userStorage, ticketStorage, trainStorage);
    }

    @Test
    void testPurchaseTicket() {
        String trainId = "Train1";
        Location from = Location.LONDON;
        Location to = Location.FRANCE;
        String firstName = "Raj";
        String lastName = "Naik";
        String email = "raje@example.com";
        User user = new User(firstName, lastName, email);

        when(userStorage.createUser(firstName, lastName, email)).thenReturn(user);
        Seat seat = new Seat("Section1", "S1");
        when(trainStorage.getSections(trainId)).thenReturn(Collections.singletonMap("Section1", new Seat[]{seat}));
        when(trainStorage.isSeatAvailable(trainId, "Section1", "S1")).thenReturn(true);

        Ticket ticket = trainService.purchaseTicket(trainId, from, to, firstName, lastName, email);

        assertNotNull(ticket);
        assertEquals(trainId, ticket.getTrainId());
        assertEquals(from, ticket.getFrom());
        assertEquals(to, ticket.getTo());
        assertEquals(user, ticket.getUser());
        assertEquals("Section1", ticket.getSeatSection());
        assertEquals("S1", ticket.getSeatNumber());
        verify(ticketStorage, times(1)).addTicket(ticket);
    }

    @Test
    void testGetTicketDetails() {
        String ticketId = "Ticket1";
        Ticket ticket = new Ticket();
        ticket.setTicketId(ticketId);
        when(ticketStorage.getTicketByTicketId(ticketId)).thenReturn(ticket);

        Ticket result = trainService.getTicketDetails(ticketId);
        assertEquals(ticket, result);
    }

    @Test
    void testRemoveUserFromTrain() {
        String trainId = "Train1";
        String firstName = "Raj";
        String lastName = "Naik";
        String email = "raje@example.com";
        Ticket ticket = new Ticket();
        ticket.setTrainId(trainId);
        ticket.setSeatSection("Section1");
        ticket.setSeatNumber("S1");
        ticket.setUser(new User(firstName, lastName, email));

        List<Ticket> tickets = Collections.singletonList(ticket);
        when(ticketStorage.getAllTickets()).thenReturn(tickets);

        trainService.removeUserFromTrain(trainId, email);

        verify(trainStorage, times(1)).markSeatStatus(trainId, "Section1", "S1", false);
        verify(ticketStorage, times(1)).removeTicket(ticket.getTicketId());
    }

    @Test
    void testModifyUserSeat() {
        String ticketId = "Ticket1";
        String firstName = "Raj";
        String lastName = "Naik";
        String email = "raje@example.com";
        String newSeatSection = "Section1";
        String newSeatNumber = "S2";
        Ticket ticket = new Ticket();
        ticket.setTicketId(ticketId);
        ticket.setTrainId("Train1");
        ticket.setSeatSection("Section1");
        ticket.setSeatNumber("S1");
        ticket.setUser(new User(firstName, lastName, email));

        when(ticketStorage.getTicketByTicketId(ticketId)).thenReturn(ticket);
        when(trainStorage.isSeatAvailable("Train1", newSeatSection, newSeatNumber)).thenReturn(true);

        trainService.modifyUserSeat(ticketId, newSeatSection, newSeatNumber);

        verify(trainStorage, times(1)).markSeatStatus("Train1", "Section1", "S1", false);
        verify(trainStorage, times(1)).markSeatStatus("Train1", newSeatSection, newSeatNumber, true);
        verify(ticketStorage, times(1)).addTicket(ticket);
        assertEquals(newSeatSection, ticket.getSeatSection());
        assertEquals(newSeatNumber, ticket.getSeatNumber());
    }

    @Test
    void testGetUsersBySeatSection() {
        String trainId = "Train1";
        String seatSection = "Section1";
        String firstName = "Raj";
        String lastName = "Naik";
        String email = "raje@example.com";
        Ticket ticket = new Ticket();
        ticket.setTrainId(trainId);
        ticket.setSeatSection(seatSection);
        ticket.setSeatNumber("S1");
        User user = new User(firstName, lastName, email);
        ticket.setUser(user);

        when(ticketStorage.getAllTickets()).thenReturn(Collections.singletonList(ticket));

        Map<String, User> usersInSection = trainService.getUsersBySeatSection(trainId, seatSection);

        assertEquals(1, usersInSection.size());
        assertEquals(user, usersInSection.get("S1"));
    }
}
