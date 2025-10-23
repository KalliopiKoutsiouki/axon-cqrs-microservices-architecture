package com.mpsp.splitstack.estore.ProductService.command.interceptors;

import com.mpsp.splitstack.estore.ProductService.command.CreateProductCommand;
import com.mpsp.splitstack.estore.ProductService.command.UpdateProductPriceCommand;
import com.mpsp.splitstack.estore.ProductService.command.utils.ProductCommandValidator;
import com.mpsp.splitstack.estore.ProductService.core.data.ProductLookUpEntity;
import com.mpsp.splitstack.estore.ProductService.core.data.ProductLookUpRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class ProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookUpRepository productLookUpRepository;

    public ProductCommandInterceptor(ProductLookUpRepository productLookUpRepository) {
        this.productLookUpRepository = productLookUpRepository;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCommandInterceptor.class);

    @Nonnull
    @Override
    // returns a function that accepts 2 args and produces a result
    // according to the documentation this handle method needs to return the function that processes messages
    // based on their position in the list
    // Integer -> command index
    // CommandMessage -> result of the function
    // IMPORTANT: This interceptor will intercept ANY commands that are dispatched, not only the create one for example
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            LOGGER.info("Intercepted command: {}", command.getPayloadType());
            if (CreateProductCommand.class.equals(command.getPayloadType())) {
                assertProductDoesNotExist(command);
            }
            if (UpdateProductPriceCommand.class.equals(command.getPayloadType())) {
                assertProductExists(command);
            }
            return command;
        };
    }

    private void assertProductDoesNotExist(CommandMessage<?> command) {
        ProductCommandValidator.validateCreateProductCommand((CreateProductCommand) command.getPayload());
        ProductLookUpEntity productLookUpEntity = productLookUpRepository.findByProductIdOrTitle(((CreateProductCommand) command.getPayload()).getProductId(), ((CreateProductCommand) command.getPayload()).getTitle());
        if (productLookUpEntity != null) {
            throw new IllegalStateException("Product already exists");
        }
    }

    private void assertProductExists(CommandMessage<?> command) {
        ProductCommandValidator.validateUpdateProductPriceCommand((UpdateProductPriceCommand) command.getPayload());
        if (!productLookUpRepository.existsById(((UpdateProductPriceCommand) command.getPayload()).getProductId())) {
            throw new IllegalStateException("Product does not exist");
        }
    }
}
