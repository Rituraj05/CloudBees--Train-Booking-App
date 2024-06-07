package com.bookingservice;

import com.bookingservice.storage.impl.InMemoryTrainStorage;

import com.bookingservice.storage.impl.InMemoryTicketStorage;
import com.bookingservice.storage.interfaces.TrainStorage;
import com.bookingservice.storage.interfaces.UserStorage;
import com.bookingservice.storage.impl.InMemoryUserStorage;
import com.bookingservice.storage.interfaces.TicketStorage;
import com.bookingservice.models.Ticket;
import com.bookingservice.service.TrainService;
import com.bookingservice.enums.Location;

public class Main {

    public static void main(String[] args) {
        try {
            UserStorage inMemoryUserStorage = InMemoryUserStorage.getInstance();
            TicketStorage ticketStorage = InMemoryTicketStorage.getInstance();
            TrainStorage inMemoryTrainStorage = InMemoryTrainStorage.getInstance();
            inMemoryTrainStorage.createTrain("T1",3,4);
            inMemoryTrainStorage.createTrain("T2",2,2);
            TrainService trainService = new TrainService(inMemoryUserStorage,ticketStorage, inMemoryTrainStorage);

            TestCases testCases = new TestCases();

            testCases.invokeModifyTicketTestCases(trainService, inMemoryUserStorage);
            testCases.invokeSeatAllocation(trainService, inMemoryUserStorage);
            testCases.invokeUserBySeatSection(trainService, inMemoryUserStorage);
            testCases.invokeDeleteUserFromTrain(trainService, inMemoryUserStorage);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static class TestCases {
        /**
         * Create 1 Ticket for users(A) for T1 train
         * Fetch ticket details by ticket Id booked. (Section 3 - s1)
         * Modify the ticket to Section 2 - S1 (Releases Section 3 - s1)
         * Re-Modify  the ticket to Section 1 - S1 (Releases Section 2 - S1)
         * Re-Modify  the ticket to Section 2 - S4 (Releases Section 1 - S1)
         * Purchase 1 Ticket for users(B) for T1 train
         * User B was reassigned the available seat which was released while modifying for user A.(Section 3 - s1)
         */
        public void invokeModifyTicketTestCases(TrainService trainService, UserStorage inMemoryUserStorage) {
            inMemoryUserStorage.createUser("Sam", "jack", "sam@gmail.com");
            inMemoryUserStorage.createUser("raj", "naik", "raj@gmail.com");

            Ticket ticket1 = trainService.purchaseTicket("T1",Location.FRANCE, Location.GERMANY, "Sam", "bahadur", "sam@gmail.com");
            System.out.println(trainService.getTicketDetails(ticket1.getTicketId()).toString());

            trainService.modifyUserSeat(ticket1.getTicketId(), "Section2", "S1");
            System.out.println(trainService.getTicketDetails(ticket1.getTicketId()).toString());
            //System.out.println(trainService.getTicketDetails("sam@gmail.comS1Section3").toString());

            trainService.modifyUserSeat(ticket1.getTicketId(), "Section1", "S1");
            System.out.println(trainService.getTicketDetails(ticket1.getTicketId()).toString());

            trainService.modifyUserSeat(ticket1.getTicketId(), "Section2", "S4");
            System.out.println(trainService.getTicketDetails(ticket1.getTicketId()).toString());

            Ticket ticket2 = trainService.purchaseTicket("T1",Location.FRANCE, Location.GERMANY, "raj", "naik", "raj@gmail.com");
            System.out.println(trainService.getTicketDetails(ticket2.getTicketId()).toString());
        }


        /**
         * Create 3 Ticket for users(A,B) for T1 train
         * Fetch ticket details by ticket Id booked.
         */
        public void invokeSeatAllocation(TrainService trainService, UserStorage inMemoryUserStorage) {
            inMemoryUserStorage.createUser("Sam", "jack", "sam@gmail.com");
            inMemoryUserStorage.createUser("raj", "naik", "raj@gmail.com");

            Ticket ticket1 = trainService.purchaseTicket("T1",Location.FRANCE, Location.GERMANY, "Sam", "bahadur", "sam@gmail.com");
            System.out.println(trainService.getTicketDetails(ticket1.getTicketId()).toString());

            Ticket ticket2 = trainService.purchaseTicket("T1",Location.FRANCE, Location.LONDON, "raj", "naik", "raj@gmail.com");
            System.out.println(trainService.getTicketDetails(ticket2.getTicketId()).toString());

            Ticket ticket3 =trainService.purchaseTicket("T1",Location.ITALY, Location.GERMANY, "raj", "naik", "raj@gmail.com");
            System.out.println(trainService.getTicketDetails(ticket3.getTicketId()).toString());
        }

        /**
         * Create Ticket for 3 users(A,B,C) two ticket for T1(A,B) and one for T2(C) trains
         * Fetch user by Section booked at T1 , returns user A,B with seat numbers
         * Fetch user by Section booked at T3 , returns user C with seat numbers
         */
        public void invokeUserBySeatSection(TrainService trainService, UserStorage inMemoryUserStorage) {
            inMemoryUserStorage.createUser("Sam", "jack", "sam@gmail.com");
            inMemoryUserStorage.createUser("raj", "naik", "raj@gmail.com");
            inMemoryUserStorage.createUser("rohit", "paswan", "rohit@gmail.com");
            Ticket ticket1 =  trainService.purchaseTicket("T1",Location.FRANCE, Location.GERMANY, "Sam", "bahadur", "sam@gmail.com");
            Ticket ticket2 =  trainService.purchaseTicket("T1",Location.FRANCE, Location.LONDON, "raj", "naik", "raj@gmail.com");
            Ticket ticket3 =  trainService.purchaseTicket("T2",Location.LONDON, Location.ITALY, "raj", "naik", "raj@gmail.com");
            trainService.getUsersBySeatSection("T1","Section3");
            trainService.getUsersBySeatSection("T2","Section1");
        }

        /**
         * Create Ticket for 3 users(A,B,C) two ticket for T1 and one for T2 trains
         * Remove user B from T1
         * Book ticket for User C at T1 also, user C is allocated same seat as user B which was released.
         */
        public void invokeDeleteUserFromTrain(TrainService trainService, UserStorage inMemoryUserStorage) {
            inMemoryUserStorage.createUser("Sam", "jack", "sam@gmail.com");
            inMemoryUserStorage.createUser("raj", "naik", "raj@gmail.com");
            inMemoryUserStorage.createUser("rohit", "paswan", "rohit@gmail.com");
            Ticket ticket1 =  trainService.purchaseTicket("T1",Location.FRANCE, Location.GERMANY, "Sam", "bahadur", "sam@gmail.com");
            Ticket ticket2 =  trainService.purchaseTicket("T1",Location.FRANCE, Location.LONDON, "raj", "naik", "raj@gmail.com");
            Ticket ticket3 =  trainService.purchaseTicket("T2",Location.LONDON, Location.ITALY, "raj", "naik", "raj@gmail.com");
            trainService.removeUserFromTrain("T1","raj@gmail.com");
            trainService.getUsersBySeatSection("T1","Section3");
            Ticket ticket4 =  trainService.purchaseTicket("T1",Location.FRANCE, Location.LONDON, "rohit", "Naik", "rohit@gmail.com");
            trainService.getUsersBySeatSection("T1","Section3");
        }
    }
}