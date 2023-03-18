

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class DefineRelationsButtonListener implements ActionListener {

    private static final Logger logDefineRelations = LogManager.getLogger(DefineRelationsButtonListener.class);

    private final EdgeConvertGUI edgeConvertGUI;

    DefineRelationsButtonListener(EdgeConvertGUI edgeConvertGUI) {
        this.edgeConvertGUI = edgeConvertGUI;
    }

    public void actionPerformed(ActionEvent ae) {
        if (logDefineRelations.isDebugEnabled()) {
            logDefineRelations.info("Define Relations Action Preformed - SOURCE: {}", ae.getSource());
        }
        this.edgeConvertGUI.jfDT.setVisible(false);
        this.edgeConvertGUI.jfDR.setVisible(true); //show the Define Relations screen
        this.edgeConvertGUI.clearDTControls();
        this.edgeConvertGUI.dlmDTFieldsTablesAll.removeAllElements();
    }

}
