package com.bancobogota.products.repository;

import com.bancobogota.products.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}

