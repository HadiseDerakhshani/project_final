package ir.maktab.project_final.data.dto.mapper;

import ir.maktab.project_final.data.dto.TransactionDto;
import ir.maktab.project_final.data.entity.order.Transaction;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Data
@Component

public class TransactionMap {

    private final OrderMap orderMap;

    @Autowired
    public TransactionMap(@Lazy OrderMap orderMap) {

        this.orderMap = orderMap;
    }

    public Transaction createTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .amount(transactionDto.getAmount())
                .transactionNumber(transactionDto.getTransactionNumber())
                .registerDate(transactionDto.getRegisterDate()).build();
        if (transactionDto.getOrder() != null) {
            transaction.setOrder(orderMap.createOrder(transactionDto.getOrder()));
        }
        return transaction;
    }

    public TransactionDto createTransactionDto(Transaction transaction) {
        TransactionDto transactionDto = TransactionDto.builder()
                .amount(transaction.getAmount())
                .transactionNumber(transaction.getTransactionNumber())
                .registerDate(transaction.getRegisterDate()).build();
        if (transaction.getOrder() != null) {
            transactionDto.setOrder(orderMap.createOrderDto(transaction.getOrder()));
        }
        return transactionDto;
    }
}
