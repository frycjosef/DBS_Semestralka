package database;

public class Column {

    private String name;
    private String datatype;

    /**
     * Column constructor.
     *
     * @param name Name od column.
     * @param datatype Datatype of column.
     */
    public Column(String name, String datatype) {
        this.name = name;
        this.datatype = datatype;
    }

    /**
     * Gets column name.
     *
     * @return name Name of column.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets column datatype.
     *
     * @return datatype Datatype of column.
     */
    public String getDatatype() {
        return datatype;
    }
}
