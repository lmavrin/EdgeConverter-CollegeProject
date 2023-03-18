

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class EdgeRadioButtonListener implements ActionListener {

    // log4j
    private static final Logger log = LogManager.getLogger(EdgeRadioButtonListener.class);
    /**
     *
     */
    private final EdgeConvertGUI edgeConvertGUI;

    /**
     * @param edgeConvertGUI
     */
    EdgeRadioButtonListener(EdgeConvertGUI edgeConvertGUI) {
        this.edgeConvertGUI = edgeConvertGUI;
    }

    public void actionPerformed(ActionEvent ae) {
        if (log.isDebugEnabled()) {
            log.info("Radio Button Action Performed - SOURCE: {}", ae.getSource());
        }
        for (int i = 0; i < edgeConvertGUI.jrbDataType.length; i++) {
            if (edgeConvertGUI.jrbDataType[i].isSelected()) {
                if (log.isDebugEnabled()) {
                    log.info("Setting Data Type: {}", i);
                }
                this.edgeConvertGUI.currentDTField.setDataType(i);
                break;
            }
        }
        if (edgeConvertGUI.jrbDataType[0].isSelected()) {
            log.info("Varchar Data Type Selected: Enabling...");
            edgeConvertGUI.jtfDTVarchar.setText(Integer.toString(EdgeField.VARCHAR_DEFAULT_LENGTH));
            edgeConvertGUI.jbDTVarchar.setEnabled(true);
        } else {
            log.info("Varchar Data Type Not Selected: Disabling...");
            edgeConvertGUI.jtfDTVarchar.setText("");
            edgeConvertGUI.jbDTVarchar.setEnabled(false);
        }
        edgeConvertGUI.jtfDTDefaultValue.setText("");
        this.edgeConvertGUI.currentDTField.setDefaultValue("");
        this.edgeConvertGUI.dataSaved = false;
    }
}