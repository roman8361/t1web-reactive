package ru.kravchenko.product.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.kravchenko.product.enity.ClientProductEntity;

public interface ClientProductRepository extends ReactiveCrudRepository<ClientProductEntity, Integer> {
    Mono<ClientProductEntity> findByClientIdAndType(Integer clientId, String type);
}
