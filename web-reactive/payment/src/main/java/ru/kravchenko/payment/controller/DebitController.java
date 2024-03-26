package ru.kravchenko.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.model.Status;
import ru.kravchenko.common.model.debit.DebitDto;
import ru.kravchenko.payment.service.DebitService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/debit")
public class DebitController {
    private final DebitService debitService;

    @PostMapping
    public Mono<Status> debitFromAccount(@RequestHeader("CLIENT_ID") Integer clientId,
                                         @RequestBody DebitDto debitDto) {
        log.info("Получен запрос на списание средств для клиента с id {} " +
                "на сумму {} для продукта {}", clientId, debitDto.sum(), debitDto.product());
        return debitService.debitFunds(clientId, debitDto);
    }
}
