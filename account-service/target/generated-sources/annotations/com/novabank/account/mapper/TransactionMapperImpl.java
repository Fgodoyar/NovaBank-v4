package com.novabank.account.mapper;

import com.novabank.account.domain.Account;
import com.novabank.account.domain.Transaction;
import com.novabank.account.dto.TransactionDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-11T05:40:19+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionDTO toDTO(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        Long accountId = null;
        Long transactionId = null;
        String transactionType = null;
        BigDecimal amount = null;
        String description = null;
        LocalDateTime creationDate = null;

        accountId = transactionAccountAccountId( transaction );
        transactionId = transaction.getTransactionId();
        transactionType = transaction.getTransactionType();
        amount = transaction.getAmount();
        description = transaction.getDescription();
        creationDate = transaction.getCreationDate();

        TransactionDTO transactionDTO = new TransactionDTO( transactionId, transactionType, amount, description, creationDate, accountId );

        return transactionDTO;
    }

    @Override
    public Transaction toEntity(TransactionDTO transactionDTO) {
        if ( transactionDTO == null ) {
            return null;
        }

        Transaction.TransactionBuilder transaction = Transaction.builder();

        transaction.transactionId( transactionDTO.transactionId() );
        transaction.transactionType( transactionDTO.transactionType() );
        transaction.amount( transactionDTO.amount() );
        transaction.description( transactionDTO.description() );
        transaction.creationDate( transactionDTO.creationDate() );

        return transaction.build();
    }

    private Long transactionAccountAccountId(Transaction transaction) {
        Account account = transaction.getAccount();
        if ( account == null ) {
            return null;
        }
        return account.getAccountId();
    }
}
