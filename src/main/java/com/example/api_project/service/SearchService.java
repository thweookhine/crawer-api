package com.example.api_project.service;

import com.example.api_project.entity.SearchColumn;
import com.example.api_project.entity.SearchTable;
import com.example.api_project.exception.ResourceNotFoundException;
import com.example.api_project.model.Condition;
import com.example.api_project.model.MultipleSearchRequest;
import com.example.api_project.model.SingleSearchRequest;
import com.example.api_project.repository.SearchColumnRepository;
import com.example.api_project.repository.SearchTableRepo;
import com.example.api_project.repository.SearchDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {

    @Autowired
    private SearchTableRepo searchTableRepo;

    @Autowired
    private SearchColumnRepository searchColumnRepo;

    @Autowired
    private SearchDataDao searchDao;

    public List<Map<String,Object>> singleSearch(SingleSearchRequest req)throws ResourceNotFoundException {

        List<Map<String, Object>> result = new ArrayList<>();

        //get table name from searchtable
        String tableName = getTableName(req.getFrom());

        //get cols string
        String cols = getCols(req.getSelect(),req.getFrom());

        Condition condition = null;
        if(req.getWhere() != null){
            //search searchColumn with column id
            Optional<SearchColumn> toSearch = searchColumnRepo.findById(req.getWhere().getColumn());
            //check column exists or not
            if(toSearch.isEmpty()){
                throw new ResourceNotFoundException("Column-"+req.getWhere().getColumn()+" Not Found");
            }
            condition = req.getWhere();

            if(!toSearch.get().getTableId().equals(req.getFrom())){
                throw new ResourceNotFoundException("Column-"+condition.getColumn()+ " not found in Table-"+req.getFrom());
            }

            //add column name to condition object
            condition.setColumn(toSearch.get().getPhysicalName());
        }

        //search data with dynamic table
        result = searchDao.singleSearch(tableName, cols, condition);

        return result;
    }

    public List<Map<String,Object>> multiSearch(MultipleSearchRequest req) {

        //get table name from searchtable
        String tableName = getTableName(req.getFrom());

        //get cols string
        String cols = getCols(req.getSelect(),req.getFrom());

        List<Condition> conditions = req.getWhere().getConditions();

        for(Condition c : conditions){
            Optional<SearchColumn> conColumn = searchColumnRepo.findById(c.getColumn());
            if(conColumn.isEmpty()){
                throw new ResourceNotFoundException("Column-"+c.getColumn()+" Not Found");
            }

            //check condition's column exists in table
            if(!conColumn.get().getTableId().equals(req.getFrom())){
                throw new ResourceNotFoundException("Column-"+c.getColumn()+ " not found in Table-"+req.getFrom());
            }
            c.setColumn(conColumn.get().getPhysicalName());
        }

        List<Map<String, Object>> result =searchDao.multipleSearch(tableName,cols,conditions,req.getWhere().getRelation());

        return result;
    }

    private String getTableName(String code){
        Optional<SearchTable> searchTable = searchTableRepo.findById(code);

        //check searchTable exists or not
        if (searchTable.isEmpty()) {
            throw new ResourceNotFoundException("Table-"+code+ " Not Found");
        }

        //get table name from searchtable
        String tableName = searchTable.get().getPhysicalName();

        return tableName;
    }

    private String getCols(List<String> selectCols,String tableId){
        String cols = "";
        Optional<SearchColumn> searchColumn = null;
        for (String c : selectCols) {
            searchColumn = searchColumnRepo.findById(c);
            if (searchColumn.isEmpty()) {
                throw new ResourceNotFoundException("Column-"+c+ " not found");
            }
            //check colname exists in defined table
            if(!tableId.equals(searchColumn.get().getTableId())){
                throw new ResourceNotFoundException("Column-"+c+ " not found in Table-"+tableId);
            }

            if (cols.equals("")) {
                cols = searchColumn.get().getPhysicalName();
            } else {
                cols = cols + ", " + searchColumn.get().getPhysicalName();
            }
        }
        return cols;
    }
}
