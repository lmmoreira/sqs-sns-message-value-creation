package br.com.ccrs.logistics.fleet.order.acceptance.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import br.com.ccrs.logistics.fleet.order.acceptance.exception.OrderAcceptanceException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class OrderAcceptanceAdvice extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value=BAD_REQUEST)
    protected void handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        
    }
    
    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<Void> handleHttpClientErrorException(HttpClientErrorException exception) {
        if (NOT_FOUND.equals(exception.getStatusCode())) {
            return ResponseEntity.notFound().build();
        }
        return handleException(exception);
    }
    
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Void> handleException(Exception exception) {
        ResponseStatus status = AnnotatedElementUtils.findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        
        if (status != null) {
            logger.error(exception);
            return ResponseEntity.status(status.value()).build();
        }
        
        logger.error("Uncaught exception: ", exception);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(OrderAcceptanceException.class)
    protected ResponseEntity<Void> handleOrderAcceptanceException(OrderAcceptanceException exception) {
        return handleException(exception);
    }
    
    @RestController
    public static class ErrorResource implements ErrorController {

        private static final String PATH = "/error";

        @RequestMapping(value=PATH)
        public void handleError() {
            
        }

        @Override
        public String getErrorPath() {
            return PATH;
        }
    }
}