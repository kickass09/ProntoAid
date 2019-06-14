package com.example.prontoaid;

import android.widget.RatingBar;

public class Employee {

    String User;
    String Phone_Number;
    String Emp_Name;
    String Loclatitude;
    String Loclongitude;
    String Rating;

    public String getLoclatitude(){return  Loclatitude;}

    public void setLoclatitude(String lats){ Loclatitude=lats;}

    public String getLoclongitude(){return  Loclongitude;}

    public void setLoclongitude(String longs){ Loclongitude=longs;}

    public String getContact() { return Phone_Number;   }

    public void setContact(String phone) {     Phone_Number = phone; }

    public String getUsername() {
        return User;
    }

    public void setUsername(String username) {
        User = username;
    }

    public String getName(){ return Emp_Name;}

    public void setName(String emp_Name){Emp_Name=emp_Name; }

    public String getRating(){return Rating;}

    public Employee(String username, String phone, String emp_Name, String lats, String longs , String rating) {


        User = username;
        Phone_Number = phone;
        Emp_Name = emp_Name;
        Loclatitude=lats;
        Loclongitude=longs;
        Rating=rating;

    }
}