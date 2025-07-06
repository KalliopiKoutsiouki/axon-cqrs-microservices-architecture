package com.mpsp.splitstack.estore.ProductService.command;

import com.mpsp.splitstack.estore.ProductService.core.events.ProductCreatedEvent;
import com.mpsp.splitstack.estore.ProductService.command.utils.CreateCommandValidator;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
// Axon automatically creates EventSourcingRepository<ProductAggregate>
// and saves to the Events Store all events with this aggregateId
public class ProductAggregate {

    @AggregateIdentifier //what will uniquely identify this aggregate
    private String productId;
    private String title;
    private BigDecimal price;
    private int quantity;

    // required by Axon Framework to be able to create instances of the class
    public ProductAggregate() {
    }

    // this constructor is used as a command handler method and will be invoked when the CreateProductCommand is dispathced
    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        CreateCommandValidator.validateCreateProductCommand(createProductCommand);
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
        // dispatch the event to all event handlers inside this aggregate, so that the state of this aggregate can be updated with new information (EventSourcingHandler annotated method 'on()')
        // as soon as it is called, the product created event will be dispatched and the EventSourcingHandler annotated method will be called first
        // the aggregate will be updated and the event will be scheduled for publication to other event handlers
        AggregateLifecycle.apply(productCreatedEvent);

    }

    @EventSourcingHandler
    // we avoid calling any business logic and use it only to update the aggregate
    public void on(ProductCreatedEvent productCreatedEvent){
        this.productId = productCreatedEvent.getProductId();
        this.title = productCreatedEvent.getTitle();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
    }






}
