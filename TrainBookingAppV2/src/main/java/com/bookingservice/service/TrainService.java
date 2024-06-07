package com.bookingservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bookingservice.storage.interfaces.UserStorage;
import com.bookingservice.storage.interfaces.TicketStorage;
import com.bookingservice.models.User;
import com.bookingservice.models.Ticket;
import com.bookingservice.models.Seat;
import com.bookingservice.storage.interfaces.TrainStorage;
import com.bookingservice.enums.Location;

/**
 * Service class that performs combined operations using multiple storage interfaces.
 * It provides multiple functionalities for e.g. booking and modifying of tickets etc.
 */
public class TrainService {
    protected UserStorage userStorage;
    protected TicketStorage ticketStorage;
    protected TrainStorage trainStorage;

    public TrainService(UserStorage userStorage, TicketStorage ticketStorage, TrainStorage trainStorage) {
        this.userStorage = userStorage;
        this.ticketStorage = ticketStorage;
        this.trainStorage = trainStorage;
    }

    public Ticket getTicketDetails(String ticketId) {
        return ticketStorage.getTicketByTicketId(ticketId);
    }

    public void removeUserFromTrain(String trainId,String userEmail) {
        List<Ticket> allUserTicketsByTrain = ticketStorage.getAllTickets().stream()
                .filter(ticket -> ticket.getTrainId().equals(trainId) && ticket.getUser().getEmail().equals(userEmail))
                .collect(Collectors.toList());
        for(int i=0;i<allUserTicketsByTrain.size();++i){
            Ticket curr = allUserTicketsByTrain.get(i);
            // Mark the seats as not occupied
            trainStorage.markSeatStatus(curr.getTrainId(),curr.getSeatSection(),curr.getSeatNumber(),false);
            // Clear ticket from ticket storage
            ticketStorage.removeTicket(curr.getTicketId());
        }
    }

    public void modifyUserSeat(String ticketId,String newSeatSection, String newSeatNumber) {
        Ticket ticket = ticketStorage.getTicketByTicketId(ticketId);
        if (ticket != null && trainStorage.isSeatAvailable(ticket.getTrainId(),newSeatSection,newSeatNumber)) {
            // Mark old seat as unoccupied
            String oldSeatSection = ticket.getSeatSection();
            String oldSeatNumber = ticket.getSeatNumber();
            String oldTicketId = ticket.getTicketId();
            trainStorage.markSeatStatus(ticket.getTrainId(),oldSeatSection,oldSeatNumber,false);
            ticketStorage.removeTicket(oldTicketId);
            // Mark new seat as occupied
            ticket.setSeatSection(newSeatSection);
            ticket.setSeatNumber(newSeatNumber);
            trainStorage.markSeatStatus(ticket.getTrainId(),newSeatSection,newSeatNumber,true);
            ticketStorage.addTicket(ticket);
            System.out.println("Modified the seat from " +oldSeatSection+":"+oldSeatNumber+ "--->" + newSeatSection + ":"+ newSeatNumber);
        }
        else {
            System.out.println("Unable to modify the seat");
        }
    }

    public Ticket purchaseTicket(String trainId, Location from, Location to, String firstName, String lastName, String email) {
        User user = userStorage.createUser(firstName,lastName,email);
        Ticket ticket = new Ticket();
        ticket.setTrainId(trainId);
        ticket.setFrom(from);
        ticket.setTo(to);
        ticket.setUser(user);
        ticket.setPrice(20); // Fixed price assumption
        Seat selectedSeat = selectAvailableSeat(trainId);
        if (selectedSeat == null) {
            System.out.println("No seats available..in train: "+ trainId);
            return null;
        }
        ticket.setSeatSection(selectedSeat.getSection());
        ticket.setSeatNumber(selectedSeat.getSeatNumber());
        ticketStorage.addTicket(ticket);
        return ticket;
    }

    public Map<String, User> getUsersBySeatSection(String trainId,String seatSection) {
        Map<String, User> usersInSection = new HashMap<>();
        for (Ticket ticket : ticketStorage.getAllTickets()) {
            if (ticket.getSeatSection().equals(seatSection) && ticket.getTrainId().equals(trainId)) {
                System.out.println(ticket.getUser().getFirstName() +" is allocated with " + ticket.getSeatNumber());
                usersInSection.put(ticket.getSeatNumber(), ticket.getUser());
            }
        }
        return usersInSection;
    }

    public Seat selectAvailableSeat(String trainId) {
        for (Map.Entry<String, Seat[]> entry : this.trainStorage.getSections(trainId).entrySet()) {
            String section = entry.getKey();
            Seat[] seats = entry.getValue();
            for (Seat seat : seats) {
                if (!seat.isOccupied()) {
                    seat.setOccupied(true);
                    System.out.println("Booking.. " + section + "-" + seat.getSeatNumber());
                    return seat;
                }
            }
        }
        return null; // No available seats
    }
}
