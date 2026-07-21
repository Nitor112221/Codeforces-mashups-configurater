package org.nitor112221.filters;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nitor112221.Database.Database;
import org.nitor112221.dto.TagEnum;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class FilterProblemContainsTagsTest {
    private FilterProblemContainsTags filter;

    @BeforeAll
    static void setUpAll() throws SQLException, ClassNotFoundException {
        Database.Conn();
        Database.CloseDB();
    }

    @BeforeEach
    void setUp() {
        filter = new FilterProblemContainsTags();
    }

    @Test
    void toSQL_withSingleTag_shouldReturnEquals() {
        filter.add(TagEnum.DP);
        assertEquals("pt.tag_id = " + TagEnum.DP.getId(), filter.toSQL());
    }

    @Test
    void toSQL_withMultipleTags_shouldReturnInClause() {
        filter.add(TagEnum.DP);
        filter.add(TagEnum.STRINGS);
        String sql = filter.toSQL();
        assertTrue(sql.startsWith("pt.tag_id IN ("));
        assertTrue(sql.contains(TagEnum.DP.getId().toString()));
        assertTrue(sql.contains(TagEnum.STRINGS.getId().toString()));
        assertTrue(sql.endsWith(")"));
    }

    @Test
    void toSQL_withNoTags_shouldReturnNull() {
        assertNull(filter.toSQL());
    }

    @Test
    void removeTag_shouldRemoveFromFilter() {
        filter.add(TagEnum.DP);
        filter.add(TagEnum.STRINGS);
        filter.remove(TagEnum.DP);
        assertEquals("pt.tag_id = " + TagEnum.STRINGS.getId(), filter.toSQL());
    }

    @Test
    void removeNonExistentTag_shouldNotAffectFilter() {
        filter.add(TagEnum.DP);
        filter.remove(TagEnum.MATH);
        assertEquals("pt.tag_id = " + TagEnum.DP.getId(), filter.toSQL());
    }
}