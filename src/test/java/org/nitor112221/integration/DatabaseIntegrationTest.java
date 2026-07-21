package org.nitor112221.integration;

import org.junit.jupiter.api.*;
import org.nitor112221.Database.Database;
import org.nitor112221.dto.Contest;
import org.nitor112221.dto.ContestTypeEnum;
import org.nitor112221.dto.Problem;
import org.nitor112221.dto.TagEnum;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseIntegrationTest {

    @BeforeAll
    void initDb() throws Exception {
        Database.Conn();
    }

    @AfterAll
    void closeDb() throws SQLException {
        Database.CloseDB();
    }

    @BeforeEach
    void clearTables() throws SQLException {
        try (Statement stmt = Database.conn.createStatement()) {
            stmt.execute("DELETE FROM problem_tags");
            stmt.execute("DELETE FROM problems");
            stmt.execute("DELETE FROM contests");
        }
    }

    @Test
    void shouldInsertAndRetrieveContest() throws SQLException {
        Contest contest = new Contest(100, ContestTypeEnum.DIV1);
        ArrayList<Contest> list = new ArrayList<>();
        list.add(contest);
        Database.LoadContests(list);

        try (Statement stmt = Database.conn.createStatement();
             var rs = stmt.executeQuery("SELECT * FROM contests WHERE id = 100")) {
            assertTrue(rs.next());
            assertEquals(100, rs.getInt("id"));
            assertEquals("Div. 1", rs.getString("type"));
        }
    }

    @Test
    void shouldInsertProblemWithTagsAndRetrieveThem() throws SQLException {
        Contest contest = new Contest(2244, ContestTypeEnum.DIV2);
        ArrayList<Contest> contests = new ArrayList<>();
        contests.add(contest);
        Database.LoadContests(contests);

        Problem problem = new Problem(2244, "E", "Маша и гирлянда", 2000);
        List<TagEnum> tags = List.of(TagEnum.DP, TagEnum.STRINGS);
        problem.setTags(new ArrayList<>(tags));

        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(problem);
        Database.LoadProblems(problems);

        String query = "SELECT * FROM problems WHERE contest_id = 2244";
        ArrayList<Problem> result = Database.ExecuteSearch(query);
        assertEquals(1, result.size());

        Problem found = result.getFirst();
        assertEquals("E", found.getIndex());
        assertEquals(2000, found.getRating());
        assertNotNull(found.getTags());
        assertEquals(2, found.getTags().size());
        assertTrue(found.getTags().containsAll(tags));
    }

    @Test
    void shouldSkipProblemWithoutRating() throws SQLException {
        Contest contest = new Contest(2244, ContestTypeEnum.DIV2);
        ArrayList<Contest> contests = new ArrayList<>();
        contests.add(contest);
        Database.LoadContests(contests);

        Problem problem = new Problem(2244, "E", "Маша и гирлянда", null);
        problem.setTags(new ArrayList<>());
        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(problem);
        Database.LoadProblems(problems);

        String query = "SELECT * FROM problems WHERE contest_id = 2244";
        ArrayList<Problem> result = Database.ExecuteSearch(query);
        assertEquals(0, result.size());
    }

    @Test
    void shouldNotInsertProblemWithoutContest() {
        Problem problem = new Problem(2244, "E", "Нет контеста", 1500);
        problem.setTags(new ArrayList<>());
        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(problem);

        assertThrows(SQLException.class, () -> Database.LoadProblems(problems));
    }

    @Test
    void shouldFindProblemsByTag() throws SQLException {
        Contest contest = new Contest(1, ContestTypeEnum.DIV1);
        ArrayList<Contest> contests = new ArrayList<>();
        contests.add(contest);
        Database.LoadContests(contests);

        Problem p1 = new Problem(1, "A", "Задача A", 1000);
        p1.setTags(new ArrayList<>(List.of(TagEnum.DP)));
        Problem p2 = new Problem(1, "B", "Задача B", 1200);
        p2.setTags(new ArrayList<>(List.of(TagEnum.STRINGS, TagEnum.MATH)));
        Problem p3 = new Problem(1, "C", "Задача C", 1400);
        p3.setTags(new ArrayList<>(List.of(TagEnum.DP, TagEnum.MATH)));

        ArrayList<Problem> problems = new ArrayList<>(List.of(p1, p2, p3));
        Database.LoadProblems(problems);

        String sql = "SELECT DISTINCT p.* FROM problems p " +
                "JOIN problem_tags pt ON p.contest_id = pt.contest_id AND p.problem_index = pt.problem_index " +
                "JOIN tags t ON pt.tag_id = t.id " +
                "WHERE t.name = 'dp'";
        ArrayList<Problem> result = Database.ExecuteSearch(sql);
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListForNonExistentSearch() throws SQLException {
        String query = "SELECT * FROM problems WHERE contest_id = -1";
        ArrayList<Problem> result = Database.ExecuteSearch(query);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}