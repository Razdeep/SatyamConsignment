package satyamconsignment.service;

import java.sql.SQLException;
import satyamconsignment.entity.BillEntity;
import satyamconsignment.repository.BillRepository;

public class BillService {

    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public void saveBill(BillEntity billEntity) throws SQLException {
        billRepository.save(billEntity);
    }
}
