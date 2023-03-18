


/**
 *
 * @author Mariana
 */
public class XMLField {
    private String name;
    private String type;
    private int size;
    private boolean isFixed;
    private boolean disallowNull;
    private boolean isPrimaryKey;
    private boolean isAutoincrement;

    public XMLField(String name, String type, int size, boolean isFixed, boolean disallowNull, boolean isPrimaryKey, boolean isAutoincrement) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.isFixed = isFixed;
        this.disallowNull = disallowNull;
        this.isPrimaryKey = isPrimaryKey;
        this.isAutoincrement = isAutoincrement;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public boolean isDisallowNull() {
        return disallowNull;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public boolean isAutoincrement() {
        return isAutoincrement;
    }
}
