package com.bancobogota.products.validator;

import com.bancobogota.products.constants.ErrorMessages;
import com.bancobogota.products.exception.CustomException;
import com.bancobogota.products.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

    public void validate(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new CustomException(ErrorMessages.PRODUCT_NAME_REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (product.getCategory() == null || product.getCategory().isEmpty()) {
            throw new CustomException(ErrorMessages.PRODUCT_CATEGORY_REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new CustomException(ErrorMessages.PRODUCT_PRICE_INVALID, HttpStatus.BAD_REQUEST);
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new CustomException(ErrorMessages.PRODUCT_STOCK_INVALID, HttpStatus.BAD_REQUEST);
        }
    }
}

