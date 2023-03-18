

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

class EdgeMenuListener implements ActionListener {

    /**
     *
     */
    private final EdgeConvertGUI edgeConvertGUI;

    /**
     * @param edgeConvertGUI
     */
    EdgeMenuListener(EdgeConvertGUI edgeConvertGUI) {
        this.edgeConvertGUI = edgeConvertGUI;
    }

    public void actionPerformed(ActionEvent ae) {

        if (handleOpenEdgeEventAndShouldReturn(ae)) {
            return;
        }

        handleSaveEvent(ae);

        if (handleExitEventAndShouldReturn(ae)) {
            return;
        }

        handleOutputLocationEvent(ae);
        handleOptionShowProductsEvent(ae);
        handleHelpAboutEvent(ae);

    } // EdgeMenuListener.actionPerformed()

    private boolean isActionEventOpenEdge(ActionEvent ae) {
        return (ae.getSource() == edgeConvertGUI.jmiDTOpenEdge) || (ae.getSource() == edgeConvertGUI.jmiDROpenEdge);
    }

    private boolean isActionEventAnySave(ActionEvent ae) {
        return (ae.getSource() == edgeConvertGUI.jmiDTSaveAs) || (ae.getSource() == edgeConvertGUI.jmiDRSaveAs)
                || (ae.getSource() == edgeConvertGUI.jmiDTSave) || (ae.getSource() == edgeConvertGUI.jmiDRSave);
    }

    private boolean isActionEventSaveAs(ActionEvent ae) {
        return (ae.getSource() == edgeConvertGUI.jmiDTSaveAs) || (ae.getSource() == edgeConvertGUI.jmiDRSaveAs);
    }

    private boolean isActionEventAnyExit(ActionEvent ae) {
        return (ae.getSource() == edgeConvertGUI.jmiDTExit) || (ae.getSource() == edgeConvertGUI.jmiDRExit);
    }

    private boolean isActionEventOptionOutputLocation(ActionEvent ae) {
        return (ae.getSource() == edgeConvertGUI.jmiDTOptionsOutputLocation) || (ae.getSource() == edgeConvertGUI.jmiDROptionsOutputLocation);
    }

    private boolean isActionEventOptionShowProducts(ActionEvent ae) {
        return (ae.getSource() == edgeConvertGUI.jmiDTOptionsShowProducts) || (ae.getSource() == edgeConvertGUI.jmiDROptionsShowProducts);
    }

    private boolean isActionEventHelpAbout(ActionEvent ae) {
        return (ae.getSource() == edgeConvertGUI.jmiDTHelpAbout) || (ae.getSource() == edgeConvertGUI.jmiDRHelpAbout);
    }

