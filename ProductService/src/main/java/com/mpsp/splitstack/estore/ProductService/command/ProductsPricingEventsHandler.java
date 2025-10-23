package com.mpsp.splitstack.estore.ProductService.command;

import com.mpsp.splitstack.estore.ProductService.core.data.ProductLookUpEntity;
import com.mpsp.splitstack.estore.ProductService.core.data.ProductPricingEntity;
import com.mpsp.splitstack.estore.ProductService.core.data.ProductPricingRepository;
import com.mpsp.splitstack.estore.ProductService.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ProcessingGroup("product-group")
public class ProductsPricingEventsHandler {

    private final ProductPricingRepository productPricingRepository;
    private static final BigDecimal SURCHARGE_RATE = new BigDecimal("0.015");

    public ProductsPricingEventsHandler(ProductPricingRepository productPricingRepository) {
        this.productPricingRepository = productPricingRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event){
        BigDecimal priceAfterSurcharge = event.getPrice().multiply(BigDecimal.ONE.add(SURCHARGE_RATE));
        ProductPricingEntity productPricingEntity = new ProductPricingEntity(event.getProductId(),  priceAfterSurcharge);
        productPricingRepository.save(productPricingEntity);
    }
}
