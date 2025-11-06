package com.trainapp.database;

import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {

        String[] createTables = {
                """
            CREATE TABLE IF NOT EXISTS routes (
                route_id TEXT PRIMARY KEY,
                departure_city TEXT NOT NULL,
                arrival_city TEXT NOT NULL,
                departure_time TIME NOT NULL,
                arrival_time TIME NOT NULL,
                train_type TEXT NOT NULL,
                days_of_operation TEXT NOT NULL,
                first_class_price REAL NOT NULL,
                second_class_price REAL NOT NULL,
                arrives_next_day INTEGER DEFAULT 0
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS connections (
                connection_id INTEGER PRIMARY KEY AUTOINCREMENT,
                departure_city TEXT NOT NULL,
                arrival_city TEXT NOT NULL,
                departure_time TIME NOT NULL,
                arrival_time TIME NOT NULL,
                duration_minutes INTEGER NOT NULL,
                number_of_routes INTEGER NOT NULL,
                price REAL NOT NULL
            );
            """,
                """
            CREATE TABLE IF NOT EXISTS connection_routes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                connection_id INTEGER NOT NULL,
                route_id TEXT NOT NULL,
                route_order INTEGER NOT NULL,
                FOREIGN KEY (connection_id) REFERENCES connections(connection_id) ON DELETE CASCADE,
                FOREIGN KEY (route_id) REFERENCES routes(route_id) ON DELETE RESTRICT,
                CONSTRAINT uq_connection_route_order UNIQUE (connection_id, route_order)
            );
            """,

                """
            CREATE TABLE IF NOT EXISTS users (
                user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                age INTEGER,
                is_booker INTEGER DEFAULT 0
            );
            """,
                """
            CREATE TABLE IF NOT EXISTS trips (
                trip_id INTEGER PRIMARY KEY AUTOINCREMENT,
                booker_id INTEGER NOT NULL,
                connection_id INTEGER NOT NULL,
                FOREIGN KEY (booker_id) REFERENCES users(user_id) ON DELETE CASCADE,
                FOREIGN KEY (connection_id) REFERENCES connections(connection_id) ON DELETE CASCADE
            );
            """,

                """
            CREATE TABLE IF NOT EXISTS tickets (
                ticket_id INTEGER PRIMARY KEY AUTOINCREMENT,
                reservation_id INTEGER NOT NULL,
                user_id INTEGER NOT NULL,
                arrival_city TEXT NOT NULL,
                departure_city TEXT NOT NULL,
                arrival_time TIME NOT NULL,
                departure_time TIME NOT NULL,
                num_of_routes INTEGER NOT NULL,
                duration TEXT NOT NULL,
                price REAL NOT NULL,
                FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id)
            );
            """,
                """
            CREATE TABLE IF NOT EXISTS reservations (
                reservation_id INTEGER PRIMARY KEY AUTOINCREMENT,
                trip_id INTEGER NOT NULL,
                age INTEGER,
                name TEXT,
                FOREIGN KEY (trip_id) REFERENCES trips(trip_id) ON DELETE CASCADE
            );
            
            """
        };

        // Use java.sql.Connection and not the model.Connection
        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            for (String sql : createTables) {
                statement.execute(sql);
            }

            System.out.println("Database tables created successfully!");

        } catch (Exception e) {
            System.out.println("Error creating database tables: " + e.getMessage());
        }
    }
}
