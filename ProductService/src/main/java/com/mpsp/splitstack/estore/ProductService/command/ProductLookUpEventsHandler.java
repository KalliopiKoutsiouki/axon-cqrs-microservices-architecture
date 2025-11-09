package com.mpsp.splitstack.estore.ProductService.command;

import com.mpsp.splitstack.estore.ProductService.core.data.ProductLookUpEntity;
import com.mpsp.splitstack.estore.ProductService.core.data.ProductLookUpRepository;
import com.mpsp.splitstack.estore.ProductService.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
//groups handlers if they are in different packages(?) - Axon framework will create its own tracking processor and use a special tracking token
// which it will use to avoid multiple processing of the same event in different threads and nodes
// so by grouping the 2 handlers (the one in the query package also) in the same logical group we make sure that the event
// is handled once by the same thread
public class ProductLookUpEventsHandler {

    private final ProductLookUpRepository productLookUpRepository;

    public ProductLookUpEventsHandler(ProductLookUpRepository productLookUpRepository) {
        this.productLookUpRepository = productLookUpRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event){
        ProductLookUpEntity productLookUpEntity = new ProductLookUpEntity(event.getProductId(), event.getTitle());
        productLookUpRepository.save(productLookUpEntity);
    }

    @ResetHandler
    public void on() {
        productLookUpRepository.deleteAll();
    }


}
