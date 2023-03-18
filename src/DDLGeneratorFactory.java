/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author eli
 */
public class DDLGeneratorFactory {

    public static DDLGenerator getInstance(String type) {
        switch (type.toLowerCase()) {
            case "mysql":
                return new MySQLGenerator();
            case "oracle":
                return new OracleGenerator();
            default:
                return null;
        }
    }
}
