package database;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String name;
    private List<String>columns;

    public Table(String name){
        this.name=name;
        this.columns=new ArrayList<>();
    }

    public List<String> getColumns() {
        return columns;
    }

    public void addColumn(String name){
        columns.add(name);
    }


    public String getName(){
        return name;
    }

}
