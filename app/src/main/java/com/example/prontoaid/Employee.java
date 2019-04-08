package com.example.prontoaid;

public class Employee {

    String Loc;
    String User;
    String Phone_Number;
    String Emp_Name;


    public String getContact() { return Phone_Number;   }

    public void setContact(String phone) {     Phone_Number = phone;
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

    public String getName(){ return Emp_Name;}

    public void setName(String emp_Name){Emp_Name=emp_Name; }

    public Employee(String location, String username, String phone, String emp_Name  ) {

        Loc = location;
        User = username;
        Phone_Number = phone;
        Emp_Name = emp_Name;
    }

    public Employee() {
    }
}