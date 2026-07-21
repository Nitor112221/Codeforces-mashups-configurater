package org.nitor112221.filters;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FilterProblemWithRatingFromXToYTest {

    @Test
    void toSQL_withBothXAndY_shouldReturnRange() {
        FilterProblemWithRatingFromXToY filter = new FilterProblemWithRatingFromXToY();
        filter.setX(1500);
        filter.setY(2000);
        assertEquals("p.rating BETWEEN 1500 AND 2000", filter.toSQL());
    }

    @Test
    void toSQL_withOnlyX_shouldReturnGreaterOrEqual() {
        FilterProblemWithRatingFromXToY filter = new FilterProblemWithRatingFromXToY();
        filter.setX(1500);
        assertNull(filter.getY());
        assertEquals("p.rating >= 1500", filter.toSQL());
    }

    @Test
    void toSQL_withOnlyY_shouldReturnLessOrEqual() {
        FilterProblemWithRatingFromXToY filter = new FilterProblemWithRatingFromXToY();
        filter.setY(2000);
        assertNull(filter.getX());
        assertEquals("p.rating <= 2000", filter.toSQL());
    }

    @Test
    void toSQL_withNoParams_shouldReturnNull() {
        FilterProblemWithRatingFromXToY filter = new FilterProblemWithRatingFromXToY();
        assertNull(filter.getX());
        assertNull(filter.getY());
        assertNull(filter.toSQL());
    }
}