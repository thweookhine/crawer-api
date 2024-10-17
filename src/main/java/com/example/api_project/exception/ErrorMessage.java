package com.example.api_project.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorMessage {
    private String message;
    
    public ErrorMessage(String message) {
    	this.message = message;
    }

}
