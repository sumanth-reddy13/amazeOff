package com.driver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String, Order> ordersMap;
    HashMap<String, DeliveryPartner> partnersMap;
    HashMap<String, List<String>> partnerOrdersMap;
    List<String> allOrders;
    List<String> unassignedOrders;

    public OrderRepository() {
        this.ordersMap = new HashMap<>();
        this.partnersMap = new HashMap<>();
        this.partnerOrdersMap = new HashMap<>();
        this.allOrders = new ArrayList<>();
        this.unassignedOrders = new ArrayList<>();
    }

    public void addOrder(@RequestBody Order order) {
        String id = order.getId();
        allOrders.add(id);
        unassignedOrders.add(id);
        ordersMap.put(id, order);
    }

    public void addPartner(@PathVariable String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnersMap.put(partnerId, deliveryPartner);
    }

    public void addOrderPartnerPair(@RequestParam String orderId, @RequestParam String partnerId) {
        if (!partnerOrdersMap.containsKey(partnerId)) {
            partnerOrdersMap.put(partnerId, new ArrayList<>());
        }
        int num = partnersMap.get(partnerId).getNumberOfOrders();
        num += 1;
        partnersMap.get(partnerId).setNumberOfOrders(num);
        partnerOrdersMap.get(partnerId).add(orderId);
        unassignedOrders.remove(orderId);

    }

    public Order getOrderById(@PathVariable String orderId) {

        Order order = ordersMap.get(orderId);
        //order should be returned with an orderId.

        return order;
    }

    public DeliveryPartner getPartnerById(@PathVariable String partnerId) {

        DeliveryPartner deliveryPartner = partnersMap.get(partnerId);

        //deliveryPartner should contain the value given by partnerId

        return deliveryPartner;
    }

    public Integer getOrderCountByPartnerId(@PathVariable String partnerId) {

        Integer orderCount = partnerOrdersMap.get(partnerId).size();

        //orderCount should denote the orders given by a partner-id

        return orderCount;
    }

    public List<String> getOrdersByPartnerId(@PathVariable String partnerId) {

        List<String> orders = partnerOrdersMap.get(partnerId);

        //orders should contain a list of orders by PartnerId

        return orders;
    }

    public List<String> getAllOrders() {
        return allOrders;
    }

    public Integer getCountOfUnassignedOrders(){
        Integer countOfOrders = unassignedOrders.size();

        return countOfOrders;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(@PathVariable String time, @PathVariable String partnerId){
        String[] arr = time.split(":");
        String hour = arr[0];
        String minutes = arr[1];
        int givenTime = Integer.parseInt(hour) * 60 + Integer.parseInt(minutes);
        List<String> orders = partnerOrdersMap.get(partnerId);
        int undelivered = 0;

        for (String id : orders) {
            Order order = ordersMap.get(id);
            if (order.getDeliveryTime() > givenTime) undelivered++;
        }
        return undelivered;
    }

    public String getLastDeliveryTimeByPartnerId(@PathVariable String partnerId){
        String time = "";
        List<String> orders = partnerOrdersMap.get(partnerId);
        int lastDelivery = Integer.MIN_VALUE;

        for (String id : orders) {
            Order order = ordersMap.get(id);
            if (order.getDeliveryTime() > lastDelivery) lastDelivery = order.getDeliveryTime();
        }
        //Return the time when that partnerId will deliver his last delivery order.
        int hour = lastDelivery / 60;
        int minutes = lastDelivery % 60;

        if (hour < 10) time += "0" + hour;
        else time += hour;
        time += ":";
        time += minutes;
        return time;
    }

    public void deletePartnerById(@PathVariable String partnerId){

        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.
        partnersMap.remove(partnerId);
        List<String> orders = partnerOrdersMap.get(partnerId);
        partnerOrdersMap.remove(partnerId);
        for (String id : orders) {
            unassignedOrders.add(id);
        }
    }

    public void deleteOrderById(@PathVariable String orderId){

        //Delete an order and also
        // remove it from the assigned order of that partnerId
        ordersMap.remove(orderId);

        for (String id : partnerOrdersMap.keySet()) {
            if (partnerOrdersMap.get(id).contains(orderId)) {
                List<String> list = partnerOrdersMap.get(id);
                list.remove(orderId);
                partnerOrdersMap.put(id, list);
                break;
            }
        }

    }
}
