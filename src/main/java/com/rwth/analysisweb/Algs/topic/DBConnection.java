package com.rwth.analysisweb.Algs.topic;

import java.sql.*;

public class DBConnection {
    public Connection conn;
    public Statement statement;
    private final String db;

    public DBConnection(String database) {
        this.db = database;
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/" + database
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try {
            this.conn = DriverManager.getConnection(DB_URL, "root", "19951015rR!");
            this.statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet get(String sql) throws SQLException {
        return statement.executeQuery(sql);
    }

    public void dropTableIfExits(String table) throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS " + table);
    }

    public void createTable(String sql, String table) throws SQLException {
        dropTableIfExits(table);
        statement.executeUpdate(sql);
    }

}
