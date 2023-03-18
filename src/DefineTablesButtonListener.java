

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class DefineTablesButtonListener implements ActionListener {

    private final EdgeConvertGUI edgeConvertGUI;
    private static final Logger log = LogManager.getLogger(DefineTablesButtonListener.class);

    DefineTablesButtonListener(EdgeConvertGUI edgeConvertGUI) {
        log.info("Starting the EdgeConverterGUI: Enabling it...");
        this.edgeConvertGUI = edgeConvertGUI;
    }

    public void actionPerformed(ActionEvent ae) {
        log.info("Define Tables being loaded" );
        edgeConvertGUI.jfDT.setVisible(true); //show the Define Tables screen
        edgeConvertGUI.jfDR.setVisible(false);
        edgeConvertGUI.clearDRControls();
        edgeConvertGUI.depopulateLists();
        edgeConvertGUI.populateLists();
    }

}
