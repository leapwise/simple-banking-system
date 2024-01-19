package com.leapwise.bankingsystem.repository;

import com.leapwise.bankingsystem.domain.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Transaction> findAllByCustomerId(UUID customerId, Map<String, String> params) {
        StringBuilder sb = new StringBuilder("SELECT t.amount, t.currency, t.message, t.id, t.created, t.modified, t.timestamp, t.sender_account_id, t.receiver_account_id " +
                "FROM transaction t, account a, account b WHERE  t.sender_account_id = a.id AND a.customer_id = :customerId");

        String amount = params.get("amount");
        String timestamp = params.get("timestamp");
        String currency = params.get("currency");
        String receiverAccNumber = params.get("receiverAccNumber");

        appendCondition(sb, amount, "t.amount <= :amount");
        appendCondition(sb, timestamp, "t.timestamp <= :timestamp");
        appendCondition(sb, currency, "t.currency = :currency");
        appendCondition(sb, receiverAccNumber, "b.account_number = :receiverAccNumber AND b.id = t.receiver_account_id");

        Query query = entityManager.createNativeQuery(sb.toString(), Transaction.class);
        appendParameterValue(query, "customerId", customerId.toString());
        appendParameterValue(query, "amount", amount);
        appendParameterValue(query, "timestamp", timestamp);
        appendParameterValue(query, "currency", currency);
        appendParameterValue(query, "receiverAccNumber", receiverAccNumber);

        return query.getResultList();
    }

    private void appendCondition(StringBuilder sb, String param, String condition) {
        if(param != null && !param.isEmpty()) {
            sb.append(" AND ").append(condition);
        }
    }

    private void appendParameterValue(Query query, String param, String value) {
        if (value != null && !value.isEmpty()) {
            query.setParameter(param, value);
        }
    }
}
