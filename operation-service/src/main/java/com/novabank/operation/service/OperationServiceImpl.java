package com.novabank.operation.service;


import com.novabank.operation.customer.AccountServiceClient;
import com.novabank.operation.dto.*;
import com.novabank.operation.exception.InsufficientBalanceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final AccountServiceClient accountServiceClient;

    @Override
    public TransactionDTO deposit(CreateOperationRequest request) {
        AccountDTO account = accountServiceClient.getAccountByNumber(request.accountNumber());

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto tiene que ser mayor que 0.");
        }

        accountServiceClient.updateBalance(account.accountId(), request.amount());

        return accountServiceClient.createTransaction(account.accountId(), new CreateTransactionRequest(
                "DEPOSITO", request.amount(),
                "Depósito en cuenta " + account.accountNumber()
        ));
    }

    @Override
    public TransactionDTO withdraw(CreateOperationRequest request) {
        AccountDTO account = accountServiceClient.getAccountByNumber(request.accountNumber());

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto tiene que ser mayor que 0.");
        }

        if (account.balance().compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException(account.accountNumber(), account.balance(), request.amount());
        }

        accountServiceClient.updateBalance(account.accountId(), request.amount().negate());

        return accountServiceClient.createTransaction(account.accountId(), new CreateTransactionRequest(
                "RETIRO", request.amount(),
                "Retiro en cuenta " + account.accountNumber()
        ));
    }

    @Override
    public List<TransactionDTO> transfer(CreateTransferRequest request) {
        AccountDTO sourceAccount = accountServiceClient.getAccountByNumber(request.fromAccountNumber());
        AccountDTO destinationAccount = accountServiceClient.getAccountByNumber(request.toAccountNumber());

        if (sourceAccount.accountNumber().equals(request.toAccountNumber())) {
            throw new IllegalArgumentException("La cuenta origen no puede ser igual a la cuenta destino.");
        }

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto tiene que ser mayor que 0.");
        }

        if (sourceAccount.balance().compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException(sourceAccount.accountNumber(), sourceAccount.balance(), request.amount());
        }

        accountServiceClient.updateBalance(sourceAccount.accountId(), request.amount().negate());
        accountServiceClient.updateBalance(destinationAccount.accountId(), request.amount());

        TransactionDTO outgoing = accountServiceClient.createTransaction(
                sourceAccount.accountId(), new CreateTransactionRequest(
                        "TRANSFERENCIA_SALIENTE", request.amount(),
                        "Transferencia saliente en cuenta " + request.fromAccountNumber())
                );
        TransactionDTO incoming = accountServiceClient.createTransaction(
                destinationAccount.accountId(),  new CreateTransactionRequest(
                        "TRANSFERENCIA_ENTRANTE", request.amount(),
                        "Transferencia entrante en cuenta " + request.toAccountNumber())
                );

        return List.of(outgoing, incoming);
    }

}