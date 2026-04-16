package org.b.bonusserver.mapper;

import org.b.bonusserver.dto.BalanceResponse;
import org.b.bonusserver.entity.CardAccount;

public final class CardAccountMapper {

    private CardAccountMapper() {
    }

    public static CardAccount toNewEntity(String cardNumber) {
        CardAccount account = new CardAccount();
        account.setCardNumber(cardNumber);
        account.setBalance(0L);
        return account;
    }

    public static BalanceResponse toBalanceResponse(CardAccount account) {
        return new BalanceResponse(account.getCardNumber(), account.getBalance());
    }
}
