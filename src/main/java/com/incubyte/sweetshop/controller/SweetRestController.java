package com.incubyte.sweetshop.controller;

import com.incubyte.sweetshop.model.Sweet;
import com.incubyte.sweetshop.service.SweetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sweets")
public class SweetRestController {

    private final SweetService sweetService;

    public SweetRestController(SweetService sweetService) {
        this.sweetService = sweetService;
    }

    // 🟢 GREEN — REST endpoint
    @GetMapping
    public List<Sweet> getAllSweets() {
        return sweetService.getAllSweets();
    }
}
