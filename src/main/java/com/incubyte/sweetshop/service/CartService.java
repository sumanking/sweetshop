package com.incubyte.sweetshop.service;

import com.incubyte.sweetshop.model.CartItem;
import com.incubyte.sweetshop.model.Sweet;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CartService {

    public void addToCart(Map<Long, CartItem> cart, Sweet sweet, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        int availableStock = sweet.getQuantity();
        int currentInCart = cart.containsKey(sweet.getId())
                ? cart.get(sweet.getId()).getQuantity()
                : 0;

        if (currentInCart + quantity > availableStock) {
            throw new IllegalArgumentException("Cannot add more than available stock");
        }

        if (cart.containsKey(sweet.getId())) {
            cart.get(sweet.getId()).addQuantity(quantity);
        } else {
            cart.put(sweet.getId(), new CartItem(sweet, quantity));
        }
    }

    public double calculateTotal(Map<Long, CartItem> cart) {
        return cart.values()
                .stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}
