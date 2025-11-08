package com.trainapp.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.trainapp.services.TripHistoryService;
import spark.Request;
import spark.Response;

public class TripHistoryController {
    
    private TripHistoryService tripHistoryService;
    private Gson gson;
    
    public TripHistoryController(Gson gson) {
        this.tripHistoryService = new TripHistoryService();
        this.gson = gson;
    }

    public String handleGetUserTrips(Request req, Response res) {
        res.type("application/json");
        
        try {
            String body = req.body();
            System.out.println("Get user trips request: " + body);

            JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
            String lastName = jsonObject.get("lastName").getAsString();
            String userId = jsonObject.get("userId").getAsString();

            var trips = tripHistoryService.getUserTrips(lastName, userId);

            res.status(200);
            return gson.toJson(trips);

        } catch (Exception e) {
            System.err.println("Get user trips error: " + e.getMessage());
            e.printStackTrace();
            res.status(500);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("error", "Failed to get trips: " + e.getMessage());
            return gson.toJson(errorResponse);
        }
    }
}

