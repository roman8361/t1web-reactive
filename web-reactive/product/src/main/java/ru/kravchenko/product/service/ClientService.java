package ru.kravchenko.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.exception.NotFoundException;
import ru.kravchenko.common.model.clients.ClientDto;
import ru.kravchenko.common.model.clients.ClientsDto;
import ru.kravchenko.product.mapper.ClientMapper;
import ru.kravchenko.product.repository.ClientRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientMapper mapper = ClientMapper.INSTANCE;
    private final ClientRepository clientRepository;

    public Mono<ClientsDto> getAllClient() {
        return clientRepository.findAll()
                .map(mapper::toDto)
                .collectList()
                .map(ClientsDto::new);
    }

    public Mono<ClientDto> findById(Integer clientId) {
        return clientRepository.findById(clientId)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(new NotFoundException("Клиент c id: " + clientId + " в БД не найден", "NOT_FOUND")))
                .doOnError(ex -> log.error("Пользователь с id: {} не найден", clientId))
                .doOnSuccess(ok -> log.info("Найден пользователь с id: {}", clientId));
    }
}
