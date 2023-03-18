

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MoveDownButtonListener implements ActionListener {

    private final EdgeConvertGUI edgeConvertGUI;

    MoveDownButtonListener(EdgeConvertGUI edgeConvertGUI) {
        this.edgeConvertGUI = edgeConvertGUI;
    }

    public void actionPerformed(ActionEvent ae) {
        int selection = edgeConvertGUI.jlDTFieldsTablesAll.getSelectedIndex(); //the original selected index
        edgeConvertGUI.currentDTTable.moveFieldDown(selection);
        //repopulate Fields List
        int[] currentNativeFields = edgeConvertGUI.currentDTTable.getNativeFieldsArray();
        edgeConvertGUI.jlDTFieldsTablesAll.clearSelection();
        edgeConvertGUI.dlmDTFieldsTablesAll.removeAllElements();
        for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
            edgeConvertGUI.dlmDTFieldsTablesAll.addElement(edgeConvertGUI.getFieldName(currentNativeFields[fIndex]));
        }
        edgeConvertGUI.jlDTFieldsTablesAll.setSelectedIndex(selection + 1);
        edgeConvertGUI.dataSaved = false;
    }

}