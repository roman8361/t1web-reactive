package ru.kravchenko.limit.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.exception.DefaultHttpException;
import ru.kravchenko.common.exception.LimitException;
import ru.kravchenko.common.model.Status;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler()
    public Mono<Status> handleResourceNotFoundException1(LimitException e) {
        return Mono.just(Status.statusError("", e.getMessage()));
    }

    @ExceptionHandler()
    public Mono<Status> handleResourceNotFoundException2(DefaultHttpException e) {
        return Mono.just(Status.statusError("", e.getMessage()));
    }
}
