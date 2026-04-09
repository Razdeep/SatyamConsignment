package satyamconsignment.service;

import java.sql.SQLException;
import java.util.List;
import satyamconsignment.repository.BuyerRepository;

public class BuyerService {

    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public List<String> getAllBuyers() throws SQLException {
        return buyerRepository.getAllBuyers();
    }

    public void saveBuyer(String buyerName) throws SQLException {
        buyerRepository.saveBuyer(buyerName);
    }

    public void deleteBuyer(String buyerName) throws SQLException {
        buyerRepository.deleteBuyer(buyerName);
    }

    public void renameBuyer(String oldName, String newName) throws SQLException {
        buyerRepository.renameBuyer(oldName, newName);
    }
}
