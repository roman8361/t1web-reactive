package ru.kravchenko.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.kravchenko.common.model.products.ClientProductDto;
import ru.kravchenko.product.enity.ClientProductEntity;

@Mapper(componentModel = "spring")
public abstract class ClientProductMapper {
    public static final ClientProductMapper INSTANCE = Mappers.getMapper(ClientProductMapper.class);

    public abstract ClientProductDto toDto(ClientProductEntity entity);
}
