package com.example.api_project.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MultipleCondition {
    @NotNull(message = "Relation must not be null")
    @NotEmpty(message = "Relation must not be empty")
    private String relation;
    @Valid
    private List<Condition> conditions;
}
