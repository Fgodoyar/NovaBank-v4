package com.novabank.account.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountServiceImpl {

    private String generateAccountNumber() {
        return "ES91210000" + String.format("%012d", new
                Random().nextLong(1_000_000_000_000L));
    }
}
