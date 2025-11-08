package com.trainapp.services;

import com.trainapp.DTO.ConnectionDTO;
import com.trainapp.model.Connection;
import com.trainapp.utils.ConnectionFinder;
import com.trainapp.utils.ConnectionMapper;
import java.util.List;

public class ConnectionSearchService {
    
    private ConnectionFinder connectionFinder;
    private ConnectionMapper connectionMapper;
    
    public ConnectionSearchService() {
        this.connectionFinder = new ConnectionFinder();
        this.connectionMapper = new ConnectionMapper();
    }

    public List<ConnectionDTO> searchConnections(String departureCity, String arrivalCity) {
        // Search for direct connections first
        List<Connection> connections = connectionFinder.searchDirectConnections(departureCity, arrivalCity);
        
        // If no direct connections, search for indirect connections
        if (connections.isEmpty()) {
            System.out.println("No direct connections found. Searching for indirect connections");
            connections = connectionFinder.searchIndirectConnections(departureCity, arrivalCity);
        }
        
        if (connections.isEmpty()) {
            System.out.println("No connections found.");
        }
        
        // Convert to DTOs
        return connectionMapper.convertConnectionsToDTOs(connections, false);
    }
}

