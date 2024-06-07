package com.bookingservice.storage.interfaces;

import com.bookingservice.models.User;

public interface UserStorage {
    User createUser(String firstName, String lastName, String email);
    User getUserById(String email);
    void removeUser(String email);
}
