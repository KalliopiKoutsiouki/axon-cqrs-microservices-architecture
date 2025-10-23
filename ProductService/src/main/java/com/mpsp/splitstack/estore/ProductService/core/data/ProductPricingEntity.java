package com.mpsp.splitstack.estore.ProductService.core.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PRODUCT_PRICING")
public class ProductPricingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private BigDecimal price;
}
