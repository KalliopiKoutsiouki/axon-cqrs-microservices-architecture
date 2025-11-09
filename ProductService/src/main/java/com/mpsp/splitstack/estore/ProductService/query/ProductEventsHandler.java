package com.mpsp.splitstack.estore.ProductService.query;

import com.mpsp.splitstack.estore.ProductService.core.data.ProductEntity;
import com.mpsp.splitstack.estore.ProductService.core.data.ProductsRepository;
import com.mpsp.splitstack.estore.ProductService.core.events.ProductCreatedEvent;
import com.mpsp.splitstack.estore.ProductService.core.events.ProductPriceUpdatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {

    private final ProductsRepository productsRepository;

    public ProductEventsHandler(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    /**
      Exception Handling Configuration

      For proper exception propagation in Axon Event Handlers, three components are required:

      1. @ExceptionHandler Methods
         - Must be defined in the same class as the @EventHandler
         - Act as the first line of exception interception
         - Can handle specific exception types differently

      2. ListenerInvocationErrorHandler
         - Implemented by ProductServiceEventsErrorHandler
         - Registered (check Axon Config) for the processing group ("product-group")
         - Handles exceptions after @ExceptionHandler processing
         - Controls whether exceptions are propagated or suppressed

      3. Controller Exception Handlers
         - Final layer of exception handling
         - Converts exceptions to appropriate HTTP responses

     Note: Simply throwing exceptions in @EventHandler methods without this setup
      will not properly propagate errors to the controller layer.
     */
    @ExceptionHandler(resultType = Exception.class)
    public void handeOtherExceptions(Exception exception) throws Exception {
       throw exception;
    }


    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void hande(IllegalArgumentException exception){
        //log error message
    }

    /**
      Exception Handling in Event Processing

      When an exception is thrown here, the following chain of events occurs:

      1. Local Exception Handling:
         - First caught by @ExceptionHandler methods in this class
         - Then propagated to the ProductServiceEventsErrorHandler (ListenerInvocationErrorHandler)
         - Finally reaches the controller's exception handlers

      2. Transaction Management:
         - All operations within the same processing group ("product-group") are rolled back
         - This includes database operations from other EventHandlers (e.g., ProductLookUpEventsHandler)
         - The entire transaction is treated as atomic

      3. Event Store Impact:
         - No events are persisted to the Event Store
         - The ProductCreatedEvent itself is not stored
         - Maintains data consistency across the event-sourcing system

      This behavior ensures that either all operations succeed or none do,
      preventing partial or inconsistent updates across the system.
     */

    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);
        productsRepository.save(productEntity);
    }

    @EventHandler
    public void on(ProductPriceUpdatedEvent event) {
        productsRepository.findById(event.getProductId()).ifPresent(product -> {
            product.setPrice(event.getPrice());
            productsRepository.save(product);
        });
    }

    @ResetHandler
    public void on() {
        productsRepository.deleteAll();
    }
}
