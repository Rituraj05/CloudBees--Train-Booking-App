package com.bookingservice.storage.interfaces;

import com.bookingservice.models.Seat;

import java.util.Map;

public interface TrainStorage {
     void createTrain(String trainId, int numOfSections, int seatsPerSection);
     boolean isSeatAvailable(String trainId, String section, String seatNo);
     void markSeatStatus(String trainId,String section, String seatNo,boolean isOccupied);
     Map<String, Seat[]> getSections(String trainId);
}
