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
            return "rating >= " + X + " AND rating <= " + Y;
        }
        if (X == null) {
            return "rating <= " + Y;
        }
        return "rating >= " + X;
    }
}
