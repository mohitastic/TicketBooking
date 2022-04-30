package com.booking.exceptions;

public class UserDetailNotFoundException extends Exception {
    public UserDetailNotFoundException() {
        super("User Detail not found");
    }
}
