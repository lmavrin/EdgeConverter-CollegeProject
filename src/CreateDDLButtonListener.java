

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

class CreateDDLButtonListener implements ActionListener {

    private static final Logger log = LogManager.getLogger(CreateDDLButtonListener.class);


    /**
     *
     */
    private final EdgeConvertGUI edgeConvertGUI;

    /**
     * @param edgeConvertGUI
     */
    CreateDDLButtonListener(EdgeConvertGUI edgeConvertGUI) {
        this.edgeConvertGUI = edgeConvertGUI;
    }

    public void actionPerformed(ActionEvent ae) {
        if (log.isDebugEnabled()) {
            var source = ae.getSource();
            log.info("Create DDL Button Action Performed - SOURCE: {}", source);
        }

        while (this.edgeConvertGUI.outputDir == null) {
            JOptionPane.showMessageDialog(null, "You have not selected a path that contains valid output definition files yet.\nPlease select a path now.");
            if (this.edgeConvertGUI.setOutputDir() > 0) break;
        }

        this.edgeConvertGUI.getOutputClasses(); //in case outputDir was set before a file was loaded and EdgeTable/EdgeField objects created
        this.edgeConvertGUI.sqlString = this.edgeConvertGUI.getSQLStatements();


        if (this.edgeConvertGUI.sqlString.equals(edgeConvertGUI.CANCELLED)) {
            return;
        }


        this.edgeConvertGUI.writeSQL(this.edgeConvertGUI.sqlString);
    }
}