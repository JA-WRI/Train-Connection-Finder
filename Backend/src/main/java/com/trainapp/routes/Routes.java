package com.trainapp.routes;

import com.google.gson.Gson;
import com.trainapp.controller.BookingController;
import com.trainapp.controller.ConnectionController;
import static spark.Spark.*;

public class Routes {
    
    private ConnectionController connectionController;
    private BookingController bookingController;
    
    public Routes(Gson gson) {
        this.connectionController = new ConnectionController(gson);
        this.bookingController = new BookingController(gson);
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
        get("/searchConnections", connectionController::handleSearchConnections);
        post("/filterConnections", connectionController::handleFilterConnections);

        // Booking endpoints
        post("/bookConnection", bookingController::handleBookConnection);
        post("/addTraveler", bookingController::handleAddTraveler);
    }
}

