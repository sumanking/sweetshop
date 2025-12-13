package com.incubyte.sweetshop.controller;

import com.incubyte.sweetshop.model.CartItem;
import com.incubyte.sweetshop.model.Sweet;
import com.incubyte.sweetshop.service.CartService;
import com.incubyte.sweetshop.service.SweetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/customer")
@SessionAttributes("cart")
public class SweetController {

    private final SweetService sweetService;
    private final CartService cartService;

    public SweetController(SweetService sweetService, CartService cartService) {
        this.sweetService = sweetService;
        this.cartService = cartService;
    }

    /* ---------- CART INIT ---------- */
    @ModelAttribute("cart")
    public Map<Long, CartItem> cart() {
        return new HashMap<>();
    }

    /* ---------- CUSTOMER PAGE + SEARCH ---------- */
    @GetMapping
    public String customer(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            Model model) {

        model.addAttribute("sweets",
                sweetService.search(name, category, min, max));

        return "customer";
    }

    /* ---------- ADD TO CART ---------- */
    @PostMapping("/add-to-cart")
    public String addToCart(
            @RequestParam Long sweetId,
            @RequestParam int quantity,
            @ModelAttribute("cart") Map<Long, CartItem> cart) {

        Sweet sweet = sweetService.getSweetById(sweetId);
        cartService.addToCart(cart, sweet, quantity);

        return "redirect:/customer";
    }

    /* ---------- VIEW CART ---------- */
    @GetMapping("/cart")
    public String viewCart(
            @ModelAttribute("cart") Map<Long, CartItem> cart,
            Model model) {

        model.addAttribute("items", cart.values());
        model.addAttribute("total", cartService.calculateTotal(cart));

        return "cart";
    }

    /* ---------- CHECKOUT ---------- */
    @PostMapping("/checkout")
    public String checkout(
            @ModelAttribute("cart") Map<Long, CartItem> cart,
            Model model,
            SessionStatus status) {

        double totalAmount = 0;

        for (CartItem item : cart.values()) {
            sweetService.purchaseSweet(
                    item.getSweet().getId(),
                    item.getQuantity()
            );
            totalAmount += item.getTotalPrice();
        }

        status.setComplete(); // clear cart

        model.addAttribute("amount", totalAmount);
        return "success";
    }
}
