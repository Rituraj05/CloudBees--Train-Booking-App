package com.bookingservice.models;
import lombok.Data;
@Data
public class User {
    public User(String firstName,String lastName,String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    private  String firstName;
    private  String lastName;
    private  String email;
}
