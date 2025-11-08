package com.trainapp.services;

import com.google.gson.JsonObject;
import com.trainapp.DTO.ConnectionDTO;
import com.trainapp.model.Connection;
import com.trainapp.utils.ConnectionMapper;
import com.trainapp.utils.filterConnections;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class ConnectionFilterService {
    
    private ConnectionMapper connectionMapper;
    private filterConnections filterUtil;
    
    public ConnectionFilterService() {
        this.connectionMapper = new ConnectionMapper();
        this.filterUtil = new filterConnections();
    }

    public List<ConnectionDTO> filterAndSortConnections(
            List<ConnectionDTO> connectionDTOs,
            JsonObject filtersJson,
            JsonObject sortJson) {
        
        // Convert DTOs to domain models
        List<Connection> connections = connectionMapper.convertDTOsToConnections(connectionDTOs);
        
        // Extract filter parameters
        FilterParams filterParams = extractFilterParams(filtersJson);
        SortParams sortParams = extractSortParams(sortJson);
        
        // Apply filtering and sorting
        List<Connection> filteredConnections = applyFiltersAndSorting(
                connections,
                filterParams,
                sortParams
        );
        
        // Convert back to DTOs
        boolean isFirstClass = filterParams.ticketClass != null && 
                              filterParams.ticketClass.equalsIgnoreCase("first-class");
        return connectionMapper.convertConnectionsToDTOs(filteredConnections, isFirstClass);
    }

    private List<Connection> applyFiltersAndSorting(
            List<Connection> connections,
            FilterParams filterParams,
            SortParams sortParams) {
        
        List<Connection> filtered = connections;
        
        // Apply filters
        if (filterParams.departureDay != null && !filterParams.departureDay.isEmpty()) {
            filtered = filterUtil.filterByDayOfDeparture(filtered, filterParams.departureDay);
        }
        
        if (filterParams.arrivalDay != null && !filterParams.arrivalDay.isEmpty()) {
            filtered = filterUtil.filterByDayOfArrival(filtered, filterParams.arrivalDay);
        }
        
        if (filterParams.departureTime != null && !filterParams.departureTime.isEmpty()) {
            filtered = filterUtil.filterByDepartureTime(
                    filtered, 
                    LocalTime.parse(filterParams.departureTime + ":00")
            );
        }
        
        if (filterParams.arrivalTime != null && !filterParams.arrivalTime.isEmpty()) {
            filtered = filterUtil.filterByArrivalTime(
                    filtered, 
                    LocalTime.parse(filterParams.arrivalTime + ":00")
            );
        }
        
        if (filterParams.ticketClass != null && !filterParams.ticketClass.isEmpty()) {
            if (filterParams.ticketClass.equalsIgnoreCase("first-class") && filterParams.maxPrice > 0) {
                filtered = filterUtil.filterPriceFirstClass(filterParams.maxPrice, filtered);
            } else if (filterParams.ticketClass.equalsIgnoreCase("second-class") && filterParams.maxPrice != 0) {
                filtered = filterUtil.filterPriceSecondClass(filterParams.maxPrice, filtered);
            }
        }
        
        if (filterParams.maxDuration > 0) {
            filtered = filterUtil.filterDuration(filterParams.maxDuration, filtered);
        }
        
        // Apply sorting
        if (sortParams.criteria != null && !sortParams.criteria.isEmpty() && 
            sortParams.order != null && !sortParams.order.isEmpty()) {
            filtered = applySorting(filtered, sortParams);
        }
        
        return filtered;
    }

    private List<Connection> applySorting(List<Connection> connections, SortParams sortParams) {
        List<Connection> sorted = connections;
        
        switch (sortParams.criteria) {
            case "duration":
                if ("asc".equalsIgnoreCase(sortParams.order)) {
                    sorted = filterUtil.AscenSortDuration(sorted);
                } else {
                    sorted = filterUtil.DescenSortDuration(sorted);
                }
                break;
            case "priceFirst":
                if ("asc".equalsIgnoreCase(sortParams.order)) {
                    sorted = filterUtil.sortByFirstClassPriceAscending(sorted);
                } else {
                    sorted = filterUtil.sortByFirstClassPriceDescending(sorted);
                }
                break;
            case "priceSecond":
                if ("asc".equalsIgnoreCase(sortParams.order)) {
                    sorted = filterUtil.sortBySecondClassPriceAscending(sorted);
                } else {
                    sorted = filterUtil.sortBySecondClassPriceDescending(sorted);
                }
                break;
            case "departureTime":
                sorted = filterUtil.sortByDepartureTime(sorted);
                if ("desc".equalsIgnoreCase(sortParams.order)) {
                    Collections.reverse(sorted);
                }
                break;
            case "routesCount":
                sorted = filterUtil.sortByNumRoutes(sorted);
                if ("desc".equalsIgnoreCase(sortParams.order)) {
                    Collections.reverse(sorted);
                }
                break;
            default:
                // no-op for unknown criteria
                break;
        }
        
        return sorted;
    }

    private FilterParams extractFilterParams(JsonObject filtersJson) {
        FilterParams params = new FilterParams();
        
        params.departureDay = filtersJson.has("departureDay") && 
                              !filtersJson.get("departureDay").getAsString().isEmpty()
                ? filtersJson.get("departureDay").getAsString().substring(0, 3)
                : null;
        
        params.arrivalDay = filtersJson.has("arrivalDay") && 
                            !filtersJson.get("arrivalDay").getAsString().isEmpty()
                ? filtersJson.get("arrivalDay").getAsString().substring(0, 3)
                : null;
        
        params.departureTime = filtersJson.has("departureTime") 
                ? filtersJson.get("departureTime").getAsString() 
                : null;
        
        params.arrivalTime = filtersJson.has("arrivalTime") 
                ? filtersJson.get("arrivalTime").getAsString() 
                : null;
        
        params.ticketClass = filtersJson.has("ticketClass") 
                ? filtersJson.get("ticketClass").getAsString() 
                : null;
        
        params.maxPrice = filtersJson.has("maxPrice") && 
                         !filtersJson.get("maxPrice").getAsString().isEmpty()
                ? filtersJson.get("maxPrice").getAsInt()
                : Integer.MAX_VALUE;
        
        params.maxDuration = filtersJson.has("maxDuration") && 
                            !filtersJson.get("maxDuration").getAsString().isEmpty()
                ? filtersJson.get("maxDuration").getAsInt()
                : Integer.MAX_VALUE;
        
        return params;
    }

    private SortParams extractSortParams(JsonObject sortJson) {
        SortParams params = new SortParams();
        
        if (sortJson != null && sortJson.has("criteria") && sortJson.has("order")) {
            params.criteria = sortJson.get("criteria").getAsString();
            params.order = sortJson.get("order").getAsString();
        }
        
        return params;
    }

    private static class FilterParams {
        String departureDay;
        String arrivalDay;
        String departureTime;
        String arrivalTime;
        String ticketClass;
        int maxPrice;
        int maxDuration;
    }

    private static class SortParams {
        String criteria;
        String order;
    }
}

