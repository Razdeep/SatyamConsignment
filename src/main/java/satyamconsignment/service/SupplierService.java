package satyamconsignment.service;

import java.sql.SQLException;
import java.util.List;
import satyamconsignment.repository.SupplierRepository;

public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<String> getAllSuppliers() throws SQLException {
        return supplierRepository.getAllSuppliers();
    }

    public void saveSupplier(String supplierName) throws SQLException {
        supplierRepository.saveSupplier(supplierName);
    }

    public void deleteSupplier(String supplierName) throws SQLException {
        supplierRepository.deleteSupplier(supplierName);
    }

    public void renameSupplier(String oldName, String newName) throws SQLException {
        supplierRepository.renameSupplier(oldName, newName);
    }
}
