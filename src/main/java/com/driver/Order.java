package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order() {

    }

//    public Order(String id, int deliveryTime) {
//        this.id = id;
//        this.deliveryTime = deliveryTime;
//    }
    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        // deliveryTime  = HH*60 + MM

        String[] arr = deliveryTime.split(":");
        String hour = arr[0];
        String minutes = arr[1];
        this.deliveryTime = Integer.parseInt(hour) * 60 + Integer.parseInt(minutes);
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime(String hour, String minutes) {
        return Integer.parseInt(hour) * 60 + Integer.parseInt(minutes);
    }

    public int getDeliveryTime() {return deliveryTime;}
}
