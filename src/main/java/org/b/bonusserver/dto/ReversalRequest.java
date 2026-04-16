package org.b.bonusserver.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReversalRequest {

    @NotNull(message = "Укажите идентификатор операции")
    private Long operationId;

    @NotNull(message = "Укажите сумму")
    @Min(value = 1, message = "Сумма должна быть больше 0")
    private Long amount;
}
