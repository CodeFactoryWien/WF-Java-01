package com.blackwhite.ctrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbController {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel";
    private static final String DB_User = "root";
    private static final String DB_Pass = "";
    private static DbController instance;
    private Connection conn;

    private DbController() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, DB_User, DB_Pass);
        conn.setReadOnly(false);
    }

    public Connection getConnection() {
        return conn;
    }

    public static DbController getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            setInstance(new DbController());
        }
        return instance;
    }

    private static void setInstance(DbController instance) {
        DbController.instance = instance;
    }
}
