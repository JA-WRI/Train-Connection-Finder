package com.trainapp.repository;

import java.sql.Connection;
import java.sql.SQLException;

public interface Repository<T, ID> {

    ID insert(java.sql.Connection conn, T entity) throws SQLException;

    T findById(java.sql.Connection conn, ID id) throws SQLException;

    default T select(java.sql.Connection conn, ID id) throws SQLException {
        return findById(conn, id);
    }
}

