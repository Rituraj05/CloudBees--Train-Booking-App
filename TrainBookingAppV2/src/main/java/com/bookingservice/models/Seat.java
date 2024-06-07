package com.bookingservice.models;

import lombok.Data;
@Data
public class Seat {
    public Seat(String section,String seatNumber){
        this.section=section;
        this.seatNumber = seatNumber;
        this.isOccupied = false;
    }
    private String seatNumber;
    private String section;
    private boolean isOccupied;
}