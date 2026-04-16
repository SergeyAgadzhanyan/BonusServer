package org.b.bonusserver.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmountOperationRequest {

    @NotBlank(message = "Укажите номер карты")
    @Pattern(regexp = "^[0-9]{8,32}$", message = "Номер карты должен содержать от 8 до 32 цифр")
    private String cardNumber;

    @NotNull(message = "Укажите сумму")
    @Min(value = 1, message = "Сумма должна быть больше 0")
    private Long amount;
}
