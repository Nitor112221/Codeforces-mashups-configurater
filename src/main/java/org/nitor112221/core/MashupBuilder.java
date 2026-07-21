package org.nitor112221.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.nitor112221.Database.Database;
import org.nitor112221.dto.Mashup;
import org.nitor112221.dto.Problem;
import org.nitor112221.filters.*;

import java.sql.SQLException;
import java.util.Arrays;

@Getter
@NoArgsConstructor
public class MashupBuilder {
    private int numProblem = 1;
    private FilterProblemContainsTags filterProblemContainsTags = null;
    private FilterProblemNotContainsTags filterProblemNotContainsTags = null;
    private FilterProblemWithRatingFromXToY filterProblemWithRatingFromXToY = null;
    private FilterProblemFromXtoYFromDivZ filterProblemFromXtoYFromDivZ = null;

    public MashupBuilder setNumProblem(int numProblem) {
        this.numProblem = numProblem;
        return this;
    }

    public MashupBuilder setFilter(FilterProblemContainsTags filter) {
        filterProblemContainsTags = filter;
        return this;
    }

    public MashupBuilder setFilter(FilterProblemNotContainsTags filter) {
        filterProblemNotContainsTags = filter;
        return this;
    }

    public MashupBuilder setFilter(FilterProblemWithRatingFromXToY filter) {
        filterProblemWithRatingFromXToY = filter;
        return this;
    }

    public MashupBuilder setFilter(FilterProblemFromXtoYFromDivZ filter) {
        filterProblemFromXtoYFromDivZ = filter;
        return this;
    }

    private String toSQL() {
        StringBuilder st = new StringBuilder();
        st.append("SELECT * FROM (SELECT DISTINCT p.*" +
                "   FROM problems as p" +
                "   LEFT JOIN contests as c ON p.contest_id = c.id" +
                "   LEFT JOIN problem_tags as pt ON p.contest_id = pt.contest_id AND p.problem_index = pt.problem_index"
        );
        boolean hasCondition = false;
        hasCondition = addCondition(filterProblemContainsTags, st, hasCondition);
        hasCondition = addCondition(filterProblemNotContainsTags, st, hasCondition);
        hasCondition = addCondition(filterProblemFromXtoYFromDivZ, st, hasCondition);
        hasCondition = addCondition(filterProblemWithRatingFromXToY, st, hasCondition);

        st.append(") sub ORDER BY RAND()").append(" LIMIT ").append(numProblem);
        return st.toString();
    }

    private boolean addCondition(FilterProblemInterface filter, StringBuilder st, boolean hasCondition) {
        if (filter == null) {
            return hasCondition;
        }

        String condition = filter.toSQL();
        if (condition != null) {
            if (!hasCondition) {
                st.append(" WHERE ");
            }
            else {
                st.append(" AND ");
            }
            st.append(condition);
            hasCondition = true;
        }
        return hasCondition;
    }

    public Mashup build() {
        Mashup mashup = new Mashup();
        try {
            for (Problem problem : Database.ExecuteSearch(toSQL())) {
                mashup.add(problem);
            }
        } catch (SQLException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return mashup;
    }
}
