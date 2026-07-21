package org.nitor112221.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nitor112221.Database.Database;
import org.nitor112221.dto.Mashup;
import org.nitor112221.dto.Problem;
import org.nitor112221.filters.*;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MashupBuilderTest {

    private MashupBuilder builder;

    @Mock
    private FilterProblemContainsTags filterContains;

    @Mock
    private FilterProblemNotContainsTags filterNotContains;

    @Mock
    private FilterProblemWithRatingFromXToY filterRating;

    @Mock
    private FilterProblemFromXtoYFromDivZ filterIndexDiv;

    @BeforeEach
    void setUp() {
        builder = new MashupBuilder();
    }

    // ==================== Сеттеры ====================

    @Test
    void setNumProblem_shouldSetValueAndReturnThis() {
        MashupBuilder returned = builder.setNumProblem(10);
        assertSame(builder, returned);
        assertEquals(10, builder.getNumProblem());
    }

    @Test
    void setFilter_withContainsTags_shouldSetFieldAndReturnThis() {
        MashupBuilder returned = builder.setFilter(filterContains);
        assertSame(builder, returned);
        assertSame(filterContains, builder.getFilterProblemContainsTags());
    }

    @Test
    void setFilter_withNotContainsTags_shouldSetFieldAndReturnThis() {
        MashupBuilder returned = builder.setFilter(filterNotContains);
        assertSame(builder, returned);
        assertSame(filterNotContains, builder.getFilterProblemNotContainsTags());
    }

    @Test
    void setFilter_withRatingFilter_shouldSetFieldAndReturnThis() {
        MashupBuilder returned = builder.setFilter(filterRating);
        assertSame(builder, returned);
        assertSame(filterRating, builder.getFilterProblemWithRatingFromXToY());
    }

    @Test
    void setFilter_withIndexDivFilter_shouldSetFieldAndReturnThis() {
        MashupBuilder returned = builder.setFilter(filterIndexDiv);
        assertSame(builder, returned);
        assertSame(filterIndexDiv, builder.getFilterProblemFromXtoYFromDivZ());
    }

    // ==================== Тесты toSQL() через рефлексию ====================

    private String invokeToSQL(MashupBuilder builder) throws Exception {
        Method method = MashupBuilder.class.getDeclaredMethod("toSQL");
        method.setAccessible(true);
        return (String) method.invoke(builder);
    }

    @Test
    void toSQL_withNoFilters_shouldReturnSelectWithoutWhere() throws Exception {
        builder.setNumProblem(5);
        String sql = invokeToSQL(builder);

        assertTrue(sql.contains("SELECT DISTINCT"));
        assertTrue(sql.contains("FROM problems as p"));
        assertTrue(sql.contains("LEFT JOIN contests as c ON p.contest_id = c.id"));
        assertTrue(sql.contains("LEFT JOIN problem_tags as pt ON p.contest_id = pt.contest_id AND p.problem_index = pt.problem_index"));
        assertFalse(sql.contains("WHERE"));
        assertTrue(sql.contains("ORDER BY RAND()"));
        assertTrue(sql.contains("LIMIT 5"));
    }

    @Test
    void toSQL_withOneFilter_shouldAddWhereCondition() throws Exception {
        when(filterContains.toSQL()).thenReturn("pt.tag_id = 1");

        builder.setFilter(filterContains);
        builder.setNumProblem(3);

        String sql = invokeToSQL(builder);
        assertTrue(sql.contains("WHERE"));
        assertTrue(sql.contains("pt.tag_id = 1"));
        assertTrue(sql.contains("LIMIT 3"));
    }

    @Test
    void toSQL_withTwoFilters_shouldAddWhereAndAnd() throws Exception {
        when(filterContains.toSQL()).thenReturn("pt.tag_id = 1");
        when(filterRating.toSQL()).thenReturn("p.rating >= 1500");

        builder.setFilter(filterContains);
        builder.setFilter(filterRating);
        builder.setNumProblem(7);

        String sql = invokeToSQL(builder);
        assertTrue(sql.contains("WHERE"), sql);
        assertTrue(sql.contains("pt.tag_id = 1"), sql);
        assertTrue(sql.contains("AND"), sql);
        assertTrue(sql.contains("p.rating >= 1500"), sql);
    }

    @Test
    void toSQL_withFilterReturningNull_shouldBeIgnored() throws Exception {
        when(filterContains.toSQL()).thenReturn(null);
        when(filterRating.toSQL()).thenReturn("p.rating >= 2000");

        builder.setFilter(filterContains);
        builder.setFilter(filterRating);

        String sql = invokeToSQL(builder);
        assertTrue(sql.contains("WHERE"));
        assertTrue(sql.contains("p.rating >= 2000"));
        assertFalse(sql.contains("pt.tag_id"));
    }

    @Test
    void toSQL_withAllFourFilters_shouldCombineAll() throws Exception {
        when(filterContains.toSQL()).thenReturn("pt.tag_id IN (1,2)");
        when(filterNotContains.toSQL()).thenReturn("NOT (pt.tag_id = 3)");
        when(filterIndexDiv.toSQL()).thenReturn("p.problem_index >= 'A' AND p.problem_index < 'D'");
        when(filterRating.toSQL()).thenReturn("p.rating BETWEEN 1200 AND 2000");

        builder.setFilter(filterContains);
        builder.setFilter(filterNotContains);
        builder.setFilter(filterIndexDiv);
        builder.setFilter(filterRating);

        String sql = invokeToSQL(builder);

        assertTrue(sql.contains("pt.tag_id IN (1,2)"));
        assertTrue(sql.contains("NOT (pt.tag_id = 3)"));
        assertTrue(sql.contains("p.problem_index >= 'A' AND p.problem_index < 'D'"));
        assertTrue(sql.contains("p.rating BETWEEN 1200 AND 2000"));
    }

    @Test
    void toSQL_shouldRespectNumProblemInLimit() throws Exception {
        builder.setNumProblem(42);
        String sql = invokeToSQL(builder);
        assertTrue(sql.contains("LIMIT 42"));
    }

    @Test
    void toSQL_defaultNumProblemIsOne() throws Exception {
        String sql = invokeToSQL(builder);
        assertTrue(sql.contains("LIMIT 1"));
    }

    // ==================== Тесты build() ====================

    @Test
    void build_shouldExecuteSearchAndReturnMashupWithProblems() throws Exception {
        List<Problem> expectedProblems = new ArrayList<>();
        Problem p1 = new Problem(1, "A", "Test1", 1000);
        Problem p2 = new Problem(2, "B", "Test2", 1200);
        expectedProblems.add(p1);
        expectedProblems.add(p2);

        try (MockedStatic<Database> dbMock = mockStatic(Database.class)) {
            dbMock.when(() -> Database.ExecuteSearch(anyString()))
                    .thenReturn(expectedProblems);

            when(filterRating.toSQL()).thenReturn("rating >= 1000");
            builder.setFilter(filterRating);
            builder.setNumProblem(5);

            Mashup mashup = builder.build();

            String expectedSQL = invokeToSQL(builder);
            dbMock.verify(() -> Database.ExecuteSearch(expectedSQL), times(1));

            assertNotNull(mashup);
            assertEquals(2, mashup.getProblems().size());
            assertTrue(mashup.getProblems().contains(p1));
            assertTrue(mashup.getProblems().contains(p2));
        }
    }

    @Test
    void build_whenDatabaseThrowsException_shouldReturnEmptyMashupAndLogError() {
        try (MockedStatic<Database> dbMock = mockStatic(Database.class)) {
            dbMock.when(() -> Database.ExecuteSearch(anyString()))
                    .thenThrow(new SQLException("Connection failed"));

            when(filterRating.toSQL()).thenReturn("rating >= 1000");
            builder.setFilter(filterRating);
            builder.setNumProblem(5);

            Mashup mashup = builder.build();

            assertNotNull(mashup);
            assertTrue(mashup.getProblems().isEmpty());

            dbMock.verify(() -> Database.ExecuteSearch(anyString()), times(1));
        }
    }

    @Test
    void build_withNoFilters_shouldCallExecuteSearchWithCorrectSQL() throws Exception {
        try (MockedStatic<Database> dbMock = mockStatic(Database.class)) {
            dbMock.when(() -> Database.ExecuteSearch(anyString()))
                    .thenReturn(new ArrayList<>());

            builder.setNumProblem(10);
            Mashup mashup = builder.build();

            String expectedSQL = invokeToSQL(builder);
            dbMock.verify(() -> Database.ExecuteSearch(expectedSQL), times(1));
            assertNotNull(mashup);
            assertTrue(mashup.getProblems().isEmpty());
        }
    }
}