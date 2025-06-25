package com.mpsp.splitstack.estore.ProductService.rest;

import com.mpsp.splitstack.estore.ProductService.command.CreateProductCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final Environment env;
    private final CommandGateway commandGateway;

    public ProductsController(Environment env, CommandGateway commandGateway) {
        this.env = env;
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createProduct(@RequestBody CreateProductRestModel restModel){
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .title(restModel.getTitle())
                .price(restModel.getPrice())
                .quantity(restModel.getQuantity())
                .productId(UUID.randomUUID().toString())
                .build();
        String returnValue;
        try {
            returnValue = commandGateway.sendAndWait(createProductCommand); //returns a completable object immediately
        } catch (Exception e) {
            returnValue = e.getMessage();
        }

        return "HTTP POST Handled: " + returnValue;
    }

    @GetMapping
    public String getProduct(){
        return "HTTP GET Handled at port: " + env.getProperty("local.server.port");
    }

    @PutMapping
    public String updateProduct(){
        return "HTTP PUT Handled at port: " + env.getProperty("local.server.port");
    }

    @DeleteMapping
    public String deleteProduct(){
        return "HTTP DELETE Handled at port: " + env.getProperty("local.server.port");
    }
}
