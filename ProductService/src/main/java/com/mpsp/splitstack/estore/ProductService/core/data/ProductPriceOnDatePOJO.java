package com.mpsp.splitstack.estore.ProductService.core.data;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProductPriceOnDatePOJO {


    private final String productId;
    private final LocalDate onDate;

    public ProductPriceOnDatePOJO(String productId, LocalDate onDate) {
        this.productId = productId;
        this.onDate = onDate;
    }

}
