package com.bookingservice;

import com.bookingservice.models.Seat;
import com.bookingservice.storage.interfaces.TrainStorage;
import com.bookingservice.storage.impl.InMemoryTrainStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrainStorageTest {
    private TrainStorage trainStorage;

    @BeforeEach
    void setUp() {
        trainStorage = InMemoryTrainStorage.getInstance();
        trainStorage.createTrain("Train1", 2, 10);
    }

    @Test
    void testSingletonInstance() {
        InMemoryTrainStorage instance1 = InMemoryTrainStorage.getInstance();
        InMemoryTrainStorage instance2 = InMemoryTrainStorage.getInstance();
        assertSame(instance1, instance2, "Singleton instances should be the same");
    }

    @Test
    void testCreateTrain() {
        trainStorage.createTrain("Train2", 3, 5);
        assertNotNull(trainStorage.getSections("Train2"));
        assertEquals(3, trainStorage.getSections("Train2").size());
    }

    @Test
    void testIsSeatAvailable() {
        assertTrue(trainStorage.isSeatAvailable("Train1", "Section1", "S1"));
        assertFalse(trainStorage.isSeatAvailable("Train1", "Section1", "S11"), "Seat should not exist");
    }

    @Test
    void testMarkSeatStatus() {
        trainStorage.markSeatStatus("Train1", "Section1", "S1", true);
        assertFalse(trainStorage.isSeatAvailable("Train1", "Section1", "S1"));

        trainStorage.markSeatStatus("Train1", "Section1", "S1", false);
        assertTrue(trainStorage.isSeatAvailable("Train1", "Section1", "S1"));
    }

    @Test
    void testGetSections() {
        assertEquals(2, trainStorage.getSections("Train1").size(), "Train1 should have 2 sections");
    }
}
