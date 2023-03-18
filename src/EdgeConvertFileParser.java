


import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EdgeConvertFileParser extends FileParser {
    
    private int table1Index = 0;
    private int table2Index = 0;
    private boolean connectorError = false;

    public EdgeConvertFileParser() {
        super();
    }
    
    private boolean skipLine = false;
    private boolean endParsing = false;

    // log4j
    private static final Logger log = LogManager.getLogger(EdgeConvertFileParser.class);

    @Override
    public boolean parseFile() throws IOException {
 
        while ((currentLine = br.readLine()) != null && !endParsing) {
            currentLine = currentLine.trim();
            if (currentLine.startsWith("Figure ") && canParse()) { //this is the start of a Figure entry
                parseFigure();
            }
            if (currentLine.startsWith("Connector ") && canParse()) { //this is the start of a Connector entry
                parseConnector();
            }
            skipLine = false;
        }
        endParsing = false;
        skipLine = true;
        return true;
    }
    
    private boolean canParse() {
        return !skipLine && !endParsing;
    }

    private void parseFigure() throws IOException {
        parseFigureNumber();

        if (!currentLine.startsWith("Style")) { // this is to weed out other Figures, like Labels
            skipLine = true;
            return;
        }
        style = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); //get the Style parameter
        if (style.startsWith("Relation")) { //presence of Relations implies lack of normalization
            JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile + "\ncontains relations.  Please resolve them and try again.");
            EdgeConvertGUI.setReadSuccess(false);
            endParsing = true;
            return;
        }

        isEntity = style.startsWith("Entity");
        isAttribute = style.startsWith("Attribute");

        if (!(isEntity || isAttribute)) { //these are the only Figures we're interested in
            skipLine = true;
            return;
        }
        currentLine = br.readLine().trim(); //this should be Text
        text = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")).replaceAll(" ", ""); //get the Text parameter
        
        if (text.equals("")) {
            JOptionPane.showMessageDialog(null, "There are entities or attributes with blank names in this diagram.\nPlease provide names for them and try again.");
            EdgeConvertGUI.setReadSuccess(false);
            endParsing = true;
            return;
        }
        int escape = text.indexOf("\\");
        if (escape > 0) { //Edge denotes a line break as "\line", disregard anything after a backslash
            text = text.substring(0, escape);
        }

        do { //advance to end of record, look for whether the text is underlined
            currentLine = br.readLine().trim();
            if (currentLine.startsWith("TypeUnderl")) {
                isUnderlined = true;
            }
        } while (!currentLine.equals("}")); // this is the end of a Figure entry

        if (isEntity) { //create a new EdgeTable object and add it to the alTables ArrayList
            if (isTableDup(text)) {
                JOptionPane.showMessageDialog(null, "There are multiple tables called " + text + " in this diagram.\nPlease rename all but one of them and try again.");
                EdgeConvertGUI.setReadSuccess(false);
                endParsing = true;
                return;
            }
            alTables.add(new Table(numFigure + DELIM + text));
        }
        if (isAttribute) { //create a new EdgeField object and add it to the alFields ArrayList
            tempField = new EdgeField(numFigure, text);
            tempField.setIsPrimaryKey(isUnderlined);
            alFields.add(tempField);
        }
        //reset flags
        isEntity = false;
        isAttribute = false;
        isUnderlined = false;

    }
    
    protected void resolveConnectors() { //Identify nature of Connector endpoints
        for (int cIndex = 0; cIndex < connectors.length; cIndex++) {
            if (connectorError) {
                break;
            }
            resolveConnector(cIndex);
        }
    }
    
    private void resolveConnector(int cIndex) {
        int fieldIndex = 0;
        fieldIndex = setEndpoints(cIndex);

        for (int tIndex = 0; tIndex < tables.length; tIndex++) { //search tables array for endpoints
            if (endPoint1 == tables[tIndex].getNumFigure()) { //found endPoint1 in tables array
                connectors[cIndex].setIsEP1Table(true); //set appropriate flag
                table1Index = tIndex; //identify which element of the tables array that endPoint1 was found in
            }
            if (endPoint2 == tables[tIndex].getNumFigure()) { //found endPoint1 in tables array
                connectors[cIndex].setIsEP2Table(true); //set appropriate flag
                table2Index = tIndex; //identify which element of the tables array that endPoint2 was found in
            }
        }

        if (bothEndpointsAreFields(cIndex)) { //both endpoints are fields, implies lack of normalization
            JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile + "\ncontains composite attributes. Please resolve them and try again.");
            EdgeConvertGUI.setReadSuccess(false); //this tells GUI not to populate JList components
            connectorError = true;
            return; //stop processing list of Connectors
        }

        if (bothEndpointsAreTables(cIndex)) {
            if ((connectors[cIndex].getEndStyle1().indexOf("many") >= 0)
                    && (connectors[cIndex].getEndStyle2().indexOf("many") >= 0)) { //the connector represents a many-many relationship, implies lack of normalization
                JOptionPane.showMessageDialog(null, "There is a many-many relationship between tables\n\"" + tables[table1Index].getName() + "\" and \"" + tables[table2Index].getName() + "\"" + "\nPlease resolve this and try again.");
                EdgeConvertGUI.setReadSuccess(false); //this tells GUI not to populate JList components
                connectorError = true;
                return; //stop processing list of Connectors
            } else { //add Figure number to each table's list of related tables
                tables[table1Index].addRelatedTable(tables[table2Index].getNumFigure());
                tables[table2Index].addRelatedTable(tables[table1Index].getNumFigure());
                return; //next Connector
            }
        }

        if (fieldNotAssignedTable(fieldIndex)) { //field has not been assigned to a table yet
            assignTableToField(cIndex, table1Index, table2Index, fieldIndex);
        } else if (fieldIndex >= 0) { //field has already been assigned to a table
            JOptionPane.showMessageDialog(null, "The attribute " + fields[fieldIndex].getName() + " is connected to multiple tables.\nPlease resolve this and try again.");
            EdgeConvertGUI.setReadSuccess(false); //this tells GUI not to populate JList components
            connectorError = true;
            //stop processing list of Connectors
        }
    }
    private void assignTableToField(int cIndex, int table1Index, int table2Index, int fieldIndex) {
        if (connectors[cIndex].getIsEP1Table()) { //endpoint1 is the table
            tables[table1Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
            fields[fieldIndex].setTableID(tables[table1Index].getNumFigure()); //tell the field what table it belongs to
        } else { //endpoint2 is the table
            tables[table2Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
            fields[fieldIndex].setTableID(tables[table2Index].getNumFigure()); //tell the field what table it belongs to
        }
    }

    private boolean bothEndpointsAreFields(int index) {
        return connectors[index].getIsEP1Field() && connectors[index].getIsEP2Field();
    }

    private boolean bothEndpointsAreTables(int index) {
        return connectors[index].getIsEP1Table() && connectors[index].getIsEP2Table();
    }

    private boolean fieldNotAssignedTable(int fieldIndex) {
        return fieldIndex >= 0 && fields[fieldIndex].getTableID() == 0;
    }

    private int setEndpoints(int cIndex) {
        endPoint1 = connectors[cIndex].getEndPoint1();
        endPoint2 = connectors[cIndex].getEndPoint2();
        int fieldIndex = -1;
        for (int fIndex = 0; fIndex < fields.length; fIndex++) { //search fields array for endpoints
            if (endPoint1 == fields[fIndex].getNumFigure()) { //found endPoint1 in fields array
                connectors[cIndex].setIsEP1Field(true); //set appropriate flag
                fieldIndex = fIndex; //identify which element of the fields array that endPoint1 was found in
            }
            if (endPoint2 == fields[fIndex].getNumFigure()) { //found endPoint2 in fields array
                connectors[cIndex].setIsEP2Field(true); //set appropriate flag
                fieldIndex = fIndex; //identify which element of the fields array that endPoint2 was found in
            }
        }
        return fieldIndex;
    }
    
    protected boolean isTableDup(String testTableName) {
        for (int i = 0; i < alTables.size(); i++) {
            Table tempTable = (Table) alTables.get(i);
            if (tempTable.getName().equals(testTableName)) {
                return true;
            }
        }
        return false;
    }
    

    private void parseFigureNumber() throws IOException {
        numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Figure number
        currentLine = br.readLine().trim(); // this should be "{"
        currentLine = br.readLine().trim();
        currentLine = br.readLine().trim(); // this should be "SheetNumber 1"
    }

    private void parseConnector() throws IOException {
        numConnector = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Connector number
        currentLine = br.readLine().trim(); // this should be "{"
        currentLine = br.readLine().trim(); // not interested in SheetNumber 1
        currentLine = br.readLine().trim(); // not interested in Style
        currentLine = br.readLine().trim(); // Figure1
        endPoint1 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
        currentLine = br.readLine().trim(); // Figure2
        endPoint2 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
        currentLine = br.readLine().trim(); // not interested in EndPoint1
        currentLine = br.readLine().trim(); // not interested in EndPoint2
        currentLine = br.readLine().trim(); // not interested in SuppressEnd1
        currentLine = br.readLine().trim(); // not interested in SuppressEnd2
        currentLine = br.readLine().trim(); // End1
        endStyle1 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); //get the End1 parameter
        currentLine = br.readLine().trim(); // End2
        endStyle2 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); //get the End2 parameter

        do { //advance to end of record
            currentLine = br.readLine().trim();
        } while (!currentLine.equals("}")); // this is the end of a Connector entry

        alConnectors.add(new EdgeConnector(numConnector, endPoint1, endPoint2, endStyle1, endStyle2));

    }

    public void parseSaveFile() throws IOException { //this method is fucked
        StringTokenizer stTables;
        StringTokenizer stNatFields;
        StringTokenizer stRelFields;
        StringTokenizer stField;
        Table tempTable;
        EdgeField tempField;
        currentLine = br.readLine();
        currentLine = br.readLine(); //this should be "Table: "
        while (currentLine.startsWith("Table: ")) {
            numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Table number
            currentLine = br.readLine(); //this should be "{"
            currentLine = br.readLine(); //this should be "TableName"
            tableName = currentLine.substring(currentLine.indexOf(" ") + 1);
            tempTable = new Table(numFigure + DELIM + tableName);

            currentLine = br.readLine(); //this should be the NativeFields list
            stNatFields = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
            numFields = stNatFields.countTokens();
            for (int i = 0; i < numFields; i++) {
                tempTable.addNativeField(Integer.parseInt(stNatFields.nextToken()));
            }

            currentLine = br.readLine(); //this should be the RelatedTables list
            stTables = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
            numTables = stTables.countTokens();
            for (int i = 0; i < numTables; i++) {
                tempTable.addRelatedTable(Integer.parseInt(stTables.nextToken()));
            }
            tempTable.makeArrays();

            currentLine = br.readLine(); //this should be the RelatedFields list
            stRelFields = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
            numFields = stRelFields.countTokens();

            for (int i = 0; i < numFields; i++) {
                tempTable.setRelatedField(i, Integer.parseInt(stRelFields.nextToken()));
            }

            alTables.add(tempTable);
            currentLine = br.readLine(); //this should be "}"
            currentLine = br.readLine(); //this should be "\n"
            currentLine = br.readLine(); //this should be either the next "Table: ", #Fields#
        }
        while ((currentLine = br.readLine()) != null) {
            stField = new StringTokenizer(currentLine, DELIM);
            numFigure = Integer.parseInt(stField.nextToken());
            fieldName = stField.nextToken();
            tempField = new EdgeField(numFigure, fieldName);
            tempField.setTableID(Integer.parseInt(stField.nextToken()));
            tempField.setTableBound(Integer.parseInt(stField.nextToken()));
            tempField.setFieldBound(Integer.parseInt(stField.nextToken()));
            tempField.setDataType(Integer.parseInt(stField.nextToken()));
            tempField.setVarcharValue(Integer.parseInt(stField.nextToken()));
            tempField.setIsPrimaryKey(Boolean.parseBoolean(stField.nextToken()));
            tempField.setDisallowNull(Boolean.parseBoolean(stField.nextToken()));
            if (stField.hasMoreTokens()) { //Default Value may not be defined
                tempField.setDefaultValue(stField.nextToken());
            }
            alFields.add(tempField);
        }
    }

    @Override
    public boolean openFile(File inputFile) {
        parseFile = inputFile;
        boolean fileOpened = false;
        try {
            fr = new FileReader(parseFile);
            br = new BufferedReader(fr);
            //test for what kind of file we have
            currentLine = br.readLine().trim();
            numLine++;
            if (currentLine.startsWith(EDGE_ID)) { //the file chosen is an Edge Diagrammer file
                parseFile();
                br.close();
                this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
                this.resolveConnectors(); //Identify nature of Connector endpoints
                fileOpened = true;
            } else {
                if (currentLine.startsWith(SAVE_ID)) { //the file chosen is a Save file created by this application
                    this.parseSaveFile(); //parse the file
                    br.close();
                    this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
                    fileOpened= true;
                } else { //the file chosen is something else
                    JOptionPane.showMessageDialog(null, "Unrecognized file format");
                }
            }
            
        } // try
        catch (FileNotFoundException fnfe) {
            log.error(MessageFormat.format("Cannot find {0} ", parseFile.getName()));
            System.exit(0);
        } // catch FileNotFoundException
        catch (IOException ioe) {
            log.error(MessageFormat.format("IOException: {0}", ioe));
            System.exit(0);
        } // catch IOException
        return fileOpened;
    }
} // EdgeConvertFileHandler
