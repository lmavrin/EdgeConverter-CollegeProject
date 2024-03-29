

public class EdgeConnector {
   private int numConnector;
   private int endPoint1;
   private int endPoint2;
   private String endStyle1;
   private String endStyle2;
   private boolean isEP1Field;
   private boolean isEP2Field;
   private boolean isEP1Table;
   private boolean isEP2Table;

   public EdgeConnector(int numConnector, int endPoint1, int endPoint2, String endStyle1, String endStyle2) {
      this.numConnector = numConnector;
      this.endPoint1 = endPoint1;
      this.endPoint2 = endPoint2;
      this.endStyle1 = endStyle1;
      this.endStyle2 = endStyle2;
      this.isEP1Field = false;
      this.isEP2Field = false;
      this.isEP1Table = false;
      this.isEP2Table = false;
   }
   
   public int getNumConnector() {
      return numConnector;
   }
   
   public int getEndPoint1() {
      return endPoint1;
   }
   
   public int getEndPoint2() {
      return endPoint2;
   }
   
   public String getEndStyle1() {
      return endStyle1;
   }

   public String getEndStyle2() {
      return endStyle2;
   }

   public boolean getIsEP1Field() {
      return isEP1Field;
   }
   
   public boolean getIsEP2Field() {
      return isEP2Field;
   }

   public boolean getIsEP1Table() {
      return isEP1Table;
   }

   public boolean getIsEP2Table() {
      return isEP2Table;
   }

   public void setIsEP1Field(boolean value) {
      isEP1Field = value;
   }
   
   public void setIsEP2Field(boolean value) {
      isEP2Field = value;
   }

   public void setIsEP1Table(boolean value) {
      isEP1Table = value;
   }

   public void setIsEP2Table(boolean value) {
      isEP2Table = value;
   }
}
