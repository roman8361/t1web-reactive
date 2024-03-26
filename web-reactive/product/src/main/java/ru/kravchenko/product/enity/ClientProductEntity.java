package ru.kravchenko.product.enity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@Table(value = "client_product")
@AllArgsConstructor
@Accessors(chain = true)
public class ClientProductEntity {
    @Id
    private Integer id;
    @Column(value = "client_id")
    private Integer clientId;
    @Column(value = "count")
    private Integer count;
    @Column(value = "balance")
    private BigDecimal balance;
    @Column(value = "type")
    private String type;
}
