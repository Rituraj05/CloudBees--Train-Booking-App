package com.bookingservice.storage.impl;

import com.bookingservice.models.Seat;
import com.bookingservice.storage.interfaces.TrainStorage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory class to store Trains and its state properties.
 */
public class InMemoryTrainStorage implements TrainStorage {
    private Map<String,Map<String, Seat[]>> trains = new HashMap<>();

    private static InMemoryTrainStorage instance;

    public static InMemoryTrainStorage getInstance() {
        if(instance == null){
            synchronized (InMemoryTrainStorage.class) {
                if(instance == null) {
                    instance = new InMemoryTrainStorage();
                }
            }
        }
        return instance;
    }

    public void createTrain(String trainId, int numOfSections, int seatsPerSection){
        Map<String, Seat[]> sections = new HashMap<>();
        for (int i = 0; i < numOfSections; i++) {
            sections.put("Section" + (i + 1), new Seat[seatsPerSection]);
            for (int j = 0; j < seatsPerSection; j++) {
                sections.get("Section" + (i + 1))[j] = new Seat("Section" + (i + 1),"S"+(j + 1));
            }
        }
        trains.put(trainId,sections);
    }

    public Map<String, Seat[]> getSections(String trainId) {
        return trains.get(trainId);
    }

    public boolean isSeatAvailable(String trainId, String section, String seatNo){
        if(this.trains.get(trainId).containsKey(section)){
            for(int i =0;i<this.trains.get(trainId).get(section).length;++i){
                Seat seat = this.trains.get(trainId).get(section)[i];
                if(seat.getSeatNumber().equals(seatNo)  && seat.isOccupied()== false){
                    System.out.println(section + ":" +seatNo + " is free");
                    return true;
                }
            }
        }
        System.out.println(section + ":" +seatNo + " is not free");
        return false;
    }

    public void markSeatStatus(String trainId,String section, String seatNo,boolean isOccupied){
        if(this.trains.get(trainId).containsKey(section)){
            Optional<Seat> currSeat =
            Arrays.stream(this.trains.get(trainId).get(section))
                    .filter(seat -> (seat.getSeatNumber().equals(seatNo))).findFirst();
            if(!currSeat.isEmpty()){
                currSeat.get().setOccupied(isOccupied);
                return;
            }
        }
        System.out.println("Unable to mark seat as occupied");
    }
}
