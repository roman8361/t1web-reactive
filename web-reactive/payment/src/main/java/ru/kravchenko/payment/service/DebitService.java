package ru.kravchenko.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.model.Status;
import ru.kravchenko.common.model.debit.DebitDto;
import ru.kravchenko.common.model.limit.ClientLimitDto;
import ru.kravchenko.payment.web.WebClients;
import ru.kravchenko.payment.web.WebLimit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebitService {
    private final WebLimit webLimit;
    private final WebClients webClients;

    public Mono<Status> debitFunds(Integer clientId, DebitDto debitDto) {
        final var clientLimitDto = new ClientLimitDto(debitDto.sum());
        return webLimit.checkClientLimit(clientId, clientLimitDto)
                .flatMap(it -> checkStatus(it, clientId, debitDto))
                .flatMap(it -> checkStatusFromProduct(it, clientId, debitDto))
                .doOnSuccess(ok -> log.info("Операция списания выполнена успешно для клиента с id: {}", clientLimitDto));
    }

    private Mono<Status> checkStatus(Status status, Integer clientId, DebitDto debitDto) {
        if (status.status())
            return webClients.debitFunds(clientId, debitDto);
        return Mono.just(status);
    }

    private Mono<Status> checkStatusFromProduct(Status status, Integer clientId, DebitDto debitDto) {
        return status.status() ? webLimit.debitLimit(clientId, debitDto) : Mono.just(status);
    }
}
