package org.b.bonusserver.mapper;

import org.b.bonusserver.dto.OperationResponse;
import org.b.bonusserver.entity.BonusOperation;
import org.b.bonusserver.entity.OperationType;

import java.time.OffsetDateTime;

public final class BonusOperationMapper {

    private BonusOperationMapper() {
    }

    public static BonusOperation toEntity(String cardNumber,
                                          OperationType operationType,
                                          long amount,
                                          long impactAmount,
                                          Long sourceOperationId) {
        BonusOperation operation = new BonusOperation();
        operation.setCardNumber(cardNumber);
        operation.setOperationType(operationType);
        operation.setAmount(amount);
        operation.setImpactAmount(impactAmount);
        operation.setSourceOperationId(sourceOperationId);
        operation.setCreatedAt(OffsetDateTime.now());
        return operation;
    }

    public static OperationResponse toResponse(BonusOperation operation) {
        return new OperationResponse(
                operation.getId(),
                operation.getCardNumber(),
                operation.getOperationType(),
                operation.getAmount(),
                operation.getImpactAmount(),
                operation.getSourceOperationId(),
                operation.getCreatedAt()
        );
    }
}
