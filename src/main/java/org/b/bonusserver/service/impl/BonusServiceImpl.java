package org.b.bonusserver.service.impl;

import org.b.bonusserver.dto.AmountOperationRequest;
import org.b.bonusserver.dto.BalanceResponse;
import org.b.bonusserver.dto.OperationResponse;
import org.b.bonusserver.dto.ReversalRequest;
import org.b.bonusserver.entity.BonusOperation;
import org.b.bonusserver.entity.CardAccount;
import org.b.bonusserver.entity.OperationType;
import org.b.bonusserver.exception.BusinessException;
import org.b.bonusserver.exception.ResourceNotFoundException;
import org.b.bonusserver.mapper.BonusOperationMapper;
import org.b.bonusserver.mapper.CardAccountMapper;
import org.b.bonusserver.repository.BonusOperationRepository;
import org.b.bonusserver.repository.CardAccountRepository;
import org.b.bonusserver.service.BonusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BonusServiceImpl implements BonusService {

    private final CardAccountRepository cardAccountRepository;
    private final BonusOperationRepository bonusOperationRepository;

    @Override
    @Transactional
    public OperationResponse accrue(AmountOperationRequest request) {
        CardAccount account = getOrCreateAccount(request.getCardNumber());
        long amount = request.getAmount();

        cardAccountRepository.incrementBalance(account.getCardNumber(), amount);

        BonusOperation operation = BonusOperationMapper.toEntity(
                account.getCardNumber(),
                OperationType.ACCRUAL,
                amount,
                amount,
                null
        );
        return BonusOperationMapper.toResponse(bonusOperationRepository.save(operation));
    }

    @Override
    @Transactional
    public OperationResponse writeOff(AmountOperationRequest request) {
        CardAccount account = getOrCreateAccount(request.getCardNumber());
        long amount = request.getAmount();

        int updatedRows = cardAccountRepository.decrementBalanceIfEnough(account.getCardNumber(), amount);
        if (updatedRows == 0) {
            throw new BusinessException("Недостаточно бонусов на счете");
        }

        long impact = -amount;
        BonusOperation operation = BonusOperationMapper.toEntity(
                account.getCardNumber(),
                OperationType.WRITE_OFF,
                amount,
                impact,
                null
        );
        return BonusOperationMapper.toResponse(bonusOperationRepository.save(operation));
    }

    @Override
    @Transactional
    public OperationResponse reverse(ReversalRequest request) {
        BonusOperation source = bonusOperationRepository.findById(request.getOperationId())
                .orElseThrow(() -> new ResourceNotFoundException("Операция не найдена: " + request.getOperationId()));

        if (source.getOperationType() == OperationType.REVERSAL) {
            throw new BusinessException("Нельзя выполнить возврат операции возврата");
        }


        long alreadyReversed = Math.abs(bonusOperationRepository.getTotalReversedImpact(source.getId()));
        long available = source.getAmount() - alreadyReversed;

        if (available < request.getAmount()) {
            throw new BusinessException("Сумма возврата превышает доступную сумму по исходной операции");
        }

        CardAccount account = cardAccountRepository.findByCardNumber(source.getCardNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Карта не найдена: " + source.getCardNumber()));

        long impact = source.getOperationType() == OperationType.ACCRUAL
                ? -request.getAmount()
                : request.getAmount();

        if (impact < 0) {
            int updatedRows = cardAccountRepository.decrementBalanceIfEnough(account.getCardNumber(), request.getAmount());
            if (updatedRows == 0) {
                throw new BusinessException("Недостаточно бонусов для выполнения возврата");
            }
        } else {
            cardAccountRepository.incrementBalance(account.getCardNumber(), request.getAmount());
        }

        BonusOperation operation = BonusOperationMapper.toEntity(
                account.getCardNumber(),
                OperationType.REVERSAL,
                request.getAmount(),
                impact,
                source.getId()
        );
        return BonusOperationMapper.toResponse(bonusOperationRepository.save(operation));
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceResponse getBalance(String cardNumber) {
        CardAccount account = cardAccountRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Карта не найдена: " + cardNumber));

        return CardAccountMapper.toBalanceResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OperationResponse> getHistory(String cardNumber, Pageable pageable) {
        cardAccountRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Карта не найдена: " + cardNumber));

        return bonusOperationRepository.findHistoryByCardNumber(cardNumber, pageable)
                .map(BonusOperationMapper::toResponse);
    }

    private CardAccount getOrCreateAccount(String cardNumber) {
        return cardAccountRepository.findByCardNumber(cardNumber)
                .orElseGet(() -> cardAccountRepository.save(CardAccountMapper.toNewEntity(cardNumber)));
    }
}
