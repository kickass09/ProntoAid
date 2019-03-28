package com.example.prontoaid;

public class Employee {
    String AvailableNow;
    String Loc;
    String User;

    public String getAvailable() {
        return AvailableNow;
    }

    public void setAvailable(String available) {
        AvailableNow = available;
    }

    public String getLocation() {
        return Loc;
    }

    public void setLocation(String location) {
        Loc = location;
    }

    public String getUsername() {
        return User;
    }

    public void setUsername(String username) {
        User = username;
    }

    public Employee(String available, String location, String username) {
        AvailableNow = available;
        Loc = location;
        User = username;
    }

    public Employee() {
    }
}