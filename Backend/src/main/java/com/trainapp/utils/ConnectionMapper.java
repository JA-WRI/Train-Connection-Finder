package com.trainapp.utils;

import com.trainapp.DTO.ConnectionDTO;
import com.trainapp.DTO.RouteDTO;
import com.trainapp.model.Connection;
import com.trainapp.model.Route;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConnectionMapper {
    
    public ConnectionMapper() {}

    public List<Connection> convertDTOsToConnections(List<ConnectionDTO> connectionDTOs) {
        List<Connection> connections = new ArrayList<>();
        if (connectionDTOs != null) {
            for (ConnectionDTO dto : connectionDTOs) {
                List<Route> routes = new ArrayList<>();
                if (dto.routes != null) {
                    for (RouteDTO rdto : dto.routes) {
                        Route r = new Route(
                                null,
                                rdto.departureCity,
                                rdto.arrivalCity,
                                LocalTime.parse(rdto.departureTime),
                                LocalTime.parse(rdto.arrivalTime),
                                rdto.trainType,
                                rdto.daysOfOperation,
                                rdto.firstClassTicketRate,
                                rdto.secondClassTicketRate,
                                false
                        );
                        routes.add(r);
                    }
                } else {
                    // Fallback: synthesize a single route from top-level fields if routes missing
                    try {
                        Route r = new Route(
                                null,
                                dto.departureCity,
                                dto.arrivalCity,
                                LocalTime.parse(dto.departureTime),
                                LocalTime.parse(dto.arrivalTime),
                                null,
                                null,
                                dto.firstClassPrice,
                                dto.secondClassPrice,
                                false
                        );
                        routes.add(r);
                    } catch (Exception ignored) {}
                }
                connections.add(new Connection(routes));
            }
        }
        return connections;
    }

    public Connection convertDTOToConnection(ConnectionDTO connectionDTO) {
        List<Route> routes = new ArrayList<>();
        if (connectionDTO.routes != null) {
            for (RouteDTO rdto : connectionDTO.routes) {
                Route r = new Route(
                        null,
                        rdto.departureCity,
                        rdto.arrivalCity,
                        LocalTime.parse(rdto.departureTime),
                        LocalTime.parse(rdto.arrivalTime),
                        rdto.trainType,
                        rdto.daysOfOperation,
                        rdto.firstClassTicketRate,
                        rdto.secondClassTicketRate,
                        false
                );
                routes.add(r);
            }
        } else {
            // Fallback: create route from connection DTO
            Route r = new Route(
                    null,
                    connectionDTO.departureCity.toLowerCase(),
                    connectionDTO.arrivalCity.toLowerCase(),
                    LocalTime.parse(connectionDTO.departureTime),
                    LocalTime.parse(connectionDTO.arrivalTime),
                    null,
                    null,
                    connectionDTO.firstClassPrice,
                    connectionDTO.secondClassPrice,
                    false
            );
            routes.add(r);
        }
        return new Connection(routes);
    }

    public List<ConnectionDTO> convertConnectionsToDTOs(List<Connection> connections, boolean isFirstClass) {
        return connections.stream()
                .map(c -> new ConnectionDTO(c, isFirstClass))
                .toList();
    }
}

