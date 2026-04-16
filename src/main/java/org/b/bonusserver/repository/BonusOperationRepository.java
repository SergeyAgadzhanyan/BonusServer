package org.b.bonusserver.repository;

import org.b.bonusserver.entity.BonusOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BonusOperationRepository extends JpaRepository<BonusOperation, Long> {

    @Query("""
            select o
            from BonusOperation o
            where o.cardNumber = :cardNumber
            order by o.createdAt desc
            """)
    Page<BonusOperation> findHistoryByCardNumber(@Param("cardNumber") String cardNumber, Pageable pageable);

    @Query("""
            select coalesce(sum(o.impactAmount), 0)
            from BonusOperation o
            where o.sourceOperationId = :sourceOperationId
            """)
    Long getTotalReversedImpact(@Param("sourceOperationId") Long sourceOperationId);
}
