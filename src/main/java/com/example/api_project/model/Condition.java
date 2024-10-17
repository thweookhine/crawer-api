package com.example.api_project.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class Condition {

    @NotNull(message = "column must not be null")
    private String column;
    @NotNull(message = "operator must not be null")
    private Operator operator;
    @NotNull(message = "key must not be null")
    private Object key;
}
