package com.incubyte.sweetshop.service;

import com.incubyte.sweetshop.model.CartItem;
import com.incubyte.sweetshop.model.Sweet;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CartService {

    public void addToCart(Map<Long, CartItem> cart, Sweet sweet, int quantity) {
        validateQuantity(quantity);

        int alreadyInCart = getCurrentCartQuantity(cart, sweet);
        validateStockLimit(sweet, alreadyInCart + quantity);

        cart.computeIfAbsent(sweet.getId(), id -> new CartItem(sweet, 0))
            .addQuantity(quantity);
    }

    public double calculateTotal(Map<Long, CartItem> cart) {
        return cart.values()
                .stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    // ===== helpers =====

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
    }

    private int getCurrentCartQuantity(Map<Long, CartItem> cart, Sweet sweet) {
        return cart.containsKey(sweet.getId())
                ? cart.get(sweet.getId()).getQuantity()
                : 0;
    }

    private void validateStockLimit(Sweet sweet, int requestedTotal) {
        if (requestedTotal > sweet.getQuantity()) {
            throw new IllegalArgumentException("Cannot add more than available stock");
        }
    }
}
