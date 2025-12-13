package com.incubyte.sweetshop.controller;

import com.incubyte.sweetshop.model.Sweet;
import com.incubyte.sweetshop.service.SweetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class InventoryController {

    private final SweetService sweetService;

    public InventoryController(SweetService sweetService) {
        this.sweetService = sweetService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("sweets", sweetService.getAllSweets());
        model.addAttribute("sweet", new Sweet());
        return "admin";
    }

    @PostMapping("/add")
    public String addSweet(@ModelAttribute Sweet sweet) throws IOException {

        if (sweet.getImageFile() != null && !sweet.getImageFile().isEmpty()) {
            sweet.setImage(sweet.getImageFile().getBytes());
        }

        sweetService.addSweet(sweet);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editSweet(@PathVariable Long id, Model model) {
        model.addAttribute("sweet", sweetService.getSweetById(id));
        return "edit-sweet";
    }

    @PostMapping("/update/{id}")
    public String updateSweet(
            @PathVariable Long id,
            @ModelAttribute Sweet sweet
    ) throws IOException {

        if (sweet.getImageFile() != null && !sweet.getImageFile().isEmpty()) {
            sweet.setImage(sweet.getImageFile().getBytes());
        }

        sweetService.updateSweet(id, sweet);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteSweet(@PathVariable Long id) {
        sweetService.deleteSweet(id);
        return "redirect:/admin";
    }
}
