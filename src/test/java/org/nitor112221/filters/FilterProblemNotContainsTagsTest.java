package org.nitor112221.filters;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nitor112221.Database.Database;
import org.nitor112221.dto.TagEnum;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class FilterProblemNotContainsTagsTest {
    private FilterProblemNotContainsTags filter;

    @BeforeAll
    static void setUpAll() throws SQLException, ClassNotFoundException {
        Database.Conn();
        Database.CloseDB();
    }

    @BeforeEach
    void setUp() {
        filter = new FilterProblemNotContainsTags();
    }

    @Test
    void toSQL_withSingleTag_shouldReturnNotEquals() {
        filter.add(TagEnum.DP);
        assertEquals("NOT (pt.tag_id = " + TagEnum.DP.getId() + ")", filter.toSQL());
    }

    @Test
    void toSQL_withMultipleTags_shouldReturnNotIn() {
        filter.add(TagEnum.DP);
        filter.add(TagEnum.STRINGS);
        String sql = filter.toSQL();
        assertTrue(sql.startsWith("NOT (pt.tag_id IN ("));
        assertTrue(sql.contains(TagEnum.DP.getId().toString()));
        assertTrue(sql.contains(TagEnum.STRINGS.getId().toString()));
        assertTrue(sql.endsWith("))"));
    }

    @Test
    void toSQL_withNoTags_shouldReturnNull() {
        assertNull(filter.toSQL());
    }
}