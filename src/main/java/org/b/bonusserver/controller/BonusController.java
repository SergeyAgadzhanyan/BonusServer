package org.b.bonusserver.controller;

import org.b.bonusserver.dto.AmountOperationRequest;
import org.b.bonusserver.dto.BalanceResponse;
import org.b.bonusserver.dto.OperationResponse;
import org.b.bonusserver.dto.ReversalRequest;
import org.b.bonusserver.service.BonusService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bonuses")
public class BonusController {

    private final BonusService bonusService;

    @PostMapping("/accrual")
    public OperationResponse accrue(@Valid @RequestBody AmountOperationRequest request) {
        return bonusService.accrue(request);
    }

    @PostMapping("/write-off")
    public OperationResponse writeOff(@Valid @RequestBody AmountOperationRequest request) {
        return bonusService.writeOff(request);
    }

    @PostMapping("/reversal")
    public OperationResponse reverse(@Valid @RequestBody ReversalRequest request) {
        return bonusService.reverse(request);
    }

    @Validated
    @GetMapping("/{cardNumber}/balance")
    public BalanceResponse getBalance(@PathVariable
                                      @Pattern(regexp = "^[0-9]{8,32}$", message = "Номер карты должен содержать от 8 до 32 цифр")
                                      String cardNumber) {
        return bonusService.getBalance(cardNumber);
    }

    @Validated
    @GetMapping("/{cardNumber}/operations")
    public Page<OperationResponse> getHistory(@PathVariable
                                              @Pattern(regexp = "^[0-9]{8,32}$", message = "Номер карты должен содержать от 8 до 32 цифр")
                                              String cardNumber,
                                              Pageable pageable) {
        return bonusService.getHistory(cardNumber, pageable);
    }
}
