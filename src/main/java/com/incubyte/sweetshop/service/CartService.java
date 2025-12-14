package com.incubyte.sweetshop.service;

import com.incubyte.sweetshop.model.CartItem;
import com.incubyte.sweetshop.model.Sweet;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CartService {

    public void addToCart(Map<Long, CartItem> cart, Sweet sweet, int quantity) {
        validateQuantity(quantity);

        CartItem item = cart.get(sweet.getId());

        if (item == null) {
            addNewItem(cart, sweet, quantity);
        } else {
            increaseQuantity(item, quantity);
        }
    }

    public double calculateTotal(Map<Long, CartItem> cart) {
        return cart.values()
                   .stream()
                   .mapToDouble(CartItem::getTotalPrice)
                   .sum();
    }

    // ===================== HELPERS =====================

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
    }

    private void addNewItem(Map<Long, CartItem> cart, Sweet sweet, int quantity) {
        cart.put(sweet.getId(), new CartItem(sweet, quantity));
    }

    private void increaseQuantity(CartItem item, int quantity) {
        item.addQuantity(quantity);
    }
}
