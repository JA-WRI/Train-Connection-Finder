package com.trainapp.API;

import com.google.gson.Gson;
import com.trainapp.controller.BookingController;
import com.trainapp.controller.ConnectionController;
import com.trainapp.controller.TripHistoryController;
import static spark.Spark.*;

public class APIRoutes {
    
    private ConnectionController connectionController;
    private BookingController bookingController;
    private TripHistoryController tripHistoryController;
    
    public APIRoutes(Gson gson) {
        this.connectionController = new ConnectionController(gson);
        this.bookingController = new BookingController(gson);
        this.tripHistoryController = new TripHistoryController(gson);
    }
    
    //seting all routes and CORS configuration
    public void configureRoutes() {
        // Enable basic CORS (frontend on different port)
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json");
        });

        // Connection endpoints
        get("/searchConnections", connectionController::searchConnections);
        post("/filterConnections", connectionController::filterConnections);

        // Booking endpoints
        post("/bookConnection", bookingController::bookTrip);
        post("/addTraveler", bookingController::addTraveler);

        // Trip history endpoints
        post("/getUserTrips", tripHistoryController::viewTrip);
    }
}

