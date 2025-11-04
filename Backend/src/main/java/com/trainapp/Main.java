package com.trainapp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trainapp.DTO.ConnectionDTO;
import com.trainapp.utils.LocalTimeAdapter;
import com.trainapp.model.Connection;
import com.trainapp.console;  // Assuming 'console' is a class that contains the filtering logic
import java.time.LocalTime;
import java.util.List;
import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        // Initialize the console class
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

        // Endpoint to search for connections
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

        post("/filterConnections", (req, res) -> {
            res.type("application/json");

            // Parse the request body
            String body = req.body();
            System.out.println("Received body: " + body); // helpful for debugging

            var jsonObject = gson.fromJson(body, com.google.gson.JsonObject.class);

            // Extract connections list
            var connectionsJson = jsonObject.getAsJsonArray("connections");
            var filtersJson = jsonObject.getAsJsonObject("filters");

            // Deserialize the connections array into List<Connection>
            List<Connection> connections = gson.fromJson(
                    connectionsJson,
                    new com.google.gson.reflect.TypeToken<List<Connection>>() {}.getType()
            );

            // Defensive check
            if (connections == null) {
                res.status(400);
                return gson.toJson("Error: connections list is missing or invalid");
            }

            // Extract filters safely
            String departureDay = filtersJson.has("departureDay") && !filtersJson.get("departureDay").getAsString().isEmpty()
                    ? filtersJson.get("departureDay").getAsString().substring(0, 3)
                    : null;
            String arrivalDay = filtersJson.has("arrivalDay") && !filtersJson.get("arrivalDay").getAsString().isEmpty()
                    ? filtersJson.get("arrivalDay").getAsString().substring(0, 3)
                    : null;

            String departureTime = filtersJson.has("departureTime") ? filtersJson.get("departureTime").getAsString() : null;
            String arrivalTime = filtersJson.has("arrivalTime") ? filtersJson.get("arrivalTime").getAsString() : null;
            String ticketClass = filtersJson.has("ticketClass") ? filtersJson.get("ticketClass").getAsString() : null;

            int maxPrice = filtersJson.has("maxPrice") && !filtersJson.get("maxPrice").getAsString().isEmpty()
                    ? filtersJson.get("maxPrice").getAsInt()
                    : Integer.MAX_VALUE;
            int maxDuration = filtersJson.has("maxDuration") && !filtersJson.get("maxDuration").getAsString().isEmpty()
                    ? filtersJson.get("maxDuration").getAsInt()
                    : Integer.MAX_VALUE;

            // Apply your filtering logic
            List<Connection> filteredConnections = console.SelectedConnection(
                    connections,
                    maxPrice,
                    maxDuration,
                    ticketClass,
                    departureTime,
                    arrivalTime,
                    departureDay,
                    arrivalDay
            );

            // Convert back to DTO for frontend
            List<ConnectionDTO> filteredConnectionsDTO = filteredConnections.stream()
                    .map(c -> new ConnectionDTO(c, false))
                    .toList();

            return gson.toJson(filteredConnectionsDTO);
        });


    }
}
