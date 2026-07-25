package org.nitor112221.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contest {
    private int id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ContestTypeEnum type;

    @JsonCreator
    public Contest(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name) {
        this.id = id;
        this.type = ContestTypeEnum.fromContestName(name);
    }
}
