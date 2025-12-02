package com.trainapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trainapp.API.APIRoutes;
import com.trainapp.utils.LocalTimeAdapter;
import com.trainapp.utils.CSVReader;
import java.time.LocalTime;
import static spark.Spark.*;


public class Main {
    public static void main(String[] args) {
        // Initialize database
        System.out.println("Starting Railway System Database Setup...");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        CSVReader.loadRoutesFromCSVToDatabase("data/eu_rail_network.csv");
        System.out.println("Database will be at: " + new java.io.File("../railway_system.db").getAbsolutePath());
        System.out.println("Database setup complete YIPPEE YIPPEE");
        
        // Configure Spark
        port(4567);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();

        // Configure routes
        APIRoutes APIRoutes = new APIRoutes(gson);
        APIRoutes.configureRoutes();
    }
}
