
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OracleGenerator extends DDLGenerator {

    protected String databaseName;
    protected String[] strDataType = {"VARCHAR", "BOOLEAN", "NUMBER", "BINARY_DOUBLE"};
    protected StringBuilder sb;

    private static final Logger log = LogManager.getLogger(OracleGenerator.class);

    public OracleGenerator() {
        super();
        sb = new StringBuilder();
    }

    public OracleGenerator(Table[] inputTables, EdgeField[] inputFields) {
        super();
        sb = new StringBuilder();

    }

    @Override
    public String getDatabaseName() {
        return "Oracle";
    }

    @Override
    public String getProductName() {
        return "Oracle";
    }

    @Override
    public String getSQLString() {
        return "Oracle SQL";
    }

    @Override
    public void createDDL() {
        log.info("Creating the dummy data");
    }
}
