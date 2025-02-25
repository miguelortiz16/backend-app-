package com.bancobogota.products.service.impl;

import com.bancobogota.products.constants.ErrorMessages;
import com.bancobogota.products.exception.CustomException;
import com.bancobogota.products.model.Product;
import com.bancobogota.products.repository.ProductRepository;
import com.bancobogota.products.service.ProductService;
import com.bancobogota.products.validator.ProductValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductValidator validator;

    public ProductServiceImpl(ProductRepository repository, ProductValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Product> getById(String id) {
        return Optional.ofNullable(repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorMessages.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND)));
    }

    @Override
    public Product save(Product product) {
        validator.validate(product);
        return repository.save(product);
    }

    @Override
    public Product update(String id, Product product) {
        return repository.findById(id)
                .map(existing -> {
                    validator.validate(product);
                    existing.setName(product.getName());
                    existing.setCategory(product.getCategory());
                    existing.setPrice(product.getPrice());
                    existing.setStock(product.getStock());
                    return repository.save(existing);
                }).orElseThrow(() -> new CustomException(ErrorMessages.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new CustomException(ErrorMessages.PRODUCT_DELETE_ERROR, HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }
}

