package com.bookingservice;

import com.bookingservice.storage.impl.InMemoryTicketStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.bookingservice.storage.interfaces.TicketStorage;
import com.bookingservice.enums.Location;
import com.bookingservice.models.Seat;
import com.bookingservice.models.Ticket;
import com.bookingservice.models.User;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TicketStorageTest {
    private TicketStorage ticketStorage;

    @BeforeEach
    void setUp() {
        ticketStorage = new InMemoryTicketStorage();
    }

    @Test
    void testAddTicket() {
        User user = new User("raj", "naik", "raj@example.com");
        assertEquals(0, ticketStorage.getAllTickets().size());

        Ticket expectedTicket = new Ticket();
        expectedTicket.setTrainId("1");
        expectedTicket.setUser(user);
        expectedTicket.setSeatNumber("S1");
        expectedTicket.setSeatSection("Section2");

        ticketStorage.addTicket(expectedTicket);
        assertEquals(1, ticketStorage.getAllTickets().size());
    }

    @Test
    void testRemoveTicket() {
        User user = new User("John", "Doe", "john.doe@example.com");
        Ticket expectedTicket = new Ticket();
        expectedTicket.setTrainId("1");
        expectedTicket.setUser(user);
        expectedTicket.setSeatNumber("S1");
        expectedTicket.setSeatSection("Section2");
        ticketStorage.addTicket(expectedTicket);
        assertEquals(1, ticketStorage.getAllTickets().size());

        ticketStorage.removeTicket( "john.doe@example.comS1Section21");
        assertEquals(0, ticketStorage.getAllTickets().size());
    }

    @Test
    void testGetAllTickets() {
        User user1 = new User("John", "Doe", "john.doe@example.com");
        Ticket expectedTicket1 = new Ticket();
        expectedTicket1.setTrainId("1");
        expectedTicket1.setUser(user1);
        expectedTicket1.setSeatNumber("S1");
        expectedTicket1.setSeatSection("Section2");
        ticketStorage.addTicket(expectedTicket1);

        User user2 = new User("Jane", "Smith", "jane.smith@example.com");
        Ticket expectedTicket2 = new Ticket();
        expectedTicket2.setTrainId("2");
        expectedTicket2.setUser(user2);
        expectedTicket2.setSeatNumber("S2");
        expectedTicket2.setSeatSection("Section4");

        ticketStorage.addTicket(expectedTicket1);
        ticketStorage.addTicket(expectedTicket2);

        List<Ticket> allTickets = ticketStorage.getAllTickets();
        assertEquals(2, allTickets.size());
        assertTrue(allTickets.contains(expectedTicket1));
        assertTrue(allTickets.contains(expectedTicket2));
    }

    @Test
    void testGetTicketById() {
        User user1 = new User("Ram", "Patnaik", "ram@example.com");
        Ticket expectedTicket1 = new Ticket();
        expectedTicket1.setTrainId("1");
        expectedTicket1.setUser(user1);
        expectedTicket1.setSeatNumber("S1");
        expectedTicket1.setSeatSection("Section2");
        ticketStorage.addTicket(expectedTicket1);

        User user2 = new User("Jane", "Smith", "jane.smith@example.com");
        Ticket expectedTicket2 = new Ticket();
        expectedTicket2.setTrainId("2");
        expectedTicket2.setUser(user2);
        expectedTicket2.setSeatNumber("S2");
        expectedTicket2.setSeatSection("Section4");
        ticketStorage.addTicket(expectedTicket1);
        ticketStorage.addTicket(expectedTicket2);

        assertEquals(expectedTicket1,ticketStorage.getTicketByTicketId("ram@example.comS1Section21"));
        assertEquals(expectedTicket2,ticketStorage.getTicketByTicketId("jane.smith@example.comS2Section42"));

    }
}
