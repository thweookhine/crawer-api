package com.example.api_project.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MultipleSearchRequest {

    private List<String> select;
    @Valid
    @NotEmpty(message = "Table name must be not empty")
    @NotNull(message = "Table name must be not null")
    private String from;
    @Valid
    private MultipleCondition where;
}
