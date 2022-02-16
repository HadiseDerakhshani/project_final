package ir.maktab.project_final.service.implemention;

import ir.maktab.project_final.data.dto.OrderDto;
import ir.maktab.project_final.data.dto.TransactionDto;
import ir.maktab.project_final.data.dto.mapper.OrderMap;
import ir.maktab.project_final.data.dto.mapper.TransactionMap;
import ir.maktab.project_final.data.entity.order.Transaction;
import ir.maktab.project_final.data.repasitory.TransactionRepository;
import ir.maktab.project_final.service.TransactionService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Service

public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final OrderMap orderMap;
    private final TransactionMap transactionMap;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, @Lazy OrderMap orderMap,
                                  @Lazy TransactionMap transactionMap) {
        this.transactionRepository = transactionRepository;
        this.orderMap = orderMap;
        this.transactionMap = transactionMap;
    }

    @Override
    public Optional<Transaction> findByReceptionNumber(long number) {
        return transactionRepository.findByTransactionNumber(number);
    }

    @Override
    public TransactionDto save(OrderDto orderDto, double amount) {
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .order(orderMap.createOrder(orderDto)).build();
        Transaction saveTransaction = transactionRepository.save(transaction);
        return transactionMap.createTransactionDto(transactionRepository.save(giveTransactionNumber(saveTransaction)));
    }

    @Override
    public Transaction giveTransactionNumber(Transaction transaction) {
        transaction.setTransactionNumber(1000 + transaction.getId());
        return transaction;
    }
}
