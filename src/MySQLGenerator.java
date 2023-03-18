


import javax.swing.*;

public class MySQLGenerator extends DDLGenerator {

    protected String databaseName;
    //this array is for determining how MySQL refers to datatypes
    protected String[] strDataType = {"VARCHAR", "BOOL", "INT", "DOUBLE"};
    protected StringBuilder sb;
    protected static final String PRODUCT_NAME = "MySQL";

    public MySQLGenerator() {
        super();
        sb = new StringBuilder();
    } //CreateDDLMySQL(EdgeTable[], EdgeField[])
    
    public MySQLGenerator(Table[] inputTables, EdgeField[] inputFields) {
        super();
        sb = new StringBuilder();
    }
    
    public void createDDL() {
        EdgeConvertGUI.setReadSuccess(true);
        databaseName = generateDatabaseName();
        sb = new StringBuilder();
        sb.append("CREATE DATABASE " + databaseName + ";\r\n");
        sb.append("USE " + databaseName + ";\r\n");
        for (int boundCount = 0; boundCount <= maxBound; boundCount++) { //process tables in order from least dependent (least number of bound tables) to most dependent
            processTables(boundCount);
        }
    }

    public void processTables(int boundCount) {
        for (int tableCount = 0; tableCount < numBoundTables.length; tableCount++) { //step through list of tables
            if (numBoundTables[tableCount] == boundCount) { //
                var currentTable = tables[tableCount];
                printTable(currentTable);
            }
        }
    }

    private void printTable(Table currentTable) {
        sb.append("CREATE TABLE " + currentTable.getName() + " (\r\n");
        int[] nativeFields = currentTable.getNativeFieldsArray();
        int[] relatedFields = currentTable.getRelatedFieldsArray();
        boolean[] primaryKey = new boolean[nativeFields.length];
        int numPrimaryKey = 0;
        int numForeignKey = 0;
        for (int nativeFieldCount = 0; nativeFieldCount < nativeFields.length; nativeFieldCount++) { //print out the fields
            EdgeField currentField = getField(nativeFields[nativeFieldCount]);
            printDataType(currentField);
            printNullAllowed(currentField);
            printDefaultValue(currentField);

            boolean isPrimary = currentField.getIsPrimaryKey();
            primaryKey[nativeFieldCount] = isPrimary;
            numPrimaryKey = isPrimary ? numPrimaryKey + 1 : numPrimaryKey;

            if (currentField.getFieldBound() != 0) {
                numForeignKey++;
            }
            sb.append(",\r\n"); //end of field
        }
        if (numPrimaryKey > 0) { //table has primary key(s)
            printTablePrimaryKeys(currentTable, primaryKey, numPrimaryKey, numForeignKey, nativeFields);
        }
        if (numForeignKey > 0) { //table has foreign keys
            printTableForeignKeys(currentTable, numForeignKey, nativeFields, relatedFields);
        }
        sb.append(");\r\n\r\n"); //end of table
    }

    private void printTableForeignKeys(Table currentTable, int numForeignKey, int[] nativeFields, int[] relatedFields) {
        int currentFK = 1;
        for (int i = 0; i < relatedFields.length; i++) {
            if (relatedFields[i] != 0) {
                sb.append("CONSTRAINT " + currentTable.getName() + "_FK" + currentFK
                        + " FOREIGN KEY(" + getField(nativeFields[i]).getName() + ") REFERENCES "
                        + getTable(getField(nativeFields[i]).getTableBound()).getName() + "(" + getField(relatedFields[i]).getName() + ")");
                if (currentFK < numForeignKey) {
                    sb.append(",\r\n");
                }
                currentFK++;
            }
        }
        sb.append("\r\n");
    }

    private void printTablePrimaryKeys(Table currentTable, boolean[] primaryKey, int numPrimaryKey, int numForeignKey, int[] nativeFields) {
        sb.append("CONSTRAINT ")
                .append(currentTable.getName())
                .append("_PK PRIMARY KEY (");

        for (int i = 0; i < primaryKey.length; i++) {
            if (primaryKey[i]) {
                sb.append(getField(nativeFields[i]).getName());
                numPrimaryKey--;
                if (numPrimaryKey > 0) {
                    sb.append(", ");
                }
            }
        }
        sb.append(")");
        if (numForeignKey > 0) {
            sb.append(",");
        }
        sb.append("\r\n");
    }

    private void printDataType(EdgeField currentField) {
        sb.append("\t" + currentField.getName() + " " + strDataType[currentField.getDataType()]);
        if (currentField.getDataType() == 0) { //varchar
            sb.append("(" + currentField.getVarcharValue() + ")"); //append varchar length in () if data type is varchar
        }
    }

    private void printNullAllowed(EdgeField currentField) {
        if (currentField.getDisallowNull()) {
            sb.append(" NOT NULL");
        }
    }

    private void printDefaultValue(EdgeField currentField) {
        if (!currentField.getDefaultValue().equals("")) {
            if (currentField.getDataType() == 1) { //boolean data type
                sb.append(" DEFAULT " + convertStrBooleanToInt(currentField.getDefaultValue()));
            } else { //any other data type
                sb.append(" DEFAULT " + currentField.getDefaultValue());
            }
        }
    }

    protected int convertStrBooleanToInt(String input) { //MySQL uses '1' and '0' for boolean types
        if (input.equals("true")) {
            return 1;
        } else {
            return 0;
        }
    }

    public String generateDatabaseName() { //prompts user for database name
        String dbNameDefault = "MySQLDB";

        do {
            databaseName = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter the database name:",
                    "Database Name",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    dbNameDefault);
            if (databaseName == null) {
                EdgeConvertGUI.setReadSuccess(false);
                return "";
            }
            if (databaseName.equals("")) {
                JOptionPane.showMessageDialog(null, "You must select a name for your database.");
            }
        } while (databaseName.equals(""));
        return databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }
    
    public String getProductName() {
        return PRODUCT_NAME;
    }

    public String getSQLString() {
        createDDL();
        return sb.toString();
    }

}//EdgeConvertCreateDDL
