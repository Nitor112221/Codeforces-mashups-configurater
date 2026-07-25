package org.nitor112221.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class Response<T> {
    private String status;
    private String comment;
    private T result;
}
