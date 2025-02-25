package com.bancobogota.products.constants;

public class ErrorMessages {
    public static final String PRODUCT_NOT_FOUND = "Producto no encontrado";
    public static final String PRODUCT_NAME_REQUIRED = "El nombre del producto es obligatorio";
    public static final String PRODUCT_CATEGORY_REQUIRED = "La categoría del producto es obligatoria";
    public static final String PRODUCT_PRICE_INVALID = "El precio debe ser mayor a 0";
    public static final String PRODUCT_STOCK_INVALID = "El stock no puede ser negativo";
    public static final String PRODUCT_DELETE_ERROR = "No se puede eliminar, el producto no existe";

    private ErrorMessages() {
    }
}
