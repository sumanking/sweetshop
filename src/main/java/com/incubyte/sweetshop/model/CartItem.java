package com.incubyte.sweetshop.model;

public class CartItem {

    private Sweet sweet;
    private int quantity;

    public CartItem(Sweet sweet, int quantity) {
        this.sweet = sweet;
        this.quantity = quantity;
    }

    public Sweet getSweet() {
        return sweet;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int qty) {
        this.quantity += qty;
    }

    public double getTotalPrice() {
        return sweet.getPrice() * quantity;
    }
}
