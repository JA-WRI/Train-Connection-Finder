package database;

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
            CREATE TABLE IF NOT EXISTS users (
                user_id TEXT PRIMARY KEY,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                age INTEGER
            )
            """,

                """
            CREATE TABLE IF NOT EXISTS trips (
                trip_id INTEGER PRIMARY KEY AUTOINCREMENT,
                booker_id TEXT NOT NULL,
                FOREIGN KEY (booker_id) REFERENCES users(user_id)
            )
            """,

                """
            CREATE TABLE IF NOT EXISTS tickets (
                ticket_id INTEGER PRIMARY KEY AUTOINCREMENT,
                trip_id INTEGER NOT NULL,
                user_id TEXT NOT NULL,
                route_id TEXT NOT NULL,
                departure_date TEXT NOT NULL,
                price REAL NOT NULL,
                FOREIGN KEY (trip_id) REFERENCES trips(trip_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id),
                FOREIGN KEY (route_id) REFERENCES routes(route_id)
            )
            """,

                """
            CREATE TABLE IF NOT EXISTS trip_passengers (
                trip_id INTEGER NOT NULL,
                user_id TEXT NOT NULL,
                PRIMARY KEY (trip_id, user_id),
                FOREIGN KEY (trip_id) REFERENCES trips(trip_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id)
            )
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
