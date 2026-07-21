package org.nitor112221.filters;

import org.nitor112221.dto.TagEnum;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FilterProblemContainsTags implements FilterProblemInterface{
    private final Set<TagEnum> tags = new HashSet<>();

    public void add(TagEnum tag) {
        tags.add(tag);
    }

    public void remove(TagEnum tag) {
        tags.remove(tag);
    }

    @Override
    public String toSQL() {
        if (tags.isEmpty()) {
            return null;
        }
        if (tags.size() == 1) {
            return "pt.tag_id = " + tags.iterator().next().getId();
        }

        StringBuilder res = new StringBuilder();
        res.append("pt.tag_id IN (");
        Iterator<TagEnum> iter = tags.iterator();
        while (iter.hasNext()) {
            res.append(iter.next().getId());
            if (iter.hasNext()) res.append(", ");
        }
        res.append(")");
        return res.toString();
    }
}
