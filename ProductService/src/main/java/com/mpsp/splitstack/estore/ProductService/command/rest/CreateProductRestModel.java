package com.mpsp.splitstack.estore.ProductService.command.rest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRestModel {

    @NotBlank(message = "Title cannot be null or blank")
    private String title;
    @Min(value=1, message = "Price must be greater than 1")
    private BigDecimal price;
    @Max(value = 5, message = "Quantity must be less than 5")
    @Min(value=1, message = "Quantity must be greater than 1")
    private int quantity;
}
