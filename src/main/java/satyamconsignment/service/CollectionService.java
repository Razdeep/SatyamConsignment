package satyamconsignment.service;

import java.sql.SQLException;
import java.util.List;
import satyamconsignment.entity.BillEntity;
import satyamconsignment.entity.CollectionEntity;
import satyamconsignment.model.BuyerLedgerRow;
import satyamconsignment.repository.CollectionRepository;

public class CollectionService {

    private final CollectionRepository collectionRepository;

    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public List<String> fetchPendingBillNosForBuyer(String buyerName) throws SQLException {
        return collectionRepository.fetchPendingBillsForBuyer(buyerName).stream()
                .map(BillEntity::getBillNo)
                .toList();
    }

    public List<BillEntity> fetchPendingBillsForBuyer(String buyerName) throws SQLException {
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

    public CollectionEntity getCollection(String voucherNo) throws SQLException {
        return collectionRepository.getCollection(voucherNo);
    }

    public void deleteCollection(String voucherNo) throws SQLException {
        collectionRepository.deleteCollection(voucherNo);
    }

    public List<CollectionEntity> getCollections() throws SQLException {
        return collectionRepository.getCollections();
    }

    public List<BuyerLedgerRow> getCollectionDetailsForBuyer(String buyerName) throws SQLException {
        return collectionRepository.getCollectionDetailsForBuyer(buyerName);
    }
}
