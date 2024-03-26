package ru.kravchenko.payment.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.model.Status;
import ru.kravchenko.common.model.clients.ClientDto;
import ru.kravchenko.common.model.clients.ClientsDto;
import ru.kravchenko.common.model.debit.DebitDto;

import java.nio.charset.StandardCharsets;


@Slf4j
@Service
@RequiredArgsConstructor
public class WebClients extends AbstractWebClient {
    private static final String CLIENT_ID = "CLIENT_ID";
    private final WebClient productWebClient;
    @Value("${integrations.product.get-all}")
    private String getAll;

    public Mono<ClientsDto> getAllClients() {
        log.info("Отправки запроса в сервис Product для получения всех клиентов ");
        return productWebClient.get()
                .uri(getAll)
                .retrieve()
                .bodyToMono(ClientsDto.class);
    }

    public Mono<ClientDto> findClientById(Integer clientId) {
        log.info("Отправки запроса в сервис Product для получения клиента по id: {}", clientId);
        return productWebClient.get()
                .uri("")
                .header(CLIENT_ID, String.valueOf(clientId))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response
                        .createException()
                        .flatMap(it -> createException(it.getStatusCode().value(), it.getResponseBodyAsString(StandardCharsets.UTF_8)))
                )
                .bodyToMono(ClientDto.class)
                .doOnError(ex -> log.error("Клиент по id: {} не найден", clientId))
                .onErrorResume(this::handleError);
    }

    public Mono<Status> debitFunds(Integer clientId, DebitDto debitDto) {
        log.info("Отправки запроса в сервис Product для получения клиента по id: {}", clientId);
        return productWebClient.put()
                .uri("")
                .header(CLIENT_ID, String.valueOf(clientId))
                .body(Mono.just(debitDto), DebitDto.class)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response
                        .createException()
                        .flatMap(it -> createException(it.getStatusCode().value(), it.getResponseBodyAsString(StandardCharsets.UTF_8)))
                )
                .bodyToMono(Status.class)
                .doOnError(ex -> log.error("Для клиента с id: {} не удалось списание", clientId))
                .onErrorResume(this::handleError);
    }
}
