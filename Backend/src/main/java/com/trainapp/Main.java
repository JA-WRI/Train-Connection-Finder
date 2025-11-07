package com.trainapp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trainapp.DTO.ConnectionDTO;
import com.trainapp.DTO.RouteDTO;
import com.trainapp.utils.LocalTimeAdapter;
import com.trainapp.utils.filterConnections;
import com.trainapp.model.Connection;
import com.trainapp.model.Route;
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
            var sortJson = jsonObject.has("sort") && jsonObject.get("sort").isJsonObject()
                    ? jsonObject.getAsJsonObject("sort")
                    : null;

            java.util.List<ConnectionDTO> connectionDTOs = null;
            java.util.List<Connection> connections = new java.util.ArrayList<>();
            try {
                // Deserialize as DTOs (shape sent by frontend) then map to domain model
                connectionDTOs = gson.fromJson(
                        connectionsJson,
                        new com.google.gson.reflect.TypeToken<java.util.List<ConnectionDTO>>() {}.getType()
                );
                if (connectionDTOs != null) {
                    for (ConnectionDTO dto : connectionDTOs) {
                        java.util.List<Route> routes = new java.util.ArrayList<>();
                        if (dto.routes != null) {
                            for (RouteDTO rdto : dto.routes) {
                                Route r = new Route(
                                        null,
                                        rdto.departureCity,
                                        rdto.arrivalCity,
                                        java.time.LocalTime.parse(rdto.departureTime),
                                        java.time.LocalTime.parse(rdto.arrivalTime),
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
                                        java.time.LocalTime.parse(dto.departureTime),
                                        java.time.LocalTime.parse(dto.arrivalTime),
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
            } catch (Exception e) {
                System.err.println("Failed to parse/mapping connections: " + e.getMessage());
                e.printStackTrace();
                // Best-effort: return original list without filtering/sorting
                res.status(200);
                return gson.toJson(connectionDTOs != null ? connectionDTOs : java.util.Collections.emptyList());
            }

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
            List<Connection> filteredConnections;
            try {
                filteredConnections = console.SelectedConnection(
                        connections,
                        maxPrice,
                        maxDuration,
                        ticketClass,
                        departureTime,
                        arrivalTime,
                        departureDay,
                        arrivalDay
                );
            } catch (Exception e) {
                System.err.println("Filtering failed: " + e.getMessage());
                e.printStackTrace();
                filteredConnections = connections; // fallback to original list
            }

            // Apply sorting if provided
            if (sortJson != null && sortJson.has("criteria") && sortJson.has("order")) {
                String criteria = sortJson.get("criteria").getAsString();
                String order = sortJson.get("order").getAsString();

                filterConnections sorter = new filterConnections();

                switch (criteria) {
                    case "duration":
                        if ("asc".equalsIgnoreCase(order)) {
                            filteredConnections = sorter.AscenSortDuration(filteredConnections);
                        } else {
                            filteredConnections = sorter.DescenSortDuration(filteredConnections);
                        }
                        break;
                    case "priceFirst":
                        if ("asc".equalsIgnoreCase(order)) {
                            filteredConnections = sorter.sortByFirstClassPriceAscending(filteredConnections);
                        } else {
                            filteredConnections = sorter.sortByFirstClassPriceDescending(filteredConnections);
                        }
                        break;
                    case "priceSecond":
                        if ("asc".equalsIgnoreCase(order)) {
                            filteredConnections = sorter.sortBySecondClassPriceAscending(filteredConnections);
                        } else {
                            filteredConnections = sorter.sortBySecondClassPriceDescending(filteredConnections);
                        }
                        break;
                    case "departureTime":
                        // only ascending defined; reverse for desc
                        filteredConnections = sorter.sortByDepartureTime(filteredConnections);
                        if ("desc".equalsIgnoreCase(order)) {
                            java.util.Collections.reverse(filteredConnections);
                        }
                        break;
                    case "routesCount":
                        // only ascending defined; reverse for desc
                        filteredConnections = sorter.sortByNumRoutes(filteredConnections);
                        if ("desc".equalsIgnoreCase(order)) {
                            java.util.Collections.reverse(filteredConnections);
                        }
                        break;
                    default:
                        // no-op for unknown criteria
                        break;
                }
            }

            // Convert back to DTO for frontend
            List<ConnectionDTO> filteredConnectionsDTO;
            try {
                filteredConnectionsDTO = filteredConnections.stream()
                        .map(c -> new ConnectionDTO(c, false))
                        .toList();
            } catch (Exception e) {
                System.err.println("DTO mapping failed: " + e.getMessage());
                e.printStackTrace();
                // fallback to original DTOs if mapping fails
                filteredConnectionsDTO = connectionDTOs != null ? connectionDTOs : java.util.Collections.emptyList();
            }

            return gson.toJson(filteredConnectionsDTO);
        });


    }
}
