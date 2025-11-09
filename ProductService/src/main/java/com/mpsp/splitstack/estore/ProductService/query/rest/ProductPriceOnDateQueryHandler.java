package com.mpsp.splitstack.estore.ProductService.query.rest;

import com.mpsp.splitstack.estore.ProductService.core.data.ProductPriceOnDatePOJO;
import com.mpsp.splitstack.estore.ProductService.query.ProductPriceOnDateService;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;

@Component
public class ProductPriceOnDateQueryHandler {

    private final ProductPriceOnDateService priceOnDateService;
    private final ZoneId zoneId = ZoneId.systemDefault();

    public ProductPriceOnDateQueryHandler(ProductPriceOnDateService timeTravelService) {
        this.priceOnDateService = timeTravelService;
    }

    @QueryHandler
    public BigDecimal handle(ProductPriceOnDatePOJO query) {
        Instant asOf = query.getOnDate()
                .plusDays(1)  // include entire day
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();

        return priceOnDateService.priceAsOf(query.getProductId(), asOf);
    }


}
