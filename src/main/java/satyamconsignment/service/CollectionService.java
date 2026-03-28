package satyamconsignment.service;

import java.sql.SQLException;
import java.util.List;
import satyamconsignment.entity.CollectionEntity;
import satyamconsignment.repository.CollectionRepository;

public class CollectionService {

    private final CollectionRepository collectionRepository;

    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public List<String> fetchPendingBillsForBuyer(String buyerName) throws SQLException {
        return collectionRepository.fetchPendingBillsForBuyer(buyerName);
    }

    public int fetchPendingAmountForBillNo(String billNo) throws SQLException {
        return collectionRepository.fetchPendingAmountForBillNo(billNo);
    }

    public void saveCollection(CollectionEntity collectionEntity) throws SQLException {
        collectionRepository.saveCollection(collectionEntity);
    }

    public String getLastVoucher() throws SQLException {
        return collectionRepository.getLastVoucher();
    }
}
