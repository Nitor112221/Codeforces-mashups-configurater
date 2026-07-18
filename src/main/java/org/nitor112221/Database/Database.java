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
                        tagId = rs.getInt(1);
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
                if (problem.getRating() == null) continue;
                try (PreparedStatement pstmt = conn.prepareStatement(INSERT_PROBLEM)) {
                    pstmt.setInt(1, problem.getContestId());
                    pstmt.setString(2, problem.getIndex());
                    pstmt.setString(3, problem.getName());
                    pstmt.setInt(4, problem.getRating());
                    pstmt.executeUpdate();
                }

                for (TagEnum tag : problem.getTags()) {
                    // Связываем задачу с тегом
                    try (PreparedStatement pstmt = conn.prepareStatement(LINK_TAG)) {
                        pstmt.setInt(1, problem.getContestId());
                        pstmt.setString(2, problem.getIndex());
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
                    pstmt.setInt(1, contest.getId());
                    pstmt.setString(2, contest.getType().toString());
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

    public static ArrayList<Problem> ExecuteSearch(String query) throws SQLException{
        ArrayList<Problem> result = new ArrayList<Problem>();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(
                        new Problem(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            GetProblemTags(rs.getInt(1), rs.getString(2))
                        )
                    );
                }
            }
        }

        return result;
    }

    public static ArrayList<TagEnum> GetProblemTags(int contestId, String index) throws SQLException{
        ArrayList<TagEnum> result = new ArrayList<TagEnum>();
        try (PreparedStatement pstmt = conn.prepareStatement(FIND_LINKED_TAGS)) {
            pstmt.setInt(1, contestId);
            pstmt.setString(2, index);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(TagEnum.fromId(rs.getInt(1)));
                }
            }
        }

        return result;
    }

    public static void CloseDB() throws SQLException {
        if (resSet != null) resSet.close();
        if (statmt != null) statmt.close();
        if (conn != null) conn.close();
    }

}
