

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetVarcharLengthButtonListener implements ActionListener {

    private final EdgeConvertGUI edgeConvertGUI;

    SetVarcharLengthButtonListener(EdgeConvertGUI edgeConvertGUI) {
        this.edgeConvertGUI = edgeConvertGUI;
    }

    public void actionPerformed(ActionEvent ae) {
        String prev = edgeConvertGUI.jtfDTVarchar.getText();
        String result = (String) JOptionPane.showInputDialog(
                null,
                "Enter the varchar length:",
                "Varchar Length",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                prev);
        if ((result == null)) {
            edgeConvertGUI.jtfDTVarchar.setText(prev);
            return;
        }
        edgeConvertGUI.jlDTFieldsTablesAll.getSelectedIndex();
        int varchar;
        try {
            if (result.length() > 5) {
                JOptionPane.showMessageDialog(null, "Varchar length must be greater than 0 and less than or equal to 65535.");
                edgeConvertGUI.jtfDTVarchar.setText(Integer.toString(EdgeField.VARCHAR_DEFAULT_LENGTH));
                return;
            }
            varchar = Integer.parseInt(result);
            if (varchar > 0 && varchar <= 65535) { // max length of varchar is 255 before v5.0.3
                edgeConvertGUI.jtfDTVarchar.setText(Integer.toString(varchar));
                edgeConvertGUI.currentDTField.setVarcharValue(varchar);
            } else {
                JOptionPane.showMessageDialog(null, "Varchar length must be greater than 0 and less than or equal to 65535.");
                edgeConvertGUI.jtfDTVarchar.setText(Integer.toString(EdgeField.VARCHAR_DEFAULT_LENGTH));
                return;
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "\"" + result + "\" is not a number");
            edgeConvertGUI.jtfDTVarchar.setText(Integer.toString(EdgeField.VARCHAR_DEFAULT_LENGTH));
            return;
        }
        edgeConvertGUI.dataSaved = false;
    }

}