    private boolean handleOpenEdgeEventAndShouldReturn(ActionEvent ae) {
        if (isActionEventOpenEdge(ae)) {
            if (!this.edgeConvertGUI.dataSaved) {
                int answer = JOptionPane.showConfirmDialog(null, "You currently have unsaved data. Continue?",
                        "Are you sure?", JOptionPane.YES_NO_OPTION);
                if (answer != JOptionPane.YES_OPTION) {
                    return true;
                }
            }
            edgeConvertGUI.jfcEdge.addChoosableFileFilter(edgeConvertGUI.effEdge);
            edgeConvertGUI.jfcEdge.addChoosableFileFilter(edgeConvertGUI.effSave);

            int returnVal = edgeConvertGUI.jfcEdge.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                handleFileParsing();
            } else {
                return true;
            }
            this.edgeConvertGUI.dataSaved = true;
        }
        return false;
    }

    private boolean handleExitEventAndShouldReturn(ActionEvent ae) {
        if (isActionEventAnyExit(ae)) {
            if (!this.edgeConvertGUI.dataSaved) {
                int answer = JOptionPane.showOptionDialog(null,
                        "You currently have unsaved data. Would you like to save?",
                        "Are you sure?",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, null, null);

                if (shouldSaveBasedOnAnswer(answer)) {
                    this.edgeConvertGUI.saveAs();
                }
                if (shouldReturnBasedOnAnswer(answer)) {
                    return true;
                }
            }
            System.exit(0); //No was selected
        }
        return false;
    }

    private void handleFileParsing() {
        this.edgeConvertGUI.parseFile = edgeConvertGUI.jfcEdge.getSelectedFile();

        var name = edgeConvertGUI.parseFile.getName();

        //this.edgeConvertGUI.ecfp = new EdgeConvertFileParser(this.edgeConvertGUI.parseFile);
        if (name.contains(".edg")) {
            this.edgeConvertGUI.ecfp = FileParserFactory.getInstance("edge");

        } else if (name.contains(".xml")) {
            this.edgeConvertGUI.ecfp = FileParserFactory.getInstance("xml");
        }

        try {
            this.edgeConvertGUI.ecfp.openFile(this.edgeConvertGUI.parseFile);
            this.edgeConvertGUI.tables = this.edgeConvertGUI.ecfp.getEdgeTables();
            for (int i = 0; i < this.edgeConvertGUI.tables.length; i++) {
                this.edgeConvertGUI.tables[i].makeArrays();
            }
        } catch (IOException e) {
            
        }

        

        this.edgeConvertGUI.fields = this.edgeConvertGUI.ecfp.getEdgeFields();
        this.edgeConvertGUI.ecfp = null;
        this.edgeConvertGUI.populateLists();
        this.edgeConvertGUI.saveFile = null;
        edgeConvertGUI.jmiDTSave.setEnabled(false);
        edgeConvertGUI.jmiDRSave.setEnabled(false);
        edgeConvertGUI.jmiDTSaveAs.setEnabled(true);
        edgeConvertGUI.jmiDRSaveAs.setEnabled(true);
        edgeConvertGUI.jbDTDefineRelations.setEnabled(true);

        edgeConvertGUI.jbDTCreateDDL.setEnabled(true);
        edgeConvertGUI.jbDRCreateDDL.setEnabled(true);

        this.edgeConvertGUI.truncatedFilename = this.edgeConvertGUI.parseFile.getName().substring(this.edgeConvertGUI.parseFile.getName().lastIndexOf(File.separator) + 1);
        edgeConvertGUI.jfDT.setTitle(EdgeConvertGUI.DEFINE_TABLES + " - " + this.edgeConvertGUI.truncatedFilename);
        edgeConvertGUI.jfDR.setTitle(EdgeConvertGUI.DEFINE_RELATIONS + " - " + this.edgeConvertGUI.truncatedFilename);
    }

    private void handleSaveEvent(ActionEvent ae) {
        if (isActionEventAnySave(ae)) {
            if (isActionEventSaveAs(ae)) {
                this.edgeConvertGUI.saveAs();
            } else {
                this.edgeConvertGUI.writeSave();
            }
        }
    }

    private void handleOptionShowProductsEvent(ActionEvent ae) {
        if (isActionEventOptionShowProducts(ae)) {
            JOptionPane.showMessageDialog(null, "The available products to create DDL statements are:\n" + this.edgeConvertGUI.displayProductNames());
        }
    }

    private void handleOutputLocationEvent(ActionEvent ae) {
        if (isActionEventOptionOutputLocation(ae)) {
            this.edgeConvertGUI.setOutputDir();
        }
    }

    private void handleHelpAboutEvent(ActionEvent ae) {
        if (isActionEventHelpAbout(ae)) {
            JOptionPane.showMessageDialog(null, "EdgeConvert ERD To DDL Conversion Tool\n"
                    + "by Stephen A. Capperell\n"
                    + "© 2007-2008");
        }
    }

    private boolean shouldSaveBasedOnAnswer(int answer) {
        return (answer == JOptionPane.YES_OPTION) && (this.edgeConvertGUI.saveFile == null);
    }

    private boolean shouldReturnBasedOnAnswer(int answer) {
        return (answer == JOptionPane.CANCEL_OPTION) || (answer == JOptionPane.CLOSED_OPTION);
    }

} // EdgeMenuListener
