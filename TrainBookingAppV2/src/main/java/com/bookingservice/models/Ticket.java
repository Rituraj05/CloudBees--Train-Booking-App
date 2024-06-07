package com.bookingservice.models;

import com.bookingservice.enums.Location;
import lombok.Data;

@Data
public class Ticket {
    private Location from;
    private String ticketId;
    private Location to;
    private User user;
    private double price;
    private String trainId;
    private String seatSection;
    private String seatNumber;
}