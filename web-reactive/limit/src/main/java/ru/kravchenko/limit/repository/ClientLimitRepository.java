package ru.kravchenko.limit.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.kravchenko.limit.entity.ClientLimitEntity;

public interface ClientLimitRepository extends ReactiveCrudRepository<ClientLimitEntity, Integer> {
    Mono<ClientLimitEntity> findByClientId(Integer clientId);

    Mono<ClientLimitEntity> findById(Integer id);
}
