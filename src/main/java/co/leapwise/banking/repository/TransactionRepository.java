package co.leapwise.banking.repository;

import co.leapwise.banking.model.Transaction;
import co.leapwise.banking.request.params.TransactionParams;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
  @Query(
      "SELECT t FROM Transaction t "
          + "WHERE (t.sender.customer.customerId = :customerId OR t.receiver.customer.customerId = :customerId) "
          + "AND (:#{#params.transactionId} IS NULL OR t.transactionId = :#{#params.transactionId}) "
          + "AND (:#{#params.amount} IS NULL OR t.amount = :#{#params.amount}) "
          + "AND (:#{#params.currencyName} IS NULL OR t.currency.name = :#{#params.currencyName}) "
          + "AND (:#{#params.senderAccountId} IS NULL OR t.sender.accountId = :#{#params.senderAccountId}) "
          + "AND (:#{#params.senderAccountNumber} IS NULL OR t.sender.accountNumber = :#{#params.senderAccountNumber}) "
          + "AND (:#{#params.senderAccountType} IS NULL OR t.sender.accountType = :#{#params.senderAccountType}) "
          + "AND (:#{#params.senderCustomerId} IS NULL OR t.sender.customer.customerId = :#{#params.senderCustomerId}) "
          + "AND (:#{#params.senderName} IS NULL OR t.sender.customer.name = :#{#params.senderName}) "
          + "AND (:#{#params.senderAddress} IS NULL OR t.sender.customer.address = :#{#params.senderAddress}) "
          + "AND (:#{#params.senderEmail} IS NULL OR t.sender.customer.email = :#{#params.senderEmail}) "
          + "AND (:#{#params.senderPhoneNumber} IS NULL OR t.sender.customer.phoneNumber = :#{#params.senderPhoneNumber}) "
          + "AND (:#{#params.receiverAccountId} IS NULL OR t.receiver.accountId = :#{#params.receiverAccountId}) "
          + "AND (:#{#params.receiverAccountNumber} IS NULL OR t.receiver.accountNumber = :#{#params.receiverAccountNumber}) "
          + "AND (:#{#params.receiverAccountType} IS NULL OR t.receiver.accountType = :#{#params.receiverAccountType}) "
          + "AND (:#{#params.receiverCustomerId} IS NULL OR t.receiver.customer.customerId = :#{#params.receiverCustomerId}) "
          + "AND (:#{#params.receiverName} IS NULL OR t.receiver.customer.name = :#{#params.receiverName}) "
          + "AND (:#{#params.receiverAddress} IS NULL OR t.receiver.customer.address = :#{#params.receiverAddress}) "
          + "AND (:#{#params.receiverEmail} IS NULL OR t.receiver.customer.email = :#{#params.receiverEmail}) "
          + "AND (:#{#params.receiverPhoneNumber} IS NULL OR t.receiver.customer.phoneNumber = :#{#params.receiverPhoneNumber})")
  Page<Transaction> findByCustomerId(Long customerId, TransactionParams params, Pageable pageable);

  @Query(
      "SELECT t FROM Transaction t "
          + "WHERE (t.sender.accountId = :accountId OR t.receiver.accountId = :accountId) "
          + "AND MONTH(t.timeStamp) = :month "
          + "AND YEAR(t.timeStamp) = :year")
  List<Transaction> findByMonthAndYear(Long accountId, int year, int month);
}
