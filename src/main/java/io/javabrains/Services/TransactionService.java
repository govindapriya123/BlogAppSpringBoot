package io.javabrains.Services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.javabrains.Entities.Transaction;
import io.javabrains.Repositories.TransactionRepository;
import java.util.Optional;
@Service
public class TransactionService {
	@Autowired
	private TransactionRepository transactionRepository;
	public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
	
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
	
   public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
    
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
