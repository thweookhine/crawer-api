package com.example.api_project.repository;

import com.example.api_project.entity.SearchTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SearchTableRepo extends JpaRepository<SearchTable,String> {

}
