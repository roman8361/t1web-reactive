package ru.kravchenko.product.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.exception.NotFoundException;
import ru.kravchenko.common.model.Status;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler()
    public Mono<ResponseEntity> handleResourceNotFoundException(NotFoundException e) {
        return Mono.just(new ResponseEntity<>(Status.statusError(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND));
    }
}
