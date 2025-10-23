package com.mpsp.splitstack.estore.ProductService.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Builder
@Data
public class UpdateProductPriceCommand {

    @TargetAggregateIdentifier
    private final String productId;
    private final BigDecimal price;
}
