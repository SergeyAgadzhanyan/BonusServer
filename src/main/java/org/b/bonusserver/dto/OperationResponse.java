package org.b.bonusserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.b.bonusserver.entity.OperationType;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class OperationResponse {

    private final Long operationId;
    private final String cardNumber;
    private final OperationType operationType;
    private final Long amount;
    private final Long impactAmount;
    private final Long sourceOperationId;
    private final OffsetDateTime createdAt;
}
