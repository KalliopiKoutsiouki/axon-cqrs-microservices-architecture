package com.mpsp.splitstack.estore.ProductService.config.axon;

import com.mpsp.splitstack.estore.ProductService.command.interceptors.ProductCommandInterceptor;
import com.mpsp.splitstack.estore.ProductService.core.errorhandling.ProductServiceEventsErrorHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    /**
     * Registers a command interceptor with the specified CommandBus. This enables interception of
     * commands before they are dispatched, allowing for validation, preprocessing, or modification
     * of the commands.
     *
     * @param commandBus the CommandBus instance to which the interceptor will be registered
     * @param productCommandInterceptor the interceptor instance that will be used to handle
     *                                        command dispatching logic
     */
    @Autowired
    public void registerCommandInterceptor(CommandBus commandBus, ProductCommandInterceptor productCommandInterceptor) {
        commandBus.registerDispatchInterceptor(productCommandInterceptor);
    }

    /**
     * Registers a custom ListenerInvocationErrorHandler for handling errors that occur during the invocation
     * of event listeners associated with a specific processing group.
     *
     * @param configurer the EventProcessingConfigurer instance used to configure event processing in Axon Framework
     */
    @Autowired
    public void registerEventErrorHandler(EventProcessingConfigurer configurer) {
        configurer.registerListenerInvocationErrorHandler("product-group", configuration -> new ProductServiceEventsErrorHandler());
    }
}
