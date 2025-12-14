package com.incubyte.sweetshop.service;

import com.incubyte.sweetshop.exception.SweetNotFoundException;
import com.incubyte.sweetshop.model.Sweet;
import com.incubyte.sweetshop.repository.SweetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SweetServiceImpl implements SweetService {

    private static final int MAX_RESTOCK_LIMIT = 1000;

    private final SweetRepository repo;

    public SweetServiceImpl(SweetRepository repo) {
        this.repo = repo;
    }

    // ===================== CRUD =====================

    @Override
    public Sweet addSweet(Sweet sweet) {
        validateName(sweet.getName());
        validatePrice(sweet.getPrice());
        return repo.save(sweet);
    }

    @Override
    public Sweet updateSweet(Long id, Sweet sweet) {
        Sweet existing = getSweetById(id);
        applyUpdates(existing, sweet);
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

        adjustStock(sweet, -quantity);
        return repo.save(sweet);
    }

    // ===================== RESTOCK =====================

    @Override
    public Sweet restockSweet(Long id, int quantity) {
        validateQuantity(quantity);
        validateRestockLimit(quantity);

        Sweet sweet = getSweetById(id);
        adjustStock(sweet, quantity);

        return repo.save(sweet);
    }

    // ===================== SEARCH =====================

    @Override
    public List<Sweet> search(String name, String category, Double min, Double max) {

        if (hasText(name)) {
            return repo.findByNameContainingIgnoreCase(name);
        }

        if (hasText(category)) {
            return repo.findByCategoryIgnoreCase(category);
        }

        if (min != null && max != null) {
            return repo.findByPriceBetween(min, max);
        }

        return repo.findAll();
    }

    // ===================== UPDATE HELPERS =====================

    private void applyUpdates(Sweet target, Sweet source) {

        if (source.getName() != null) {
            validateName(source.getName());
            target.setName(source.getName());
        }

        if (source.getCategory() != null) {
            target.setCategory(source.getCategory());
        }

        if (source.getPrice() != 0) {
            validatePrice(source.getPrice());
            target.setPrice(source.getPrice());
        }

        if (source.getQuantity() != 0) {
            target.setQuantity(source.getQuantity());
        }

        if (hasImage(source)) {
            target.setImage(source.getImage());
        }
    }

    // ===================== VALIDATIONS =====================

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
    }

    private void validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Invalid price");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid sweet name");
        }
    }

    private void validateRestockLimit(int quantity) {
        if (quantity > MAX_RESTOCK_LIMIT) {
            throw new IllegalArgumentException("Restock limit exceeded");
        }
    }

    private void validateStockAvailability(Sweet sweet, int quantity) {
        if (sweet.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock available");
        }
    }

    // ===================== UTIL =====================

    private void adjustStock(Sweet sweet, int delta) {
        sweet.setQuantity(sweet.getQuantity() + delta);
    }

    private boolean hasImage(Sweet sweet) {
        return sweet.getImage() != null && sweet.getImage().length > 0;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
