package com.mpsp.splitstack.estore.ProductService.core.errorhandling;

import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ProductsServiceErrorHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
       ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
       return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exceptions of type CommandExecutionException thrown during the execution of commands.
     * Converts the exception into a standardized error response containing a timestamp and error message.
     *
     * @param e the CommandExecutionException instance to be handled
     * @return a ResponseEntity containing the error details and HTTP status code
     */
    @ExceptionHandler(value = {CommandExecutionException.class})
    public ResponseEntity<Object> handleCommandExecutionException(CommandExecutionException e) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleExceptions(Exception e) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
