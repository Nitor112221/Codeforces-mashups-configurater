package org.nitor112221.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Contest {
    public String id;
    public ContestTypeEnum type;
}
