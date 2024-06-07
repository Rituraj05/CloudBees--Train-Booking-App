package com.bookingservice.storage.impl;

import com.bookingservice.models.User;
import com.bookingservice.storage.interfaces.UserStorage;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory class to create and fetch users.
 */
public class InMemoryUserStorage implements UserStorage {
    private static InMemoryUserStorage instance;
    private final HashMap<String, User> userMap;
    private InMemoryUserStorage() {
        userMap = new HashMap<>();
    }

    public static InMemoryUserStorage getInstance() {
        if(instance == null){
            synchronized (InMemoryUserStorage.class) {
                if(instance == null) {
                    instance = new InMemoryUserStorage();
                }
            }
        }
        return instance;
    }

    public  User createUser(String firstName,String lastName,String email){
        if(userMap.containsKey(email)){
            return userMap.get(email);
        }
        User newUser = new User(firstName,lastName,email);
        userMap.put(email,newUser);
        return newUser;
    }

    public User getUserById(String email){
        return userMap.get(email);
    }

    public void removeUser(String email){
        userMap.remove(email);
    }
}
