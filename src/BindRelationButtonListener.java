

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BindRelationButtonListener implements ActionListener {

    private final EdgeConvertGUI edgeConvertGUI;

    BindRelationButtonListener(EdgeConvertGUI edgeConvertGUI) {
        this.edgeConvertGUI = edgeConvertGUI;
    }

    public void actionPerformed(ActionEvent ae) {
        int nativeIndex = edgeConvertGUI.jlDRFieldsTablesRelations.getSelectedIndex();
        int relatedField = edgeConvertGUI.currentDRField2.getNumFigure();

        var currentDRField1 = edgeConvertGUI.getCurrentDRField1();
        var currentDRField2 = edgeConvertGUI.getCurrentDRField2();
        var currentDRTable1 = edgeConvertGUI.getCurrentDRTable1();
        var currentDRTable2 = edgeConvertGUI.getCurrentDRTable2();

        if (currentDRField1.getFieldBound() == relatedField) { //the selected fields are already bound to each other
            int answer = JOptionPane.showConfirmDialog(null, "Do you wish to unbind the relation on field "
                    + currentDRField1.getName() + "?",
                    "Are you sure?", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                currentDRTable1.setRelatedField(nativeIndex, 0); //clear the related field
                currentDRField1.setTableBound(0); //clear the bound table
                currentDRField1.setFieldBound(0); //clear the bound field
                edgeConvertGUI.jlDRFieldsTablesRelatedTo.clearSelection(); //clear the listbox selection
            }
            return;
        }
        if (currentDRField1.getFieldBound() != 0) { //field is already bound to a different field
            int answer = JOptionPane.showConfirmDialog(null, "There is already a relation defined on field "
                    + currentDRField1.getName() + ", do you wish to overwrite it?",
                    "Are you sure?", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.NO_OPTION || answer == JOptionPane.CLOSED_OPTION) {
                edgeConvertGUI.jlDRTablesRelatedTo.setSelectedValue(edgeConvertGUI.getTableName(currentDRField1.getTableBound()), true); //revert selections to saved settings
                edgeConvertGUI.jlDRFieldsTablesRelatedTo.setSelectedValue(edgeConvertGUI.getFieldName(currentDRField1.getFieldBound()), true); //revert selections to saved settings
                return;
            }
        }
        if (currentDRField1.getDataType() != currentDRField2.getDataType()) {
            JOptionPane.showMessageDialog(null, "The datatypes of " + currentDRTable1.getName() + "."
                    + currentDRField1.getName() + " and " + currentDRTable2.getName()
                    + "." + currentDRField2.getName() + " do not match.  Unable to bind this relation.");
            return;
        }
        if ((currentDRField1.getDataType() == 0) && (currentDRField2.getDataType() == 0) && notEqualVarchar(currentDRField1, currentDRField2)) {
            JOptionPane.showMessageDialog(null, "The varchar lengths of " + currentDRTable1.getName() + "."
                    + currentDRField1.getName() + " and " + currentDRTable2.getName()
                    + "." + currentDRField2.getName() + " do not match.  Unable to bind this relation.");
            return;

        }
        currentDRTable1.setRelatedField(nativeIndex, relatedField);
        currentDRField1.setTableBound(currentDRTable2.getNumFigure());
        currentDRField1.setFieldBound(currentDRField2.getNumFigure());
        JOptionPane.showMessageDialog(null, "Table " + currentDRTable1.getName() + ": native field "
                + currentDRField1.getName() + " bound to table " + currentDRTable2.getName()
                + " on field " + currentDRField2.getName());
        edgeConvertGUI.dataSaved = false;
    }

    private boolean notEqualVarchar(EdgeField f1, EdgeField f2) {
        return f1.getVarcharValue() != f2.getVarcharValue();
    }
}
