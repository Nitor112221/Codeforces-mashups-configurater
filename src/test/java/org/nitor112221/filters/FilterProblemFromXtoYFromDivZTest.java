package org.nitor112221.filters;

import org.junit.jupiter.api.Test;
import org.nitor112221.dto.ContestTypeEnum;
import static org.junit.jupiter.api.Assertions.*;

class FilterProblemFromXtoYFromDivZTest {

    @Test
    void toSQL_withXandYAndDiv_shouldReturnAllConditions() {
        FilterProblemFromXtoYFromDivZ filter = new FilterProblemFromXtoYFromDivZ();
        filter.setX("A");
        filter.setY("D");
        filter.setDiv(ContestTypeEnum.DIV2);
        String sql = filter.toSQL();
        assertTrue(sql.contains("p.problem_index >= 'A'"));
        assertTrue(sql.contains("p.problem_index < 'E'")); // D → < E
        assertTrue(sql.contains("c.type = 'Div. 2'"));
        assertTrue(sql.contains(" AND "));
    }

    @Test
    void toSQL_withOnlyX_shouldReturnGreaterOrEqual() {
        FilterProblemFromXtoYFromDivZ filter = new FilterProblemFromXtoYFromDivZ();
        filter.setX("C");
        assertEquals("p.problem_index >= 'C'", filter.toSQL());
    }

    @Test
    void toSQL_withOnlyYLetter_shouldReturnLessThanNext() {
        FilterProblemFromXtoYFromDivZ filter = new FilterProblemFromXtoYFromDivZ();
        filter.setY("D");
        assertEquals("p.problem_index < 'E'", filter.toSQL());
    }

    @Test
    void toSQL_withOnlyYWithDigit_shouldReturnLessOrEqual() {
        FilterProblemFromXtoYFromDivZ filter = new FilterProblemFromXtoYFromDivZ();
        filter.setY("D1");
        assertEquals("p.problem_index <= 'D1'", filter.toSQL());
    }

    @Test
    void toSQL_withXAndYLetter_shouldReturnRangeWithUpperExclusive() {
        FilterProblemFromXtoYFromDivZ filter = new FilterProblemFromXtoYFromDivZ();
        filter.setX("B");
        filter.setY("E");
        String sql = filter.toSQL();
        assertEquals("p.problem_index >= 'B' AND p.problem_index < 'F'", sql);
    }

    @Test
    void toSQL_withXAndYWithDigit_shouldReturnRangeInclusive() {
        FilterProblemFromXtoYFromDivZ filter = new FilterProblemFromXtoYFromDivZ();
        filter.setX("D1");
        filter.setY("D3");
        assertEquals("p.problem_index >= 'D1' AND p.problem_index <= 'D3'", filter.toSQL());
    }

    @Test
    void toSQL_withOnlyDiv_shouldReturnDivCondition() {
        FilterProblemFromXtoYFromDivZ filter = new FilterProblemFromXtoYFromDivZ();
        filter.setDiv(ContestTypeEnum.DIV1);
        assertEquals("c.type = 'Div. 1'", filter.toSQL());
    }

    @Test
    void toSQL_withNoParams_shouldReturnNull() {
        FilterProblemFromXtoYFromDivZ filter = new FilterProblemFromXtoYFromDivZ();
        assertNull(filter.toSQL());
    }

    @Test
    void buildUpperBoundCondition_withZ_shouldReturnNextChar() {
        FilterProblemFromXtoYFromDivZ filter = new FilterProblemFromXtoYFromDivZ();
        filter.setY("Z");
        assertEquals("p.problem_index < '['", filter.toSQL());
    }

    @Test
    void toSQL_withInvalidX_shouldReturnNull() {
        FilterProblemFromXtoYFromDivZ filter = new FilterProblemFromXtoYFromDivZ();
        filter.setX("AA");
        filter.setY("D");
        assertNull(filter.toSQL());
    }

    @Test
    void toSQL_withInvalidY_shouldReturnNull() {
        FilterProblemFromXtoYFromDivZ filter = new FilterProblemFromXtoYFromDivZ();
        filter.setX("A");
        filter.setY("D0");
        assertNull(filter.toSQL());
    }

    @Test
    void toSQL_withInvalidXAndValidY_shouldReturnNull() {
        FilterProblemFromXtoYFromDivZ filter = new FilterProblemFromXtoYFromDivZ();
        filter.setX("A1A");
        filter.setY("B");
        assertNull(filter.toSQL());
    }
}