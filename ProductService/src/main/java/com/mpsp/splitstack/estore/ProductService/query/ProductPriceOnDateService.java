package com.mpsp.splitstack.estore.ProductService.query;

import com.mpsp.splitstack.estore.ProductService.core.events.ProductCreatedEvent;
import com.mpsp.splitstack.estore.ProductService.core.events.ProductPriceUpdatedEvent;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.DomainEventStream;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class ProductPriceOnDateService {

    private final EventStore eventStore;

    public ProductPriceOnDateService(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public BigDecimal priceAsOf(String productId, Instant asOf) {
        DomainEventStream stream;
        try {
            stream = eventStore.readEvents(productId);
        } catch (AggregateNotFoundException ex) {
            // no such aggregate / no events
            return null;
        }

        BigDecimal price = null;

        while (stream.hasNext()) {
            DomainEventMessage<?> message = stream.next();

            // stop once we pass the requested instant
            if (message.getTimestamp().isAfter(asOf)) {
                break;
            }

            Object payload = message.getPayload();
            if (payload instanceof ProductCreatedEvent event) {
                price = event.getPrice();
            } else if (payload instanceof ProductPriceUpdatedEvent event) {
                price = event.getPrice();
            }
        }

        return price;
    }
}
