

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MoveUpButtonListener implements ActionListener {

    private final EdgeConvertGUI edgeConvertGUI;

    MoveUpButtonListener(EdgeConvertGUI edgeConvertGUI) {
        this.edgeConvertGUI = edgeConvertGUI;
    }

    public void actionPerformed(ActionEvent ae) {
        int selection = this.edgeConvertGUI.jlDTFieldsTablesAll.getSelectedIndex();

        //repopulate Fields List
        int[] currentNativeFields = this.edgeConvertGUI.currentDTTable.getNativeFieldsArray();
        this.edgeConvertGUI.jlDTFieldsTablesAll.clearSelection();
        this.edgeConvertGUI.dlmDTFieldsTablesAll.removeAllElements();
        for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
            this.edgeConvertGUI.dlmDTFieldsTablesAll.addElement(this.edgeConvertGUI.getFieldName(currentNativeFields[fIndex]));
        }
        this.edgeConvertGUI.jlDTFieldsTablesAll.setSelectedIndex(selection - 1);
        this.edgeConvertGUI.dataSaved = false;
    }

}
