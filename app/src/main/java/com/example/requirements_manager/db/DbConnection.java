package com.example.requirements_manager.db;

import android.os.Environment;

import com.example.requirements_manager.db.exceptions.DbException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DbConnection {

    private static Connection conn = null;

    public static Connection getConnection() {

        if (conn == null) {

            try {

                Properties props = loadProperties();

                String url = props.getProperty("dburl");

                conn = DriverManager.getConnection(url, props);

            } catch (SQLException e) {
                throw new DbException(e);
            }

        }

        return conn;

    }

    private static Properties loadProperties() {

        try (FileInputStream fs = new FileInputStream("db.properties")) {

            Properties props = new Properties();

            props.load(fs);

            return props;

        } catch (IOException e) {

            throw new DbException(e);

        }

    }

    public static void startDB() {

        Statement stmt = null;

        try {

            String sql = "CALL reset_tables";

            stmt = getConnection().createStatement();

            stmt.executeUpdate(sql);

            System.out.println("Database restarted with sucess");

        } catch (SQLException e) {
            System.out.println("Exception found during restart DB: " + e.getMessage());
        } finally {
            closeStatement(stmt);
        }

    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e);
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new DbException(e);
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException(e);
            }
        }
    }

}

