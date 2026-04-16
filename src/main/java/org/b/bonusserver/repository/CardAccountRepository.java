package org.b.bonusserver.repository;

import org.b.bonusserver.entity.CardAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CardAccountRepository extends JpaRepository<CardAccount, Long> {

    Optional<CardAccount> findByCardNumber(String cardNumber);

    @Modifying
    @Query("""
            update CardAccount a
            set a.balance = a.balance + :amount
            where a.cardNumber = :cardNumber
            """)
    int incrementBalance(@Param("cardNumber") String cardNumber, @Param("amount") long amount);

    @Modifying
    @Query("""
            update CardAccount a
            set a.balance = a.balance - :amount
            where a.cardNumber = :cardNumber
              and a.balance >= :amount
            """)
    int decrementBalanceIfEnough(@Param("cardNumber") String cardNumber, @Param("amount") long amount);
}
