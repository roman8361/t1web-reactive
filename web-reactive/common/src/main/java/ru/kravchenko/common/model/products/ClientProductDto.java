package ru.kravchenko.common.model.products;

import java.math.BigDecimal;

public record ClientProductDto(Integer id, Integer clientId, Integer count, BigDecimal balance, String type) {
}
