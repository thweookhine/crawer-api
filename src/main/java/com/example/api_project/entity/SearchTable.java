package com.example.api_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "search_table" ,schema = "crawer")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchTable {

    @Id
    private String id;
    @Column(name = "logical_name")
    private String logicalName;
    @Column(name="physical_name")
    private String physicalName;

}
