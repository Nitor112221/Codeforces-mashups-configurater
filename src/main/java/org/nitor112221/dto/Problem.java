package org.nitor112221.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Problem {
    private int contestId;
    private String index;
    private String name;
    private Integer rating;
    private ArrayList<TagEnum> tags;

    public Problem(int contestId, String index, String name, ArrayList<TagEnum> tags) {
        this.contestId = contestId;
        this.index = index;
        this.name = name;
        this.tags = tags;
    }
    public Problem(int contestId, String index, String name, int rating) {
        this.contestId = contestId;
        this.index = index;
        this.name = name;
        this.rating = rating;
    }
}
