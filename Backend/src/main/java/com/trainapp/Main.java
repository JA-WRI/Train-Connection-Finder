package com.trainapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trainapp.DTO.ConnectionDTO;
import com.trainapp.utils.LocalTimeAdapter;
import com.trainapp.model.Connection;

import java.time.LocalTime;
import java.util.List;
import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        console console = new console();
        console.startProgram();

        port(4567);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();


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


        get("/searchConnections", (req, res) -> {
            String departureCity = req.queryParams("departureCity");
            String arrivalCity = req.queryParams("arrivalCity");


            List<Connection> connections = console.getConnection(departureCity, arrivalCity);

            List<ConnectionDTO> connectionsDTO = connections.stream()
                    .map(c -> new ConnectionDTO(c, false))
                    .toList();
            System.out.println("Returning " + connections.size() + " connections");

            return gson.toJson(connectionsDTO);
        });

        //make a request for the user to select a trip and create a trip

        //make a request for user to add travellers
    }


}