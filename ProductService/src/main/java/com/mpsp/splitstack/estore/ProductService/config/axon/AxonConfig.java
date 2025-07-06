package com.mpsp.splitstack.estore.ProductService.config.axon;

import com.mpsp.splitstack.estore.ProductService.command.interceptors.CreateProductCommandInterceptor;
import org.axonframework.commandhandling.CommandBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Autowired
    public void configure(CommandBus commandBus, CreateProductCommandInterceptor createProductCommandInterceptor) {
        commandBus.registerDispatchInterceptor(createProductCommandInterceptor);
    }
}
