package com.novabank.operation.service;



import com.novabank.operation.dto.CreateOperationRequest;
import com.novabank.operation.dto.CreateTransferRequest;
import com.novabank.operation.dto.TransactionDTO;

import java.util.List;

public interface OperationService {
    TransactionDTO deposit(CreateOperationRequest request);
    TransactionDTO withdraw(CreateOperationRequest request);
    List<TransactionDTO> transfer(CreateTransferRequest request);
}
