package com.incubyte.sweetshop.service;

import com.incubyte.sweetshop.model.CartItem;
import com.incubyte.sweetshop.model.Sweet;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CartService {

    // ===================== ADD TO CART =====================

    public void addToCart(Map<Long, CartItem> cart, Sweet sweet, int quantity) {

        validateCart(cart);
        validateSweet(sweet);
        validateQuantity(quantity);

        if (cart.containsKey(sweet.getId())) {
            cart.get(sweet.getId()).addQuantity(quantity);
        } else {
            cart.put(sweet.getId(), new CartItem(sweet, quantity));
        }
    }

    // ===================== TOTAL CALCULATION =====================

    public double calculateTotal(Map<Long, CartItem> cart) {
        validateCart(cart);

        return cart.values()
                   .stream()
                   .mapToDouble(CartItem::getTotalPrice)
                   .sum();
    }

    // ===================== VALIDATIONS =====================

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
    }

    private void validateSweet(Sweet sweet) {
        if (sweet == null || sweet.getId() == null) {
            throw new IllegalArgumentException("Invalid sweet");
        }
    }

    private void validateCart(Map<Long, CartItem> cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
    }
}
