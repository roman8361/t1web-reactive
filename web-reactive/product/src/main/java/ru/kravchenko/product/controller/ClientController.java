package ru.kravchenko.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.model.clients.ClientDto;
import ru.kravchenko.common.model.clients.ClientsDto;
import ru.kravchenko.product.service.ClientService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/all")
    public Mono<ClientsDto> getAllClient() {
        log.info("Запрос на получение всех клиентов от сервиса Payment");
        return clientService.getAllClient();
    }

    @GetMapping()
    public Mono<ClientDto> findUserById(@RequestHeader("CLIENT_ID") Integer clientId) {
        log.info("Запрос на получение всех клиента от сервиса Payment по id: {}", clientId);
        return clientService.findById(clientId);
    }
}
