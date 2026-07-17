package org.nitor112221.Database;

import org.nitor112221.dto.Contest;
import org.nitor112221.dto.Problem;
import org.nitor112221.dto.TagEnum;

import java.sql.*;
import java.util.ArrayList;


public class Database extends SqlQueries {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    public static void Conn() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.h2.Driver");
        conn = DriverManager.getConnection("jdbc:h2:mem:db", "sa", "sa");
        CreateTables();
        LoadTags();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                CloseDB();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }));
    }

    public static void CreateTables() throws SQLException {
        statmt = conn.createStatement();
        statmt.execute(CREATE_CONTESTS);
        statmt.execute(CREATE_PROBLEMS);
        statmt.execute(CREATE_TAGS);
        statmt.execute(CREATE_PROBLEM_TAGS);
        statmt.execute(CREATE_INDEXES);
    }
    public static void LoadTags() throws SQLException {
        conn.setAutoCommit(false);
        try {
            for (TagEnum tag : TagEnum.values()) {
                try (PreparedStatement pstmt = conn.prepareStatement(INSERT_TAG)) {
                    pstmt.setString(1, tag.toString());
                    pstmt.executeUpdate();
                }

                int tagId;
                try (PreparedStatement pstmt = conn.prepareStatement(GET_TAG_ID)) {
                    pstmt.setString(1, tag.toString());
                    try (ResultSet rs = pstmt.executeQuery()) {
                        rs.next();
                        tagId = rs.getInt("id");
                    }
                }
                tag.setId(tagId);
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public static void LoadProblems(ArrayList<Problem> problems) throws SQLException {
        conn.setAutoCommit(false);
        try {
            for (Problem problem : problems) {
                if (problem.rating == null) continue;
                try (PreparedStatement pstmt = conn.prepareStatement(INSERT_PROBLEM)) {
                    pstmt.setInt(1, problem.contestId);
                    pstmt.setString(2, problem.index);
                    pstmt.setString(3, problem.name);
                    pstmt.setInt(4, problem.rating);
                    pstmt.executeUpdate();
                }
                for (TagEnum tag : problem.tags) {
                    // Связываем задачу с тегом
                    try (PreparedStatement pstmt = conn.prepareStatement(LING_TAG)) {
                        pstmt.setInt(1, problem.contestId);
                        pstmt.setString(2, problem.index);
                        pstmt.setInt(3, tag.getId());
                        pstmt.executeUpdate();
                    }
                }
            }
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public static void LoadContests(ArrayList<Contest> contests) throws SQLException {
        conn.setAutoCommit(false);
        try {
            for (Contest contest : contests) {
                try (PreparedStatement pstmt = conn.prepareStatement(INSERT_CONTEST)) {
                    pstmt.setInt(1, contest.id);
                    pstmt.setString(1, contest.type.toString());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public static void CloseDB() throws SQLException {
        if (resSet != null) resSet.close();
        if (statmt != null) statmt.close();
        if (conn != null) conn.close();
    }

}
