package com.mpsp.splitstack.estore.ProductService.command.rest.controller;

import com.mpsp.splitstack.estore.ProductService.command.CreateProductCommand;
import com.mpsp.splitstack.estore.ProductService.command.UpdateProductPriceCommand;
import com.mpsp.splitstack.estore.ProductService.command.rest.model.CreateProductRestModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductsCommandController {

    private final Environment env;
    private final CommandGateway commandGateway;

    public ProductsCommandController(Environment env, CommandGateway commandGateway) {
        this.env = env;
        this.commandGateway = commandGateway;
    }

    @PostMapping("/create")
    public String createProduct(@Valid @RequestBody CreateProductRestModel restModel) {
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .title(restModel.getTitle())
                .price(restModel.getPrice())
                .quantity(restModel.getQuantity())
                .productId(UUID.randomUUID().toString())
                .build();
        String returnValue = commandGateway.sendAndWait(createProductCommand); //dispatches the command to the command bus
        return "HTTP POST Handled: " + returnValue;
    }

    @PutMapping("/{productId}/price")
    public String updateProductPrice(
            @PathVariable String productId,
            @RequestParam @Positive(message = "Price must be greater than zero") BigDecimal price) {
        UpdateProductPriceCommand command = UpdateProductPriceCommand.builder()
                .productId(productId)
                .price(price)
                .build();
        String returnValue = commandGateway.sendAndWait(command);
        return "HTTP POST Handled: " + returnValue;
    }
}

