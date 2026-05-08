package com.novabank.operation.customer;

import com.novabank.operation.dto.AccountDTO;
import com.novabank.operation.dto.CreateTransactionRequest;
import com.novabank.operation.dto.TransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "ACCOUNT-SERVICE", fallback = AccountServiceFallback.class)
public interface AccountServiceClient {

    @GetMapping("/api/accounts/{accountId}")
    AccountDTO getAccountById(@PathVariable("accountId") Long accountId);

    @GetMapping("/api/accounts/number/{accountNumber}")
    AccountDTO getAccountByNumber(@PathVariable("accountNumber") String accountNumber);

    @PutMapping("/api/accounts/{accountId}/balance")
    AccountDTO updateBalance(@PathVariable("accountId") Long accountId, @RequestParam("amount") BigDecimal amount);

    @PostMapping("/api/accounts/{accountId}/transactions")
    TransactionDTO createTransaction(
            @PathVariable("accountId") Long accountId, @RequestBody CreateTransactionRequest request);
}