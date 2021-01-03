package database;

public class Column {

    private String name;
    private String datatype;

    public Column(String name, String datatype) {
        this.name = name;
        this.datatype = datatype;
    }

    public String getName() {
        return name;
    }

    public String getDatatype() {
        return datatype;
    }
}
