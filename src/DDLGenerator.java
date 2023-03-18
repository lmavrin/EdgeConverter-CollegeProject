

public abstract class DDLGenerator {
   static String[] products = {"MySQL", "Oracle"};
   protected Table[] tables; //master copy of EdgeTable objects
   protected EdgeField[] fields; //master copy of EdgeField objects
   protected int[] numBoundTables;
   protected int maxBound;
   protected StringBuilder sb;
   protected int selected;

   public DDLGenerator() {
   } //EdgeConvertCreateDDL(EdgeTable[], EdgeField[])

   public void initialize(Table[] inputTables, EdgeField[] inputFields) {
       this.tables = inputTables;
       this.fields = inputFields;
      numBoundTables = new int[tables.length];
      maxBound = 0;
      sb = new StringBuilder();

      for (int i = 0; i < tables.length; i++) { //step through list of tables
         int numBound = 0; //initialize counter for number of bound tables
         int[] relatedFields = tables[i].getRelatedFieldsArray();
         for (int j = 0; j < relatedFields.length; j++) { //step through related fields list
            if (relatedFields[j] != 0) {
               numBound++; //count the number of non-zero related fields
            }
         }
         numBoundTables[i] = numBound;
         if (numBound > maxBound) {
            maxBound = numBound;
         }
      }
   }

   protected EdgeField getField(int numFigure) {
      for (int fIndex = 0; fIndex < fields.length; fIndex++) {
         if (numFigure == fields[fIndex].getNumFigure()) {
            return fields[fIndex];
         }
      }
      return null;
   }

   protected Table getTable(int numFigure) {
      for (int tIndex = 0; tIndex < tables.length; tIndex++) {
         if (numFigure == tables[tIndex].getNumFigure()) {
            return tables[tIndex];
         }
      }
      return null;
   }

   public abstract String getDatabaseName();

   public abstract String getProductName();
   
   public abstract String getSQLString();
   
   public abstract void createDDL();
   
}//EdgeConvertCreateDDL
