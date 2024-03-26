package ru.kravchenko.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.model.clients.ClientDto;
import ru.kravchenko.common.model.clients.ClientsDto;
import ru.kravchenko.payment.web.WebClients;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
public class ClientController {
    private final WebClients clientServiceWeb;

    @GetMapping()
    public Mono<ClientDto> findClientById(@RequestHeader("CLIENT_ID") Integer clientId) {
        log.info("Запрос на клиента по id: {}", clientId);
        return clientServiceWeb.findClientById(clientId);
    }

    @GetMapping("/all")
    public Mono<ClientsDto> getAllClient() {
        log.info("Запрос на получение всех клиентов");
        return clientServiceWeb.getAllClients();
    }
}
