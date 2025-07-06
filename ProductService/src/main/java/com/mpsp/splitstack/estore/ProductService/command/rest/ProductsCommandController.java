package com.mpsp.splitstack.estore.ProductService.command.rest;

import com.mpsp.splitstack.estore.ProductService.command.CreateProductCommand;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductRestModel restModel) {
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .title(restModel.getTitle())
                .price(restModel.getPrice())
                .quantity(restModel.getQuantity())
                .productId(UUID.randomUUID().toString())
                .build();
        String returnValue;
        try {
            //dispatches the command to the Command Bus - returns a completable object immediately
            //the command bus finds the responsible @CommandHandler
            returnValue = commandGateway.sendAndWait(createProductCommand);
        } catch (Exception e) {
            returnValue = e.getMessage();
        }

        return "HTTP POST Handled: " + returnValue;
    }
}

