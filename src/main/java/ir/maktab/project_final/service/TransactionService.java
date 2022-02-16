package ir.maktab.project_final.service;


import ir.maktab.project_final.data.dto.OrderDto;
import ir.maktab.project_final.data.dto.TransactionDto;
import ir.maktab.project_final.data.entity.order.Transaction;

import java.util.Optional;

public interface TransactionService {
    Optional<Transaction> findByReceptionNumber(long number);

    TransactionDto save(OrderDto orderDto, double amount);

    Transaction giveTransactionNumber(Transaction transaction);
}
