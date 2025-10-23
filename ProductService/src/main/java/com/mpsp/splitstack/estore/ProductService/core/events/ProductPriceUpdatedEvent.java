package com.mpsp.splitstack.estore.ProductService.core.events;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductPriceUpdatedEvent {

    private String productId;
    private BigDecimal price;
}
