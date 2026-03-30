package satyamconsignment.service;

import java.sql.SQLException;
import satyamconsignment.entity.PaymentEntity;
import satyamconsignment.repository.PaymentRepository;

public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void savePayment(PaymentEntity paymentEntity) throws SQLException {
        paymentRepository.savePayment(paymentEntity);
    }
}
