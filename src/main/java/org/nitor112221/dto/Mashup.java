package org.nitor112221.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class Mashup {
    private final ArrayList<Problem> problems = new ArrayList<>();

    public void add(Problem problem) {
        problems.add(problem);
    }
}
