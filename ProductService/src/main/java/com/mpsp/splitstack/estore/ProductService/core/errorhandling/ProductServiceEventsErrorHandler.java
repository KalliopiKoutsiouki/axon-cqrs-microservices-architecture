package com.mpsp.splitstack.estore.ProductService.core.errorhandling;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;

import javax.annotation.Nonnull;

/**
 * A custom error handler for Axon framework event listener invocation errors.
 * This class implements the ListenerInvocationErrorHandler interface and provides
 * a mechanism for handling exceptions that occur during the processing of event messages.
 *
 * When an exception is thrown during the invocation of an event handler, this implementation
 * rethrows the exception to indicate that the error should propagate further.
 *
 * The implementation needs to be registered with the EventProcessingConfigurer of Axon Framework (AxonConfig)
 */
public class ProductServiceEventsErrorHandler implements ListenerInvocationErrorHandler {


    @Override
    public void onError(@Nonnull Exception exception, @Nonnull EventMessage<?> event, @Nonnull EventMessageHandler eventHandler) throws Exception {
        throw exception;
    }
}
