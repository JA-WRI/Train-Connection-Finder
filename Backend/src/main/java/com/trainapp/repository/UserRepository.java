package com.trainapp.repository;

import com.trainapp.model.User;
import java.sql.*;

public class UserRepository implements Repository<User, Integer> {

    @Override
    public Integer insert(java.sql.Connection conn, User entity) throws SQLException {
        int isBooker = entity.isBooker() ? 1 : 0;
        return insertUser(conn, entity.getFirstName(), entity.getLastName(), entity.getAge(), isBooker);
    }

    @Override
    public User findById(java.sql.Connection conn, Integer id) throws SQLException {
        String sql = "SELECT user_id, first_name, last_name, age, is_booker FROM users WHERE user_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        rs.getInt("age"),
                        rs.getInt("is_booker") == 1
                    );
                    user.setUserId(String.valueOf(rs.getInt("user_id")));
                    return user;
                }
            }
        }
        return null;
    }

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
}

