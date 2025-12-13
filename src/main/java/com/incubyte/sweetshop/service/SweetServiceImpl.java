package com.incubyte.sweetshop.service;

import com.incubyte.sweetshop.exception.SweetNotFoundException;
import com.incubyte.sweetshop.model.Sweet;
import com.incubyte.sweetshop.repository.SweetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SweetServiceImpl implements SweetService {

    private final SweetRepository repo;

    public SweetServiceImpl(SweetRepository repo) {
        this.repo = repo;
    }

    @Override
    public Sweet addSweet(Sweet sweet) {
        return repo.save(sweet);
    }

    @Override
    public Sweet updateSweet(Long id, Sweet sweet) {
        Sweet existing = getSweetById(id);

        existing.setName(sweet.getName());
        existing.setCategory(sweet.getCategory());
        existing.setPrice(sweet.getPrice());
        existing.setQuantity(sweet.getQuantity());

        if (hasImage(sweet)) {
            existing.setImage(sweet.getImage());
        }

        return repo.save(existing);
    }

    @Override
    public void deleteSweet(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Sweet getSweetById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new SweetNotFoundException(id));
    }

    @Override
    public List<Sweet> getAllSweets() {
        return repo.findAll();
    }

    // ===================== PURCHASE =====================

    @Override
    public Sweet purchaseSweet(Long id, int quantity) {
        validateQuantity(quantity);

        Sweet sweet = getSweetById(id);
        validateStockAvailability(sweet, quantity);

        sweet.setQuantity(sweet.getQuantity() - quantity);
        return repo.save(sweet);
    }

    // ===================== RESTOCK =====================

    @Override
    public Sweet restockSweet(Long id, int quantity) {
        validateQuantity(quantity);

        Sweet sweet = getSweetById(id);
        sweet.setQuantity(sweet.getQuantity() + quantity);

        return repo.save(sweet);
    }

    // ===================== SEARCH =====================

    @Override
    public List<Sweet> search(String name, String category, Double min, Double max) {

        if (isNotBlank(name)) {
            return repo.findByNameContainingIgnoreCase(name);
        }

        if (isNotBlank(category)) {
            return repo.findByCategoryIgnoreCase(category);
        }

        if (min != null && max != null) {
            return repo.findByPriceBetween(min, max);
        }

        return repo.findAll();
    }

    // ===================== VALIDATIONS =====================

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
    }

    private void validateStockAvailability(Sweet sweet, int quantity) {
        if (sweet.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock available");
        }
    }

    private boolean hasImage(Sweet sweet) {
        return sweet.getImage() != null && sweet.getImage().length > 0;
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}
