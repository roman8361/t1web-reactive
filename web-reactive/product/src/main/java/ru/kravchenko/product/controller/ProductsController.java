package ru.kravchenko.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.kravchenko.common.model.Status;
import ru.kravchenko.common.model.debit.DebitDto;
import ru.kravchenko.product.service.ProductsService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductsController {
    private final ProductsService productsService;

    @PutMapping()
    public Mono<Status> findProductByClientIdAndType(@RequestHeader("CLIENT_ID") Integer clientId,
                                                     @RequestBody DebitDto debitDto) {
        log.info("Запрос на списание у клиента с id: {} по продукту {} на сумму {}", clientId, debitDto.product(), debitDto.sum());
        return productsService.debitFunds(clientId, debitDto.product(), debitDto.sum());
    }
}
