package org.nitor112221.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Contest {
    public int id;
    public ContestTypeEnum type;
}
