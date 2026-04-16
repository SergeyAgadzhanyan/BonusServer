package org.b.bonusserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "bonus_operations")
@NoArgsConstructor
public class BonusOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", nullable = false, length = 32)
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false, length = 16)
    private OperationType operationType;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "impact_amount", nullable = false)
    private Long impactAmount;

    @Column(name = "source_operation_id")
    private Long sourceOperationId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
