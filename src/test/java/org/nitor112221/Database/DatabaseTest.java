package org.nitor112221.Database;

import org.junit.jupiter.api.*;
import org.nitor112221.dto.Contest;
import org.nitor112221.dto.ContestTypeEnum;
import org.nitor112221.dto.Problem;
import org.nitor112221.dto.TagEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционные тесты для Database.
 * Используются H2 in-memory, поэтому каждый тест запускается в чистой БД.
 */
class DatabaseTest {

    @BeforeEach
    void setUp() throws ClassNotFoundException, SQLException {
        Database.Conn();
    }

    @AfterEach
    void tearDown() throws SQLException {
        Database.CloseDB();
    }

    @Test
    void shouldLoadTags() throws SQLException {
        assertNotNull(TagEnum.DP.getId());
        assertTrue(TagEnum.DP.getId() > 0);
        assertNotNull(TagEnum.STRINGS.getId());
        assertNotNull(TagEnum.BINARY_SEARCH.getId());
    }

    // ==============================
    // Тесты для загрузки задач (Problems)
    // ==============================

    @Test
    void shouldLoadProblems() throws SQLException {
        Contest contest = new Contest(2244, ContestTypeEnum.DIV2);
        ArrayList<Contest> contests = new ArrayList<>();
        contests.add(contest);
        Database.LoadContests(contests);

        ArrayList<TagEnum> tags = new ArrayList<>();
        tags.add(TagEnum.DP);
        tags.add(TagEnum.STRINGS);

        Problem problem = new Problem(2244, "E", "Маша и гирлянда", 2000);
        problem.setTags(tags);

        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(problem);

        Database.LoadProblems(problems);

        String query = "SELECT * FROM problems WHERE contest_id = 2244 AND problem_index = 'E'";
        ArrayList<Problem> result = Database.ExecuteSearch(query);

        assertEquals(1, result.size());
        Problem found = result.getFirst();
        assertEquals(2244, found.getContestId());
        assertEquals("E", found.getIndex());
        assertEquals("Маша и гирлянда", found.getName());
        assertEquals(2000, found.getRating());

        assertNotNull(found.getTags());
        assertEquals(2, found.getTags().size());
        assertTrue(found.getTags().contains(TagEnum.DP));
        assertTrue(found.getTags().contains(TagEnum.STRINGS));
    }

    @Test
    void shouldSkipProblemWithoutRating() throws SQLException {
        Contest contest = new Contest(2244, ContestTypeEnum.DIV2);
        ArrayList<Contest> contests = new ArrayList<>();
        contests.add(contest);
        Database.LoadContests(contests);

        ArrayList<TagEnum> tags = new ArrayList<TagEnum>();
        tags.add(TagEnum.DP);
        tags.add(TagEnum.STRINGS);

        Problem problem = new Problem(2244, "E", "Маша и гирлянда", tags);
        problem.setTags(new ArrayList<>());

        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(problem);

        Database.LoadProblems(problems);

        String query = "SELECT * FROM problems WHERE contest_id = 2244 AND problem_index = 'E'";
        ArrayList<Problem> result = Database.ExecuteSearch(query);

        assertEquals(0, result.size());
    }

    @Test
    void shouldExecuteSearch() throws SQLException {
        Contest contest = new Contest(2244, ContestTypeEnum.DIV2);
        ArrayList<Contest> contests = new ArrayList<>();
        contests.add(contest);
        Database.LoadContests(contests);

        Problem problem = new Problem(2244, "E", "Маша и гирлянда", 2000);
        problem.setTags(new ArrayList<>());
        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(problem);
        Database.LoadProblems(problems);

        String query = "SELECT * FROM problems WHERE problem_index = 'E'";
        ArrayList<Problem> result = Database.ExecuteSearch(query);

        assertEquals(1, result.size());
        assertEquals("E", result.getFirst().getIndex());
    }

    @Test
    void shouldHandleEmptySearch() throws SQLException {
        String query = "SELECT * FROM problems WHERE problem_index = 'X'";
        ArrayList<Problem> result = Database.ExecuteSearch(query);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ==============================
    // Тесты для загрузки контестов
    // ==============================

    @Test
    void shouldLoadContests() throws SQLException {
        Contest contest = new Contest(2248, ContestTypeEnum.DIV2);
        ArrayList<Contest> contests = new ArrayList<>();
        contests.add(contest);

        Database.LoadContests(contests);

        // Проверяем, что контест появился в таблице contests
        try (Statement stmt = Database.conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM contests WHERE id = 2248")) {
            rs.next();
            int count = rs.getInt("cnt");
            assertEquals(1, count);
        }

        try (Statement stmt = Database.conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT type FROM contests WHERE id = 2248")) {
            rs.next();
            String type = rs.getString("type");
            assertEquals("Div. 2", type);
        }
    }

    // ==============================
    // Дополнительные тесты (граничные случаи)
    // ==============================

    @Test
    void shouldNotInsertProblemWithoutContest() throws SQLException {
        // Пытаемся вставить задачу без предварительного добавления контеста
        Problem problem = new Problem(2244, "E", "Маша и гирлянда", 2000);
        problem.setTags(new ArrayList<>());
        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(problem);

        assertThrows(SQLException.class, () -> Database.LoadProblems(problems));
    }

    @Test
    void shouldHandleDuplicateProblem() throws SQLException {
        Contest contest = new Contest(2244, ContestTypeEnum.DIV2);
        ArrayList<Contest> contests = new ArrayList<>();
        contests.add(contest);
        Database.LoadContests(contests);

        Problem problem = new Problem(2244, "E", "Маша и гирлянда", 2000);
        problem.setTags(new ArrayList<>());
        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(problem);
        Database.LoadProblems(problems);

        assertThrows(SQLException.class, () -> Database.LoadProblems(problems));

        String query = "SELECT * FROM problems WHERE contest_id = 2244 AND problem_index = 'E'";
        ArrayList<Problem> result = Database.ExecuteSearch(query);
        assertEquals(1, result.size());
    }
}