package ru.kravchenko.product;

import org.junit.jupiter.api.Test;
import ru.kravchenko.product.enity.ClientEntity;
import ru.kravchenko.product.mapper.ClientMapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientMapperTest {
    private final ClientMapper mapper = ClientMapper.INSTANCE;

    @Test
    public void clientTest() {
        final var clientEntity = new ClientEntity(1, "Шаман");
        final var result = mapper.toDto(clientEntity);
        assertAll(
                () -> assertEquals(result.id(), clientEntity.getId()),
                () -> assertEquals(result.name(), clientEntity.getName())
        );
    }

}
