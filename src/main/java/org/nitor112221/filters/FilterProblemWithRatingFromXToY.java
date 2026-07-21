package org.nitor112221.filters;

import lombok.Data;

@Data
public class FilterProblemWithRatingFromXToY implements FilterProblemInterface{
    private Integer X;
    private Integer Y;

    @Override
    public String toSQL() {
        if (X == null && Y == null) {
            return null;
        }
        if (X != null && Y != null) {
            return "p.rating BETWEEN " + X + " AND " + Y;
        }
        if (X == null) {
            return "p.rating <= " + Y;
        }
        return "p.rating >= " + X;
    }
}
