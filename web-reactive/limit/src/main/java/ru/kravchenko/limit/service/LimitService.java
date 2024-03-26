package ru.kravchenko.limit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.exception.LimitException;
import ru.kravchenko.common.model.Status;
import ru.kravchenko.limit.entity.ClientLimitEntity;
import ru.kravchenko.limit.repository.ClientLimitRepository;

import java.math.BigDecimal;

import static ru.kravchenko.common.utill.Constant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LimitService {
    private final ClientLimitRepository clientLimitRepository;

    public Mono<Status> checkLimit(Integer clientId, BigDecimal sum) {
        return clientLimitRepository.findByClientId(clientId)
                .switchIfEmpty(createNewLimit(clientId))
                .flatMap(it -> checkClientLimit(it, sum))
                .map(it -> Status.statusSuccess(SUCCESS, "Доступен лимит списания на сумму: " + sum))
                .onErrorResume(this::handleError);
    }

    public Mono<Status> debitLimit(Integer clientId, BigDecimal sum) {
        return clientLimitRepository.findByClientId(clientId)
                .flatMap(it -> debitLimit(it, sum))
                .onErrorResume(this::handleError);
    }

    private Mono<ClientLimitEntity> checkClientLimit(ClientLimitEntity entity, BigDecimal sum) {
        final var result = entity.getCurrentLimit().subtract(sum);
        if (result.compareTo(BigDecimal.ONE) <= 0) {
            return Mono.error(new LimitException("Операция не возможна суточный лимит превышен на: " + result.abs(), LIMIT_EXCEEDED));
        }
        entity.setCurrentLimit(result);
        return Mono.just(entity);
    }

    private Mono<ClientLimitEntity> createNewLimit(Integer clientId) {
        final var newClient = ClientLimitEntity.of(clientId, DAILY_LIMIT);
        return clientLimitRepository.save(newClient)
                .map(it -> newClient)
                .doOnSuccess(ok -> log.info("Добавлен новый клиент с id: {} с суточным лимитом: {}", clientId, DAILY_LIMIT));
    }

    private Mono<Status> debitLimit(ClientLimitEntity clientLimit, BigDecimal sum) {
        clientLimit.setCurrentLimit(clientLimit.getCurrentLimit().subtract(sum));
        return clientLimitRepository.save(clientLimit)
                .map(ok -> Status.statusSuccess(SUCCESS, "Удачное списание лимита: " + sum))
                .doOnSuccess(ok -> log.info("Для клиента с id: {} списан суточный лимит: {} ", clientLimit.getClientId(), sum));
    }

    private Mono<Status> handleError(Throwable error) {
        if (error instanceof LimitException) {
            return Mono.just(Status.statusError(((LimitException) error).getCode(), error.getMessage()));
        }
        return Mono.just(Status.statusError("", error.getMessage()));
    }
}
