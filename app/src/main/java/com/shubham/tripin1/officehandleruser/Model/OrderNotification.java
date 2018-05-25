package com.shubham.tripin1.officehandleruser.Model;


/**
 * Created by Tripin1 on 7/5/2017.
 */

public class OrderNotification {
    private String name;
    private  String hpass;

    public OrderNotification(String name, String hpass) {
        this.name = name;
        this.hpass = hpass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHpass() {
        return hpass;
    }

    public void setHpass(String hpass) {
        this.hpass = hpass;
    }
}
