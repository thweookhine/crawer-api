package com.example.api_project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.api_project.model.Condition;
import com.example.api_project.model.Operator;
import com.example.api_project.model.SingleSearchRequest;
import com.example.api_project.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ConditionController.class)
@ExtendWith(MockitoExtension.class)
class ConditionControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	private SearchService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private SingleSearchRequest singleSearchRequest;
	
	List<Map<String, Object>> list;
	
	
	@BeforeEach
	public void init() {
		singleSearchRequest = new SingleSearchRequest();
		singleSearchRequest.setFrom("P00001");
		List<String> selects = List.of("D00001","D00002","D00003","D00004");
		singleSearchRequest.setSelect(selects);
		Condition condition = new Condition();
		condition.setColumn("D00003");
		condition.setKey(6.9);
		condition.setOperator(Operator.GT);
		singleSearchRequest.setWhere(condition);
		list = new ArrayList<>();
		
		Map<String, Object> map = new HashMap<>();
		map.put("quantity", 10);
		map.put("price", 6.99);
		map.put("name", "BlueBerry");
		map.put("id", 19);
		list.add(map);
	}
	
	

	@Test
	void test_success() throws Exception {
		
		when(service.singleSearch(singleSearchRequest)).thenReturn(list);
		 
		 RequestBuilder requestBuilder = MockMvcRequestBuilders
	                .post("http://localhost:8080/qs")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(singleSearchRequest));
		 
		 ResultActions result = mockMvc.perform(requestBuilder);
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
		 .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
		 ;
	}

}
