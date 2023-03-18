/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author alen sejdini
 */
public class FileParserFactory {
    
    public static FileParser getInstance(String type){
        switch (type) {
            case "xml":
                return new XMLFileParser();
                
            case "edge":
                return new EdgeConvertFileParser();
        }
        return null;
    }
    
}
