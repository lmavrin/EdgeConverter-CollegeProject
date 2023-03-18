

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLFileParser extends FileParser {

    private String name;
    private NodeList parent;
    private String parentAttrCardinality;
    private String tablenameParent;
    private NodeList child;
    private String childAttrCardinality;
    private String tablenameChild;
    private String foreignKeyChild;
    private String foreignKeyAttrReferences;
    private Document doc;

    public XMLFileParser() {
        super();
    }

    @Override
    public boolean parseFile() throws IOException {
        //tables
        NodeList listOfTable = doc.getElementsByTagName("table");
        ArrayList<XMLTable> xmlTables = new ArrayList<>(); //list of xmlTable-s

        for (int i = 0; i < listOfTable.getLength(); i++) {
            Node nodeOfTable = listOfTable.item(i);

            //name, fields
            String nameOfTable = ((Element) nodeOfTable).getElementsByTagName("name").item(0).getTextContent().trim();

            NodeList listOfAllFieldNodes = ((Element) nodeOfTable).getElementsByTagName("field");
            ArrayList<XMLField> xmlFields = new ArrayList<>(); //list of field-s

            Table table = new Table(i + DELIM + nameOfTable);
            

            //field
            for (int j = 0; j < listOfAllFieldNodes.getLength(); j++) {

                Node nodeOfField = listOfAllFieldNodes.item(j);

                if (nodeOfField.getNodeType() == Node.ELEMENT_NODE) {

                    Element elementOfField = (Element) nodeOfField;
                    String fieldName = elementOfField.getTextContent().trim(); //  text context of fields

                    NamedNodeMap attributes = elementOfField.getAttributes();

                    String typeAttr = attributes.getNamedItem("type").getTextContent().trim(); //attribute

                    int sizeAttr = 0;
                    Node nodeSize = attributes.getNamedItem("size");
                    if (nodeSize != null) {
                        String sizeText = nodeSize.getTextContent().trim();
                        sizeAttr = Integer.parseInt(sizeText);
                    }

                    boolean fixedAttr = false;
                    Node nodeFixed = attributes.getNamedItem("fixed");
                    if (nodeFixed != null) {
                        String fixedText = nodeFixed.getTextContent().trim();
                        fixedAttr = Boolean.valueOf(fixedText); //attribute
                    }

                    boolean pkeyAttr = Boolean.valueOf(attributes.getNamedItem("pkey").getTextContent().trim()); //attribute
                    boolean nullAttr = Boolean.valueOf(attributes.getNamedItem("null").getTextContent().trim()); //attribute
                    boolean autoincrementAttr = Boolean.valueOf(attributes.getNamedItem("autoincrement").getTextContent().trim()); //attribute

                    EdgeField edgeField = new EdgeField(i, fieldName);
//                    edgeField.setTableID(i);

                    switch (typeAttr) {
                        case "string":
                            edgeField.setDataType(0);
                            edgeField.setVarcharValue(sizeAttr);
                            break;
                        case "boolean":
                        case "bool":
                            edgeField.setDataType(1);
                            break;
                        case "int":
                        case "integer":
                            edgeField.setDataType(2);
                            break;
                        case "double":
                            edgeField.setDataType(3);
                            break;
                    }
                    edgeField.setIsPrimaryKey(pkeyAttr);
                    edgeField.setDisallowNull(nullAttr);
                    
                    alFields.add(edgeField);
                    table.addNativeField(alFields.size() - 1);

                    xmlFields.add(new XMLField(fieldName, typeAttr, sizeAttr, fixedAttr, pkeyAttr, nullAttr, autoincrementAttr));
                }
            }
            alTables.add(table);
            table.makeArrays();
            

            //tables (table (name,field))
            xmlTables.add(new XMLTable(nameOfTable, xmlFields));
        }

        //relations
        NodeList listOfRelation = doc.getElementsByTagName("relation"); // <relationships>
        ArrayList<XMLRelation> xmlRelations = new ArrayList<>(); //list of relation-s

        for (int i = 0; i < listOfRelation.getLength(); i++) {
            Node nodeOfRelation = listOfRelation.item(i);

            //name, parent, child
            //name
            String nameOfRelation = ((Element) nodeOfRelation).getElementsByTagName("name").item(0).getTextContent().trim();

            //parent
            NodeList listOfAllParentNodes = ((Element) nodeOfRelation).getElementsByTagName("parent");
            String cardinalityAttrParent = listOfAllParentNodes.item(0).getAttributes().getNamedItem("cardinality").getTextContent(); // attribute
            Node nodeOfparent = listOfAllParentNodes.item(0);
            String attrTablenameParent = ((Element) nodeOfparent).getElementsByTagName("tablename").item(0).getTextContent().trim(); // tablename

            //child
            NodeList listOfAllChildNodes = ((Element) nodeOfRelation).getElementsByTagName("child");
            String cardinalityAttrChild = listOfAllChildNodes.item(0).getAttributes().getNamedItem("cardinality").getTextContent().trim(); // attribute
            Node nodeOfChild = listOfAllChildNodes.item(0);
            String attrTablenameChild = ((Element) nodeOfChild).getElementsByTagName("tablename").item(0).getTextContent().trim(); // tablename
            String attrForeignkeyChild = ((Element) nodeOfChild).getElementsByTagName("foreignkey").item(0).getTextContent().trim(); // tablename
            String referencesAttrForeignkey = ((Element) nodeOfChild).getElementsByTagName("foreignkey").item(0).getAttributes().getNamedItem("references").getTextContent().trim(); // attribute
            xmlRelations.add(new XMLRelation(nameOfRelation, listOfAllParentNodes, cardinalityAttrParent, attrTablenameParent, listOfAllChildNodes, cardinalityAttrChild, attrTablenameChild, attrForeignkeyChild, referencesAttrForeignkey));
            
            int parentTable = getTableIndexByName(attrTablenameParent);
            int childTable = getTableIndexByName(attrTablenameChild);
            
            alTables.get(parentTable).addRelatedTable(childTable);
            alTables.get(childTable).addRelatedTable(parentTable);
            
            var connector = new EdgeConnector(i, parentTable, childTable, cardinalityAttrParent, cardinalityAttrChild);
            alConnectors.add(connector);
            
//            for (int k = 0; k < alTables.size(); k++) {
//                if (alTables.get(k).getName().equalsIgnoreCase(attrTablenameParent)) {
//                    parentTable = k;
//                    
//                } else if (alTables.get(k).getName().equalsIgnoreCase(attrTablenameChild)) {
//                    childTable = k;
//                }
//            }
//            
//            for (int k = 0; k < alFields.size(); k++) {
//                if (alFields.get(k).getName().equalsIgnoreCase(attrForeignkeyChild)) {
//                    alTables.get(parentTable).setRelatedField(i, k);
//                    alTables.get(childTable).setRelatedField(i, k);
//                }
//            }
//            
//            alTables.get(parentTable).addRelatedTable(childTable);

        }
        this.makeArrays();
        var totalFields = alFields;
        return true;
    }
    
    private int getTableIndexByName(String name) {
        for (int i = 0; i < alTables.size(); i++) {
            if (alTables.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean openFile(File inputFile) throws IOException {
        parseFile = inputFile;
        boolean opened = false;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.doc = db.parse(parseFile);
            opened = true;
            parseFile();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return opened;
    }
    
  
}
