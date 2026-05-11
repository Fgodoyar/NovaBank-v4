package com.novabank.operation.service;


import com.novabank.operation.customer.AccountServiceClient;
import com.novabank.operation.dto.*;
import com.novabank.operation.exception.AccountNotFoundException;
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

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto tiene que ser mayor que 0.");
        }

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

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto tiene que ser mayor que 0.");
        }

        AccountDTO account = accountServiceClient.getAccountByNumber(request.accountNumber());

        if (account == null) {
            throw new AccountNotFoundException(request.accountNumber());
        }

        if (account.balance().compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException(account.accountNumber(), account.balance(), request.amount());
        }

        accountServiceClient.updateBalance(account.accountId(), request.amount().negate());

        return accountServiceClient.createTransaction(
                account.accountId(),
                new CreateTransactionRequest("WITHDRAWAL", request.amount(),
                        "Retiro de cuenta " + account.accountNumber()));
    }

    @Override
    public List<TransactionDTO> transfer(CreateTransferRequest request) {

        if (request.fromAccountNumber().equals(request.toAccountNumber())) {
            throw new IllegalArgumentException("La cuenta origen no puede ser igual a la cuenta destino.");
        }

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto tiene que ser mayor que 0.");
        }

        AccountDTO sourceAccount = accountServiceClient.getAccountByNumber(request.fromAccountNumber());
        AccountDTO destinationAccount = accountServiceClient.getAccountByNumber(request.toAccountNumber());

        if (sourceAccount.balance().compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException(sourceAccount.accountNumber(), sourceAccount.balance(), request.amount());
        }

        accountServiceClient.updateBalance(sourceAccount.accountId(), request.amount().negate());
        accountServiceClient.updateBalance(destinationAccount.accountId(), request.amount());

        TransactionDTO outgoing = accountServiceClient.createTransaction(
                sourceAccount.accountId(),
                new CreateTransactionRequest("TRANSFER_OUT", request.amount(),
                        "Transferencia a " + request.toAccountNumber()));

        TransactionDTO incoming = accountServiceClient.createTransaction(
                destinationAccount.accountId(),
                new CreateTransactionRequest("TRANSFER_IN", request.amount(),
                        "Transferencia de " + request.fromAccountNumber()));

        return List.of(outgoing, incoming);
    }

}