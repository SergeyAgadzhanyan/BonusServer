package org.b.bonusserver.service;

import org.b.bonusserver.dto.AmountOperationRequest;
import org.b.bonusserver.dto.BalanceResponse;
import org.b.bonusserver.dto.OperationResponse;
import org.b.bonusserver.dto.ReversalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BonusService {

    OperationResponse accrue(AmountOperationRequest request);

    OperationResponse writeOff(AmountOperationRequest request);

    OperationResponse reverse(ReversalRequest request);

    BalanceResponse getBalance(String cardNumber);

    Page<OperationResponse> getHistory(String cardNumber, Pageable pageable);
}
