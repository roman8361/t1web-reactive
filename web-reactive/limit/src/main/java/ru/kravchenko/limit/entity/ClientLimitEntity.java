package ru.kravchenko.limit.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;


@Getter
@Setter
@Table(value = "client_limit")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ClientLimitEntity {
    @Id
    private Integer id;
    @Column(value = "client_id")
    private Integer clientId;
    @Column(value = "current_limit")
    private BigDecimal currentLimit;

    public static ClientLimitEntity of(Integer clientId, Double limit) {
        return new ClientLimitEntity()
                .setClientId(clientId)
                .setCurrentLimit(BigDecimal.valueOf(limit));
    }
}
