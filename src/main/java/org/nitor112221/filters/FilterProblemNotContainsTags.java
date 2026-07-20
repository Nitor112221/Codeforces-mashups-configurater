package org.nitor112221.filters;

public class FilterProblemNotContainsTags extends FilterProblemContainsTags implements FilterProblemInterface{
    @Override
    public String toSQL() {
        String res = super.toSQL();
        if (res == null) return null;
        return "NOT (" + res + ")";
    }
}
