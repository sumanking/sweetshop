package com.incubyte.sweetshop.service;

import com.incubyte.sweetshop.model.Sweet;
import java.util.List;

public interface SweetService {

    Sweet addSweet(Sweet sweet);
    Sweet updateSweet(Long id, Sweet sweet);
    void deleteSweet(Long id);

    Sweet getSweetById(Long id);
    List<Sweet> getAllSweets();

    Sweet purchaseSweet(Long id, int quantity);
    Sweet restockSweet(Long id, int quantity);

    List<Sweet> search(String name, String category, Double min, Double max);
}
