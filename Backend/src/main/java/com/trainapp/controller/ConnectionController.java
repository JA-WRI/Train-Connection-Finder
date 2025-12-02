package com.trainapp.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.trainapp.DTO.ConnectionDTO;
import com.trainapp.services.ConnectionFilterService;
import com.trainapp.services.ConnectionSearchService;
import spark.Request;
import spark.Response;

import java.util.Collections;
import java.util.List;

public class ConnectionController {
    
    private ConnectionSearchService connectionSearchService;
    private ConnectionFilterService connectionFilterService;
    private Gson gson;
    
    public ConnectionController(Gson gson) {
        this.connectionSearchService = new ConnectionSearchService();
        this.connectionFilterService = new ConnectionFilterService();
        this.gson = gson;
    }

    public String searchConnections(Request req, Response res) {
        try {
            String departureCity = req.queryParams("departureCity");
            String arrivalCity = req.queryParams("arrivalCity");

            // Call service to search for connections
            List<ConnectionDTO> connectionsDTO = connectionSearchService.searchConnections(departureCity, arrivalCity);

            res.status(200);
            return gson.toJson(connectionsDTO);

        } catch (Exception e) {
            res.status(500);
            return gson.toJson("Error searching connections: " + e.getMessage());
        }
    }

    public String filterConnections(Request req, Response res) {
        res.type("application/json");
        
        try {
            String body = req.body();
            JsonObject jsonObject = gson.fromJson(body, JsonObject.class);

            // Extract request data
            var connectionsJson = jsonObject.getAsJsonArray("connections");
            var filtersJson = jsonObject.getAsJsonObject("filters");
            var sortJson = jsonObject.has("sort") && jsonObject.get("sort").isJsonObject()
                    ? jsonObject.getAsJsonObject("sort")
                    : null;

            // Deserialize connections list
            List<ConnectionDTO> connectionDTOs = gson.fromJson(
                    connectionsJson,
                    new com.google.gson.reflect.TypeToken<List<ConnectionDTO>>() {}.getType()
            );

            // Return empty list if no connections provided
            if (connectionDTOs == null || connectionDTOs.isEmpty()) {
                res.status(200);
                return gson.toJson(Collections.emptyList());
            }

            // Call service to filter and sort connections
            List<ConnectionDTO> filteredConnectionsDTO = connectionFilterService.filterAndSortConnections(
                    connectionDTOs,
                    filtersJson,
                    sortJson
            );

            res.status(200);
            return gson.toJson(filteredConnectionsDTO);
        } catch (Exception e) {
            System.err.println("Filtering/sorting error: " + e.getMessage());
            e.printStackTrace();
            res.status(500);
            return gson.toJson("Error filtering connections: " + e.getMessage());
        }
    }
}

