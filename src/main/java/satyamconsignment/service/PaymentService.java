package satyamconsignment.service;

import java.sql.SQLException;
import java.util.List;
import satyamconsignment.entity.PaymentEntity;
import satyamconsignment.repository.PaymentRepository;

public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<String> fetchPendingBillsForSupplier(String supplierName) throws SQLException {
        return paymentRepository.fetchPendingBillsForSupplier(supplierName);
    }

    public int fetchPendingAmountForBillNo(String billNo) throws SQLException {
        return paymentRepository.fetchPendingAmountForBillNo(billNo);
    }

    public void savePayment(PaymentEntity paymentEntity) throws SQLException {
        paymentRepository.savePayment(paymentEntity);
    }
}
