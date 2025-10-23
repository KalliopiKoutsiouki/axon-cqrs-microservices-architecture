package com.mpsp.splitstack.estore.ProductService.command;

import com.mpsp.splitstack.estore.ProductService.command.utils.ProductCommandValidator;
import com.mpsp.splitstack.estore.ProductService.core.events.ProductCreatedEvent;
import com.mpsp.splitstack.estore.ProductService.core.events.ProductPriceUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * Aggregate root for product-related operations in the event-sourcing architecture.
 * This class handles product creation commands and maintains product state through event sourcing.
 *
 * <p>The aggregate is automatically managed by Axon Framework which creates an
 * EventSourcingRepository<ProductAggregate> and saves all events with this aggregateId
 * to the Events Store.</p>
 */

@Aggregate
public class ProductAggregate {

    /**
     * Unique identifier for this aggregate instance.
     * Used by Axon Framework to track and store events related to this specific product.
     */

    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private int quantity;

    /**
     * Default constructor required by Axon Framework.
     * Used during aggregate reconstruction from events.
     */
    public ProductAggregate() {
    }

    /**
     * Command handler constructor for product creation.
     * This constructor serves as a command handler method and is invoked when
     * a CreateProductCommand is dispatched.
     *
     * <p>The flow of operations is as follows:</p>
     * <ol>
     *   <li>Validates the incoming command</li>
     *   <li>Creates a new ProductCreatedEvent</li>
     *   <li>Copies command properties to the event</li>
     *   <li>Applies the event using AggregateLifecycle.apply()</li>
     * </ol>
     *
     * <p>Important considerations:</p>
     * <ul>
     *   <li>If an exception occurs after apply() but before completion, the event
     *       will not be persisted to the event store</li>
     *   <li>Failed operations will throw a CommandExecutionException</li>
     *   <li>The entire operation is atomic - either succeeds completely or rolls back</li>
     * </ul>
     *
     * @param createProductCommand The command containing new product details
     * @throws IllegalArgumentException if the command validation fails
     */

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        ProductCommandValidator.validateCreateProductCommand(createProductCommand);
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
        /*
         * Dispatch the event using AggregateLifecycle.apply()
         *
         * This operation triggers the following sequence:
         * 1. The ProductCreatedEvent is first routed to @EventSourcingHandler methods within this aggregate
         *    to update the aggregate's state (the 'on()' method in this case)
         *
         * 2. After the aggregate state is updated, the event is scheduled for publication
         *    to external event handlers (like ProductEventsHandler)
         *
         * Important:
         * - If an exception occurs after apply() but before the command handler completes,
         *   the event will not be persisted to the event store
         * - The operation will fail with a CommandExecutionException which is handled in the @ControllerAdvice class,
         *   that is why it is propagated to the controller
         * - The transaction is atomic - either the entire operation succeeds or it's rolled back
         */
        AggregateLifecycle.apply(productCreatedEvent);
        //if(true) throw new Exception("Error");


    }

    // Exception that is thrown inside the CommandHandler (CommandExecutionException) method - the eventsourcinghandler will not proceed - the product will not be persisted in the event store
    @EventSourcingHandler
    // we avoid calling any business logic and use it only to update the aggregate
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.productId = productCreatedEvent.getProductId();
        this.title = productCreatedEvent.getTitle();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
    }

    @CommandHandler
    public void on(UpdateProductPriceCommand updateProductPriceCommand) {
        ProductCommandValidator.validateUpdateProductPriceCommand(updateProductPriceCommand);
        ProductPriceUpdatedEvent productPriceUpdatedEvent = new ProductPriceUpdatedEvent();
        BeanUtils.copyProperties(updateProductPriceCommand, productPriceUpdatedEvent);
        AggregateLifecycle.apply(productPriceUpdatedEvent);
    }

    /**
     * Called when a ProductPriceUpdatedEvent is received by this aggregate.
     * Updates the price of the product with the value provided in the event.
     *
     * @param productPriceUpdatedEvent the event received by this aggregate
     */
    @EventSourcingHandler
    public void on(ProductPriceUpdatedEvent productPriceUpdatedEvent) {
        this.price = productPriceUpdatedEvent.getPrice();
    }

}
