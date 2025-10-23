package com.mpsp.splitstack.estore.ProductService.command.utils;

import com.mpsp.splitstack.estore.ProductService.command.CreateProductCommand;
import com.mpsp.splitstack.estore.ProductService.command.UpdateProductPriceCommand;

import java.math.BigDecimal;

public class ProductCommandValidator {

    private static final String PRICE_ERROR_MESSAGE = "Price must be greater than zero";
    private static final String TITLE_ERROR_MESSAGE = "Title cannot be null or blank";

    public static void validateCreateProductCommand(CreateProductCommand command) {
        validatePrice(command.getPrice());
        validateTitle(command.getTitle());
    }

    public static void validateUpdateProductPriceCommand(UpdateProductPriceCommand command) {
        validatePrice(command.getPrice());
    }

    private static void validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(PRICE_ERROR_MESSAGE);
        }
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(TITLE_ERROR_MESSAGE);
        }
    }}
