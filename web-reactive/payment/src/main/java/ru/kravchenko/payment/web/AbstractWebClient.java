package ru.kravchenko.payment.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.exception.DefaultHttpException;
import ru.kravchenko.common.exception.NotFoundException;
import ru.kravchenko.common.model.Status;

@Slf4j
@Service
public abstract class AbstractWebClient {
    protected <T> Mono<T> handleError(Throwable error) {
        if (error instanceof NotFoundException) {
            return Mono.error(error);
        }
        return Mono.error(new DefaultHttpException(error.getMessage(), error.getCause()));
    }

    protected Mono<NotFoundException> createException(int statusCode, String responseBody) {
        log.info("Статус код такой: {}", statusCode);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            final Status statusResult = objectMapper.readValue(responseBody, Status.class);
            return Mono.error(new NotFoundException(statusResult.description(), statusResult.code()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
