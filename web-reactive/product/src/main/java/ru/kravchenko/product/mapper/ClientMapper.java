package ru.kravchenko.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.kravchenko.common.model.clients.ClientDto;
import ru.kravchenko.product.enity.ClientEntity;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {
    public static final ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    public abstract ClientDto toDto(ClientEntity entity);
}
