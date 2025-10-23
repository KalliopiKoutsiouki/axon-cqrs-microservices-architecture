package com.mpsp.splitstack.estore.ProductService.command.interceptors;

import com.mpsp.splitstack.estore.ProductService.command.CreateProductCommand;
import com.mpsp.splitstack.estore.ProductService.command.utils.CreateCommandValidator;
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
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookUpRepository productLookUpRepository;

    public CreateProductCommandInterceptor(ProductLookUpRepository productLookUpRepository) {
        this.productLookUpRepository = productLookUpRepository;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);

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
            if (CreateProductCommand.class.equals(command.getPayloadType())){
                CreateCommandValidator.validateCreateProductCommand((CreateProductCommand) command.getPayload());
                ProductLookUpEntity productLookUpEntity =productLookUpRepository.findByProductIdOrTitle(((CreateProductCommand) command.getPayload()).getProductId(), ((CreateProductCommand) command.getPayload()).getTitle());
                if(productLookUpEntity != null){ throw new IllegalStateException("Product already exists");}
            }
            return command;
        };
    }
}
