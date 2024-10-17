package com.example.api_project.repository;

import com.example.api_project.model.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class SearchDataDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>> singleSearch(String tableName, String cols, Condition condition){

        List<Map<String,Object>> list = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder();
        List<Object> queryParam = new ArrayList<>();

        sqlBuilder.append("select ")
                .append(cols).append(" from ")
                .append(tableName);

        if (condition != null && !condition.getColumn().isEmpty()
        ){
            sqlBuilder.append(" where ").append(condition.getColumn()).append(" ").append(condition.getOperator().getCode());
            if(!condition.getKey().equals("")){
                addKeys(condition,sqlBuilder,queryParam);
            }
        }

        System.out.println(sqlBuilder.toString());

        list = jdbcTemplate.query(sqlBuilder.toString(),queryParam.toArray(),new DynamicColumnsMapper());

        return list;
    }

    public List<Map<String,Object>> multipleSearch(String tableName, String cols, List<Condition> conditions,String relation){
        StringBuilder sqlBuilder = new StringBuilder();
        List<Object> queryParam = new ArrayList<>();

        sqlBuilder.append("select ")
                .append(cols).append(" from ")
                .append(tableName);

        for(int i = 0;i< conditions.size();i++){
            Condition condition = conditions.get(i);
            if (condition != null && !condition.getColumn().isEmpty()
            ){
                    if(i == 0){
                        sqlBuilder.append(" where ");
                    }else{
                        sqlBuilder.append(" ")
                                .append(relation)
                                .append(" ");
                    }
                    sqlBuilder.append(condition.getColumn())
                            .append(" ")
                            .append(condition.getOperator().getCode());

                if(!condition.getKey().equals("")){
                    //add condition keys to sqlbuilder and queryparam
                    addKeys(condition,sqlBuilder,queryParam);
                }

            }
        }
        System.out.println("SQL => "+ sqlBuilder);

        List<Map<String,Object>> list = jdbcTemplate.query(sqlBuilder.toString(),queryParam.toArray(),new DynamicColumnsMapper());
        return list;
    }

    private void addKeys(Condition condition,StringBuilder sqlBuilder,List<Object> queryParam){
        //Loop condition keys and append to sqlBuilder
        if(condition.getKey() instanceof Collection<?>){
            List<Object> keys = (List<Object>) condition.getKey();
            for(int c =0 ;c<keys.size();c++){
                if(c == 0){
                    if(condition.getOperator().getCode().equals("IN")){
                        sqlBuilder.append(" (?");
                    }else{
                        sqlBuilder.append(" ?");
                    }
                    queryParam.add(keys.get(c));
                }  else{
                    if(condition.getOperator().getCode().equals("BETWEEN")){
                        sqlBuilder.append(" and ?");
                    }else{
                        sqlBuilder.append(" , ?");
                    }
                    queryParam.add(keys.get(c));
                }
                if(c == keys.size()-1 && condition.getOperator().getCode().equals("IN") ){
                    sqlBuilder.append(" ) ");
                }
            }
        } else{//if single value
            if(condition.getOperator().getCode().equals("IN")){
                sqlBuilder.append(" (?)");
            }else{
                sqlBuilder.append(" ?");
            }
            queryParam.add(condition.getKey());
        }
    }

   private static class DynamicColumnsMapper implements RowMapper<Map<String, Object>> {
       @Override
       public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
           int columnCount = rs.getMetaData().getColumnCount();
           Map<String,Object> map = new HashMap<>();
           for(int i= 1;i<= columnCount; i++){
               String columnName = rs.getMetaData().getColumnName(i);
               Object columnValue = rs.getObject(i);
               map.put(columnName,columnValue);
           }
           return map;
       }
   }
}
