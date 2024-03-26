package ru.kravchenko.product;

import org.junit.jupiter.api.Test;
import ru.kravchenko.product.enity.ClientProductEntity;
import ru.kravchenko.product.mapper.ClientProductMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientProductMapperTest {
    private final ClientProductMapper mapper = ClientProductMapper.INSTANCE;

    @Test
    public void clientProductTest() {
        final var clientEntity = new ClientProductEntity(1, 2, 3, BigDecimal.valueOf(1L), "ANY");
        final var result = mapper.toDto(clientEntity);
        assertAll(
                () -> assertEquals(result.id(), clientEntity.getId()),
                () -> assertEquals(result.clientId(), clientEntity.getClientId()),
                () -> assertEquals(result.count(), clientEntity.getCount()),
                () -> assertEquals(result.balance(), clientEntity.getBalance()),
                () -> assertEquals(result.type(), clientEntity.getType())
        );
    }
}
