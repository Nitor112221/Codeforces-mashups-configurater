package org.nitor112221.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Database extends SqlQueries {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    public static void Conn() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.h2.Driver");
        conn = DriverManager.getConnection("jdbc:h2:mem:db", "sa", "sa");
        CreateTables();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                CloseDB();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }));
    }

    public static void CreateTables() throws ClassNotFoundException, SQLException {
        statmt = conn.createStatement();
        statmt.execute(CREATE_CONTESTS);
        statmt.execute(CREATE_PROBLEMS);
        statmt.execute(CREATE_TAGS);
        statmt.execute(CREATE_PROBLEM_TAGS);
        statmt.execute(CREATE_INDEXES);
    }

    public static void CloseDB() throws ClassNotFoundException, SQLException {
        if (resSet != null) resSet.close();
        if (statmt != null) statmt.close();
        if (conn != null) conn.close();
    }

}
