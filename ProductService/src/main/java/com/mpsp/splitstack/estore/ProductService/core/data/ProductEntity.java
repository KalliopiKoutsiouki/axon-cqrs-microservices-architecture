package com.mpsp.splitstack.estore.ProductService.core.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "PRODUCTS")
public class ProductEntity {

    @Id
    private String productId;
    private String title;
    private BigDecimal price;
    private int quantity;
}
