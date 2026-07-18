package org.nitor112221.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Contest {
    private int id;
    private ContestTypeEnum type;
}
