package com.bancobogota.products.service;
import com.bancobogota.products.model.Product;

import java.util.List;
import java.util.Optional;
public interface ProductService {

    List<Product> getAll();
    Optional<Product> getById(String id);
    Product save(Product entity);
    Product update(String id, Product entity);
    void delete(String id);
}


