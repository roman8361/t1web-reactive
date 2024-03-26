package ru.kravchenko.product.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.kravchenko.product.enity.ClientEntity;

public interface ClientRepository extends ReactiveCrudRepository<ClientEntity, Integer> {
}
