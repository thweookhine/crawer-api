package com.example.api_project.model;

public enum Operator {

    NULL("IS NULL"),N_NULL("IS NOT NULL"),EQ("="),NEQ("<>"),
    GT(">"),LT("<"),GTE(">="),LTE("<="),
    LIKE("LIKE"),ILIKE("ILIKE"),N_LIKE("NOT LIKE"),
    IN("IN"),BETWEEN("BETWEEN")
    ;

    private final String sqlCode;


    Operator(String sqlCode ) {
        this.sqlCode = sqlCode;
    }

    public String getCode(){
        return this.sqlCode;
    }
}
