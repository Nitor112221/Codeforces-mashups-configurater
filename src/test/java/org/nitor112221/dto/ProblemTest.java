package org.nitor112221.dto;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ProblemTest {

    @Test
    void constructor_shouldSetFields() {
        ArrayList<TagEnum> tags = new ArrayList<>();
        tags.add(TagEnum.DP);
        tags.add(TagEnum.STRINGS);

        Problem problem = new Problem(2244, "E", "Маша и гирлянда", tags);

        assertEquals(2244, problem.getContestId());
        assertEquals("E", problem.getIndex());
        assertEquals("Маша и гирлянда", problem.getName());
        assertEquals(tags, problem.getTags());
        assertNull(problem.getRating());
    }

    @Test
    void constructorWithRating_shouldSetRating() {
        Problem problem = new Problem(2244, "E", "Маша и гирлянда", 2000);

        assertEquals(2244, problem.getContestId());
        assertEquals("E", problem.getIndex());
        assertEquals("Маша и гирлянда", problem.getName());
        assertEquals(2000, problem.getRating());
        assertNull(problem.getTags());
    }
}