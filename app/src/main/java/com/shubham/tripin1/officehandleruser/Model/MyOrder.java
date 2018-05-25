package com.shubham.tripin1.officehandleruser.Model;

import java.util.List;

/**
 * Created by Tripin1 on 6/21/2017.
 */

public class MyOrder {
    private List<CoffeeOrder> mOrderList;
    private String mUserName;
    private String mUserMobile;
    private String mUserUid;
    private String mTimeAgo;

    public MyOrder(){
        //for firebase
    }

    public MyOrder(String mUserName , String mUserMobile , List<CoffeeOrder> mOrderList,String mTimeAgo, String mUserUid){
        this.mOrderList = mOrderList;
        this.mUserName = mUserName;
        this.mUserMobile = mUserMobile;
        this.mTimeAgo = mTimeAgo;
        this.mUserUid = mUserUid;
    }

    public String getmUserUid() {
        return mUserUid;
    }

    public void setmUserUid(String mUserUid) {
        this.mUserUid = mUserUid;
    }

    public List<CoffeeOrder> getmOrderList() {
        return mOrderList;
    }

    public void setmOrderList(List<CoffeeOrder> mOrderList) {
        this.mOrderList = mOrderList;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserMobile() {
        return mUserMobile;
    }

    public void setmUserMobile(String mUserMobile) {
        this.mUserMobile = mUserMobile;
    }

    public String getmTimeAgo() {
        return mTimeAgo;
    }

    public void setmTimeAgo(String mTimeAgo) {
        this.mTimeAgo = mTimeAgo;
    }

    public double getOrderCost(){
        double totalcost = 0;
        for(CoffeeOrder coffeeOrder : getmOrderList()){
            double n = Double.parseDouble(coffeeOrder.getmCoffeeNumber());
            double p = Double.parseDouble(coffeeOrder.getmItemPrice());
            totalcost = totalcost + (p*n) ;
        }
        return totalcost;
    }
}
