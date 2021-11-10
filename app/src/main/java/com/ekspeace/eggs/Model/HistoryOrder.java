package com.ekspeace.eggs.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class HistoryOrder {
    private ArrayList<String> items;
    private ArrayList<String> itemQuantity;
    private String orderDate;
    private ArrayList<String> cost;
    private String isArrived;

    public HistoryOrder() {
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public ArrayList<String> getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(ArrayList<String> itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public ArrayList<String> getCost() {
        return cost;
    }

    public void setCost(ArrayList<String> cost) {
        this.cost = cost;
    }

    public String isArrived() {
        return isArrived;
    }

    public void setArrived(String arrived) {
        isArrived = arrived;
    }
}
