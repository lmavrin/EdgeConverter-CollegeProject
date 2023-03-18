

import java.io.*;
import java.util.ArrayList;

public abstract class FileParser {

    protected File parseFile;
    protected FileReader fr;
    protected BufferedReader br;
    protected String currentLine;
    protected ArrayList<Table> alTables;
    protected ArrayList<EdgeField> alFields;
    protected ArrayList<EdgeConnector> alConnectors;
    protected Table[] tables;
    protected EdgeField[] fields;
    protected EdgeField tempField;
    protected EdgeConnector[] connectors;
    protected String style;
    protected String text;
    protected String tableName;
    protected String fieldName;
    protected boolean isEntity;
    protected boolean isAttribute;
    protected boolean isUnderlined = false;
    protected int numFigure;
    protected int numConnector;
    protected int numFields;
    protected int numTables;
    protected int numNativeRelatedFields;
    protected int endPoint1;
    protected int endPoint2;
    protected int numLine;
    protected String endStyle1;
    protected String endStyle2;
    public static final String EDGE_ID = "EDGE Diagram File"; //first line of .edg files should be this
    public static final String SAVE_ID = "EdgeConvert Save File"; //first line of save files should be this
    public static final String DELIM = "|";

    public FileParser() {
        numFigure = 0;
        numConnector = 0;
        alTables = new ArrayList<>();
        alFields = new ArrayList<>();
        alConnectors = new ArrayList<>();
        isEntity = false;
        isAttribute = false;
        numLine = 0;
    }

    public abstract boolean parseFile() throws IOException;

    public abstract boolean openFile(File inputFile) throws IOException;

    public Table[] getEdgeTables() {
        return tables;
    }

    public EdgeField[] getEdgeFields() {
        return fields;
    }

    protected void makeArrays() { //convert ArrayList objects into arrays of the appropriate Class type
        if (alTables != null) {
            tables = (Table[]) alTables.toArray(new Table[alTables.size()]);
        }
        if (alFields != null) {
            fields = (EdgeField[]) alFields.toArray(new EdgeField[alFields.size()]);
        }
        if (alConnectors != null) {
            connectors = (EdgeConnector[]) alConnectors.toArray(new EdgeConnector[alConnectors.size()]);
        }
    }

}
