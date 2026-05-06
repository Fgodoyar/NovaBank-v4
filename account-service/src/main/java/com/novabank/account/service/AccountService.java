package com.novabank.account.service;

import com.novabank.account.dto.AccountDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    AccountDTO createAccount(Long customerId);
    List<AccountDTO> findByCustomerId(Long customerId);
    AccountDTO findByAccountNumber(String accountNumber);
    List<AccountDTO> findByCustomerIdWithTransactions(Long customerId);
}
