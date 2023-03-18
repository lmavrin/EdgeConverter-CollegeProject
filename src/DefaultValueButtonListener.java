


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultValueButtonListener implements ActionListener {

    private final EdgeConvertGUI edgeConvertGUI;
    private static final Logger logDefaultValue = LogManager.getLogger(DefaultValueButtonListener.class);

    DefaultValueButtonListener(EdgeConvertGUI edgeConvertGUI) {
        logDefaultValue.info("Starting the EdgeConverterGUI: Enabling it...");
        this.edgeConvertGUI = edgeConvertGUI;
    }

    public void actionPerformed(ActionEvent ae) {
        logDefaultValue.info("Action performed: Default Value pressed by user");
        String prev = edgeConvertGUI.jtfDTDefaultValue.getText();
        boolean goodData = false;
        int i = edgeConvertGUI.currentDTField.getDataType();
        do {
            String result = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter the default value:",
                    "Default Value",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    prev);

            if ((result == null)) {
                edgeConvertGUI.jtfDTDefaultValue.setText(prev);
                return;
            }
            goodData = parseDataType(i, result);

        } while (!goodData);
        int selIndex = edgeConvertGUI.jlDTFieldsTablesAll.getSelectedIndex();
        if (selIndex >= 0) {
            String selText = edgeConvertGUI.dlmDTFieldsTablesAll.getElementAt(selIndex).toString();
            edgeConvertGUI.setCurrentDTField(selText);
            edgeConvertGUI.currentDTField.setDefaultValue(edgeConvertGUI.jtfDTDefaultValue.getText());
        }
        edgeConvertGUI.dataSaved = false;
    }

    public boolean parseDataType(int dataType, String result) {
        switch (dataType) {
            case 0: //varchar
                if (result.length() <= Integer.parseInt(edgeConvertGUI.jtfDTVarchar.getText())) {
                    edgeConvertGUI.jtfDTDefaultValue.setText(result);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "The length of this value must be less than or equal to the Varchar length specified.");
                }
                break;
            case 1: //boolean
                String newResult = result.toLowerCase();
                if (newResult.equals("true") || newResult.equals("false")) {
                    edgeConvertGUI.jtfDTDefaultValue.setText(newResult);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "You must input a valid boolean value (\"true\" or \"false\").");
                }
                break;
            case 2: //Integer
                    try {
                Integer.parseInt(result);
                edgeConvertGUI.jtfDTDefaultValue.setText(result);
                return true;
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "\"" + result + "\" is not an integer or is outside the bounds of valid integer values.");
            }
            break;
            case 3: //Double
                    try {
                Double.parseDouble(result);
                edgeConvertGUI.jtfDTDefaultValue.setText(result);
                return true;
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "\"" + result + "\" is not a double or is outside the bounds of valid double values.");
            }
            break;
            case 4: //Timestamp
                    try {
                edgeConvertGUI.jtfDTDefaultValue.setText(result);
                return true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "\"" + result + "\" an error has occurred while attempting to set.");
            }
            break;
            default:
                return false;

        }
        return false;
    }
}
