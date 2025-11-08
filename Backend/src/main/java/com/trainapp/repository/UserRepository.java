package com.trainapp.repository;

import java.sql.*;

public class UserRepository {

    public int insertUser(java.sql.Connection conn, String firstName, String lastName, int age, int isBooker) throws SQLException {
        String sql = "INSERT INTO users (first_name, last_name, age, is_booker) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, age);
            pstmt.setInt(4, isBooker);
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert user");
    }

    public int insertBooker(java.sql.Connection conn, String name, int age, String userId) throws SQLException {
        String[] nameParts = name.trim().split("\\s+", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : name;
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        return insertUser(conn, firstName, lastName, age, 1);
    }

    public int insertTraveler(java.sql.Connection conn, String name, int age, String userId) throws SQLException {
        String[] nameParts = name.trim().split("\\s+", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : name;
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        return insertUser(conn, firstName, lastName, age, 0);
    }
}

