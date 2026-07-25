package org.nitor112221.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
