package com.incubyte.sweetshop.service;

import com.incubyte.sweetshop.exception.SweetNotFoundException;
import com.incubyte.sweetshop.model.Sweet;
import com.incubyte.sweetshop.repository.SweetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SweetServiceTest {

    @Mock
    private SweetRepository sweetRepository;

    @InjectMocks
    private SweetServiceImpl sweetService;

    private Sweet sweet;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        sweet = new Sweet();
        sweet.setId(1L);
        sweet.setName("Rasgulla");
        sweet.setCategory("Milk");
        sweet.setPrice(20.0);
        sweet.setQuantity(10);
    }

    // ===================== ADD SWEET =====================

    @Test
    void shouldAddSweet() {
        when(sweetRepository.save(sweet)).thenReturn(sweet);

        Sweet result = sweetService.addSweet(sweet);

        assertNotNull(result);
        assertEquals("Rasgulla", result.getName());
        verify(sweetRepository).save(sweet);
    }

    // ===================== GET ALL =====================

    @Test
    void shouldReturnAllSweets() {
        when(sweetRepository.findAll()).thenReturn(List.of(sweet));

        List<Sweet> sweets = sweetService.getAllSweets();

        assertEquals(1, sweets.size());
        assertEquals("Rasgulla", sweets.get(0).getName());
    }

    // ===================== PURCHASE =====================

    @Test
    void shouldReduceQuantityWhenPurchased() {
        when(sweetRepository.findById(1L)).thenReturn(Optional.of(sweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(sweet);

        sweetService.purchaseSweet(1L, 3);

        assertEquals(7, sweet.getQuantity());
        verify(sweetRepository).save(sweet);
    }

    @Test
    void shouldThrowExceptionIfNotEnoughStock() {
        when(sweetRepository.findById(1L)).thenReturn(Optional.of(sweet));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> sweetService.purchaseSweet(1L, 50)
        );

        assertEquals("Not enough stock available", ex.getMessage());
    }

    // ===================== TDD CYCLE 2 =====================

    @Test
    void shouldThrowExceptionIfQuantityIsZero() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> sweetService.purchaseSweet(1L, 0)
        );

        assertEquals("Invalid quantity", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionIfQuantityIsNegative() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> sweetService.purchaseSweet(1L, -5)
        );

        assertEquals("Invalid quantity", ex.getMessage());
    }

    // ===================== TDD CYCLE 3 =====================

    @Test
    void shouldThrowExceptionWhenPurchasingNonExistingSweet() {
        when(sweetRepository.findById(99L)).thenReturn(Optional.empty());

        SweetNotFoundException ex = assertThrows(
                SweetNotFoundException.class,
                () -> sweetService.purchaseSweet(99L, 2)
        );

        assertTrue(ex.getMessage().contains("Sweet not found"));
    }
}
