package org.nitor112221.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class Problem {
    public int contestId;
    public String index;
    public String name;
    public Integer rating;
    public ArrayList<TagEnum> tags;

    Problem(int contestId, String index, String name, ArrayList<TagEnum> tags) {
        this.contestId = contestId;
        this.index = index;
        this.name = name;
        this.tags = tags;
    }
}
