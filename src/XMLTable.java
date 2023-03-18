


/**
 *
 * @author Mariana
 * 
 
 */

import java.util.ArrayList;

public class XMLTable {
    String name;
    ArrayList<XMLField> fields;

    public XMLTable(String name, ArrayList<XMLField> fields) {
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public ArrayList<XMLField> getFields() {
        return fields;
    }
}
