    package org.nitor112221.filters;

    import lombok.Data;
    import org.nitor112221.dto.ContestTypeEnum;


    @Data
    public class FilterProblemFromXtoYFromDivZ implements FilterProblemInterface {
        private String X;
        private String Y;
        private ContestTypeEnum div;

        @Override
        public String toSQL() {
            if (div == null && X == null && Y == null) {
                return null;
            }
            if (X != null && !X.matches("^[A-Z][1-9]?$")) {
                return null;
            }
            if (Y != null && !Y.matches("^[A-Z][1-9]?$")) {
                return null;
            }
            StringBuilder res = new StringBuilder();
            boolean hasCondition = false;

            if (X != null || Y != null) {
                String indexCondition = buildIndexCondition();
                if (indexCondition != null && !indexCondition.isEmpty()) {
                    res.append(indexCondition);
                    hasCondition = true;
                }
            }

            if (div != null) {
                if (hasCondition) {
                    res.append(" && ");
                }
                res.append("contests.type = '").append(div).append("'");
                hasCondition = true;
            }

            return hasCondition ? res.toString() : null;
        }

        private String buildIndexCondition() {
            if (X == null && Y == null) {
                return null;
            }

            StringBuilder cond = new StringBuilder();

            if (X != null && Y != null) {
                cond.append("problem_index >= '").append(X).append("' && ");
                cond.append(buildUpperBoundCondition(Y));
            } else if (X != null) {
                cond.append("problem_index >= '").append(X).append("'");
            } else {
                cond.append(buildUpperBoundCondition(Y));
            }

            return cond.toString();
        }

        private String buildUpperBoundCondition(String y) {
            if (y == null) {
                throw new NullPointerException(); // такого не будет, но на всякий
            }

            if (y.matches("^[A-Z]$")) {
                char nextChar = (char) (y.charAt(0) + 1);
                return "problem_index < '" + nextChar + "'";
            } else {
                return "problem_index <= '" + y + "'";
            }
        }
    }