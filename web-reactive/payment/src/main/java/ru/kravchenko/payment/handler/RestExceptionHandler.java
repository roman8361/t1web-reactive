package ru.kravchenko.payment.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.exception.DefaultHttpException;
import ru.kravchenko.common.exception.NotFoundException;
import ru.kravchenko.common.model.Status;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler()
    public Mono<ResponseEntity> handleResourceNotFoundException(NotFoundException e) {
        return Mono.just(new ResponseEntity<>(Status.statusError(String.valueOf(e.getCode()), e.getMessage()), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler()
    public Mono<ResponseEntity> handleDefaultHttpException(DefaultHttpException e) {
        return Mono.just(new ResponseEntity<>(Status.statusError(String.valueOf(e.getCode()), e.getMessage()), HttpStatus.BAD_REQUEST));
    }
}
