package ru.kravchenko.limit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.model.Status;
import ru.kravchenko.common.model.limit.ClientLimitDto;
import ru.kravchenko.limit.service.LimitService;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/limit")
public class LimitController {
    private final LimitService limitService;

    @PostMapping()
    public Mono<Status> checkLimit(@RequestHeader("CLIENT_ID") Integer clientId,
                                   @RequestBody ClientLimitDto clientLimitDto) {
        log.info("Запрос на проверку лимита у клиента с id: {} на сумму: {}", clientId, clientLimitDto.sum());
        return limitService.checkLimit(clientId, BigDecimal.valueOf(clientLimitDto.sum()));
    }

    @PutMapping()
    public Mono<Status> debitLimit(@RequestHeader("CLIENT_ID") Integer clientId,
                                   @RequestBody ClientLimitDto clientLimitDto) {
        log.info("Запрос на списание лимита у клиента с id: {} на сумму: {}", clientId, clientLimitDto.sum());
        return limitService.debitLimit(clientId, BigDecimal.valueOf(clientLimitDto.sum()));
    }
}
