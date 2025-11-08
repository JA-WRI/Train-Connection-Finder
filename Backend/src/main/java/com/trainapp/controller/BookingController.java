package com.trainapp.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.trainapp.DTO.ConnectionDTO;
import com.trainapp.services.BookingService;
import com.trainapp.utils.ConnectionMapper;
import spark.Request;
import spark.Response;

public class BookingController {
    
    private BookingService bookingService;
    private ConnectionMapper connectionMapper;
    private Gson gson;
    
    public BookingController(Gson gson) {
        this.bookingService = new BookingService();
        this.connectionMapper = new ConnectionMapper();
        this.gson = gson;
    }

    public String handleBookConnection(Request req, Response res) {
        res.type("application/json");
        
        try {
            String body = req.body();
            System.out.println("Booking request: " + body);

            JsonObject jsonObject = gson.fromJson(body, JsonObject.class);

            // Extract request data
            var connectionJson = jsonObject.getAsJsonObject("connection");
            ConnectionDTO connectionDTO = gson.fromJson(connectionJson, ConnectionDTO.class);

            var userJson = jsonObject.getAsJsonObject("user");
            String userName = userJson.get("name").getAsString();
            int userAge = userJson.get("age").getAsInt();
            String userId = userJson.get("id").getAsString();

            // Convert DTO to domain model and call service
            var connection = connectionMapper.convertDTOToConnection(connectionDTO);
            int reservationId = bookingService.bookConnection(connection, userName, userAge, userId);

            // Return success response
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("reservationId", String.valueOf(reservationId));
            response.addProperty("message", "Booking confirmed successfully");

            res.status(200);
            return gson.toJson(response);

        } catch (Exception e) {
            System.err.println("Booking error: " + e.getMessage());
            e.printStackTrace();
            res.status(500);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("error", "Failed to book connection: " + e.getMessage());
            return gson.toJson(errorResponse);
        }
    }

    public String handleAddTraveler(Request req, Response res) {
        res.type("application/json");
        
        try {
            String body = req.body();
            System.out.println("Add traveler request: " + body);

            JsonObject jsonObject = gson.fromJson(body, JsonObject.class);

            // Extract request data
            int bookerReservationId = jsonObject.get("bookerReservationId").getAsInt();

            var connectionJson = jsonObject.getAsJsonObject("connection");
            ConnectionDTO connectionDTO = gson.fromJson(connectionJson, ConnectionDTO.class);

            var travelerJson = jsonObject.getAsJsonObject("traveler");
            String travelerName = travelerJson.get("name").getAsString();
            int travelerAge = travelerJson.get("age").getAsInt();
            String travelerId = travelerJson.get("id").getAsString();

            // Convert DTO to domain model and call service
            var connection = connectionMapper.convertDTOToConnection(connectionDTO);
            int travelerReservationId = bookingService.addTraveler(
                    bookerReservationId, 
                    travelerName, 
                    travelerAge, 
                    travelerId, 
                    connection
            );

            // Return success response
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("reservationId", String.valueOf(travelerReservationId));
            response.addProperty("message", "Traveler added successfully");

            res.status(200);
            return gson.toJson(response);

        } catch (Exception e) {
            System.err.println("Add traveler error: " + e.getMessage());
            e.printStackTrace();
            res.status(500);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("error", "Failed to add traveler: " + e.getMessage());
            return gson.toJson(errorResponse);
        }
    }
}

