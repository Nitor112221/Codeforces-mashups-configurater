package org.nitor112221.integration;

import org.junit.jupiter.api.*;
import org.nitor112221.Database.Database;
import org.nitor112221.core.MashupBuilder;
import org.nitor112221.dto.Contest;
import org.nitor112221.dto.ContestTypeEnum;
import org.nitor112221.dto.Mashup;
import org.nitor112221.dto.Problem;
import org.nitor112221.filters.FilterProblemFromXtoYFromDivZ;
import org.nitor112221.filters.FilterProblemWithRatingFromXToY;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MashupBuilderIntegrationTest {

    @BeforeAll
    void initDb() throws Exception {
        Database.Conn();
        insertTestData();
    }

    @AfterAll
    void closeDb() throws SQLException {
        Database.CloseDB();
    }

    private void insertTestData() throws SQLException {
        Contest c1 = new Contest(1, ContestTypeEnum.DIV1);
        Contest c2 = new Contest(2, ContestTypeEnum.DIV2);
        Database.LoadContests(new ArrayList<>(List.of(c1, c2)));

        List<Problem> problems = List.of(
                new Problem(1, "A", "Div1 A", 1500),
                new Problem(1, "B", "Div1 B", 2000),
                new Problem(2, "A", "Div2 A", 1200),
                new Problem(2, "B", "Div2 B", 800)
        );
        for (Problem p : problems) {
            p.setTags(new ArrayList<>());
        }
        Database.LoadProblems(new ArrayList<>(problems));
    }

    @Test
    void shouldBuildMashupWithRatingFilter() {
        MashupBuilder builder = new MashupBuilder();
        FilterProblemWithRatingFromXToY filter = new FilterProblemWithRatingFromXToY();
        filter.setX(1000);
        filter.setY(1800);
        builder.setFilter(filter);
        builder.setNumProblem(2);

        Mashup mashup = builder.build();

        assertNotNull(mashup);
        assertEquals(2, mashup.getProblems().size());
        for (Problem p : mashup.getProblems()) {
            assertTrue(p.getRating() >= 1000 && p.getRating() <= 1800);
        }
    }

    @Test
    void shouldReturnEmptyMashupIfNoProblemsMatch() {
        MashupBuilder builder = new MashupBuilder();
        FilterProblemWithRatingFromXToY filter = new FilterProblemWithRatingFromXToY();
        filter.setX(3000);
        filter.setY(4000);
        builder.setFilter(filter);
        builder.setNumProblem(5);

        Mashup mashup = builder.build();
        assertNotNull(mashup);
        assertTrue(mashup.getProblems().isEmpty());
    }

    @Test
    void shouldLimitResultCount() {
        MashupBuilder builder = new MashupBuilder();
        builder.setNumProblem(1);
        Mashup mashup = builder.build();
        assertEquals(1, mashup.getProblems().size());
    }

    @Test
    void shouldCombineMultipleFilters() {
        MashupBuilder builder = new MashupBuilder();
        FilterProblemWithRatingFromXToY filterRating = new FilterProblemWithRatingFromXToY();
        filterRating.setX(500);
        filterRating.setY(2500);
        builder.setFilter(filterRating);

        FilterProblemFromXtoYFromDivZ filterDiv = new FilterProblemFromXtoYFromDivZ();
        filterDiv.setDiv(ContestTypeEnum.DIV2);
        filterDiv.setX("B");

        builder.setNumProblem(3);

        Mashup mashup = builder.build();
        assertNotNull(mashup);
        assertTrue(mashup.getProblems().size() <= 3);
    }
}