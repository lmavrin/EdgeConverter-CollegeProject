


/**
 *
 * @author Mariana
 */

import org.w3c.dom.NodeList;

public class XMLRelation {
    
    private String name;

    private NodeList parent;
    private String parentAttrCardinality;
    private String tablenameParent;

    private NodeList child;
    private String childAttrCardinality;
    private String tablenameChild;
    private String foreignKeyChild;
    private String foreignKeyAttrReferences;


    public XMLRelation(String name, NodeList parent, String parentAttrCardinality, String tablenameParent, NodeList child, String childAttrCardinality, String tablenameChild, String foreignKeyChild, String foreignKeyAttrReferences) {
        this.name = name;
        this.parent = parent;
        this.parentAttrCardinality = parentAttrCardinality;
        this.tablenameParent = tablenameParent;
        this.child = child;
        this.childAttrCardinality = childAttrCardinality;
        this.tablenameChild = tablenameChild;
        this.foreignKeyChild = foreignKeyChild;
        this.foreignKeyAttrReferences = foreignKeyAttrReferences;
    }

    public String getName() {
        return name;
    }

    public NodeList getParent() {
        return parent;
    }

    public String getParentAttrCardinality() {
        return parentAttrCardinality;
    }

    public String getTablenameParent() {
        return tablenameParent;
    }

    public NodeList getChild() {
        return child;
    }

    public String getChildAttrCardinality() {
        return childAttrCardinality;
    }

    public String getTablenameChild() {
        return tablenameChild;
    }

    public String getForeignKeyChild() {
        return foreignKeyChild;
    }

    public String getForeignKeyAttrReferences() {
        return foreignKeyAttrReferences;
    }

}
