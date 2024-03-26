package ru.kravchenko.payment.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.model.Status;
import ru.kravchenko.common.model.debit.DebitDto;
import ru.kravchenko.common.model.limit.ClientLimitDto;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebLimit extends AbstractWebClient {
    private static final String CLIENT_ID = "CLIENT_ID";
    private final WebClient limitWebClient;

    public Mono<Status> checkClientLimit(Integer clientId, ClientLimitDto clientLimitDto) {
        log.info("Отправки запроса в сервис Limit для клиента по id: {}", clientId);
        return limitWebClient.post()
                .uri("")
                .header(CLIENT_ID, String.valueOf(clientId))
                .body(Mono.just(clientLimitDto), ClientLimitDto.class)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response
                        .createException()
                        .flatMap(it -> createException(it.getStatusCode().value(), it.getResponseBodyAsString(StandardCharsets.UTF_8)))
                )
                .bodyToMono(Status.class)
                .doOnError(ex -> log.error("Клиент по id: {} не найден", clientId))
                .onErrorResume(this::handleError);
    }

    public Mono<Status> debitLimit(Integer clientId, DebitDto debitDto) {
        log.info("Отправки запроса на списание лимита в сервис Limit для клиента по id: {}", clientId);
        final var clientLimitDto = new ClientLimitDto(debitDto.sum());
        return limitWebClient.put()
                .uri("")
                .header(CLIENT_ID, String.valueOf(clientId))
                .body(Mono.just(clientLimitDto), ClientLimitDto.class)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response
                        .createException()
                        .flatMap(it -> createException(it.getStatusCode().value(), it.getResponseBodyAsString(StandardCharsets.UTF_8)))
                )
                .bodyToMono(Status.class)
                .doOnError(ex -> log.error("Ошибка списания лимита для клиента: {}", clientId))
                .onErrorResume(this::handleError);
    }
}
