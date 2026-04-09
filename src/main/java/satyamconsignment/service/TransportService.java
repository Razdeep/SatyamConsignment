package satyamconsignment.service;

import java.sql.SQLException;
import java.util.List;
import satyamconsignment.repository.TransportRepository;

public class TransportService {

    private final TransportRepository transportRepository;

    public TransportService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    public List<String> getAllTransports() throws SQLException {
        return transportRepository.getAllTransports();
    }

    public void saveTransport(String transportName) throws SQLException {
        transportRepository.saveTransport(transportName);
    }

    public void deleteTransport(String transportName) throws SQLException {
        transportRepository.deleteTransport(transportName);
    }

    public void renameTransport(String oldName, String newName) throws SQLException {
        transportRepository.renameTransport(oldName, newName);
    }
}
