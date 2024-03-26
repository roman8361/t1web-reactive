package ru.kravchenko.product.enity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(value = "client")
@AllArgsConstructor
@Accessors(chain = true)
public class ClientEntity {
    @Id
    @Column(value = "id")
    private Integer id;
    @Column(value = "name")
    private String name;
}
