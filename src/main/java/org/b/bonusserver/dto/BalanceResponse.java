package org.b.bonusserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BalanceResponse {

    private final String cardNumber;
    private final Long balance;
}
