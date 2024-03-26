package ru.kravchenko.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.exception.LimitException;
import ru.kravchenko.common.exception.NotFoundException;
import ru.kravchenko.common.model.Status;
import ru.kravchenko.common.model.products.ClientProductDto;
import ru.kravchenko.product.enity.ClientProductEntity;
import ru.kravchenko.product.mapper.ClientProductMapper;
import ru.kravchenko.product.repository.ClientProductRepository;

import java.math.BigDecimal;

import static ru.kravchenko.common.utill.Constant.BALANCE_EXCEEDED;
import static ru.kravchenko.common.utill.Constant.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ClientProductMapper mapper = ClientProductMapper.INSTANCE;
    private final ClientProductRepository productsService;

    public Mono<ClientProductDto> findProductByClientIdAndType(Integer clientId, String type) {
        return productsService.findByClientIdAndType(clientId, type)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(new NotFoundException("Клиент c id: " + clientId + " в БД не найден", "NOT_FOUND")))
                .doOnError(ex -> log.error("Пользователь с id: {} не найден", clientId))
                .doOnSuccess(ok -> log.info("Найден пользователь с id: {}", clientId));
    }

    public Mono<Status> debitFunds(Integer clientId, String product, Double sum) {
        return productsService.findByClientIdAndType(clientId, product)
                .flatMap(it -> checkBalance(it, sum))
                .flatMap(productsService::save)
                .map(it -> Status.statusSuccess(SUCCESS, "Операция выполнена. Сумма списания: " + sum))
                .doOnSuccess(ok -> log.info("Операция выполнена. Сумма списания: {} для клиента с id: {}", sum, clientId))
                .onErrorResume(this::handleError);
    }

    private Mono<ClientProductEntity> checkBalance(ClientProductEntity clientProduct, Double sum) {
        final var result = clientProduct.getBalance().subtract(BigDecimal.valueOf(sum));
        if (result.compareTo(BigDecimal.ONE) <= 0) {
            return Mono.error(new LimitException("Операция не возможна баланс превышен на: " + result.abs(), BALANCE_EXCEEDED));
        }
        clientProduct.setBalance(result);
        return Mono.just(clientProduct);
    }

    private Mono<Status> handleError(Throwable error) {
        if (error instanceof LimitException) {
            return Mono.just(Status.statusError(((LimitException) error).getCode(), error.getMessage()));
        }
        return Mono.just(Status.statusError("", error.getMessage()));
    }
}
