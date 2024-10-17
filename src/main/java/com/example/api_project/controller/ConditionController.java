package com.example.api_project.controller;

import com.example.api_project.exception.ErrorMessage;
import com.example.api_project.model.MultipleSearchRequest;
import com.example.api_project.model.SingleSearchRequest;
import com.example.api_project.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Validated
public class ConditionController {

    @Autowired
    private SearchService searchService;

    @PostMapping(value = "/qs",consumes = "application/json", produces = "application/json")
    public ResponseEntity search(@Valid @RequestBody SingleSearchRequest request){

        //Search Data
        List<Map<String,Object>> list = searchService.singleSearch(request);
        
        System.out.println(list);

        //if data not found, return with 404 status code
        if(list.isEmpty() || list == null){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No Data Found"));
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/qm",consumes = "application/json", produces = "application/json")
    public ResponseEntity search(@Valid @RequestBody MultipleSearchRequest request){

        List<Map<String,Object>> list = searchService.multiSearch(request);

        //if data not found, return with 404 status code
        if(list == null || list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No Data Found"));
        }
        return ResponseEntity.ok(list);
    }

}
