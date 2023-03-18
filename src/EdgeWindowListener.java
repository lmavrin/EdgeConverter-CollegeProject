


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JOptionPane;

class EdgeWindowListener implements WindowListener {

    /**
     *
     */
    private final EdgeConvertGUI edgeConvertGUI;
    private static final String ERROR_MESSAGE = "Invalid operation for sorted list.";
    private static final Logger log = LogManager.getLogger();

    /**
     * @param edgeConvertGUI
     */
    EdgeWindowListener(EdgeConvertGUI edgeConvertGUI) {
        this.edgeConvertGUI = edgeConvertGUI;
    }

    public void windowActivated(WindowEvent we) {
        try {
            throw new UnsupportedOperationException(ERROR_MESSAGE);
        } catch (java.lang.UnsupportedOperationException e) {
            log.info(ERROR_MESSAGE);
        }

    }

    public void windowClosed(WindowEvent we) {
        try {
            throw new UnsupportedOperationException(ERROR_MESSAGE);
        } catch (java.lang.UnsupportedOperationException e) {
            log.info(ERROR_MESSAGE);
        }
    }

    public void windowDeactivated(WindowEvent we) {
        try {
            throw new UnsupportedOperationException(ERROR_MESSAGE);
        } catch (java.lang.UnsupportedOperationException e) {
            log.info(ERROR_MESSAGE);
        }

    }

    public void windowDeiconified(WindowEvent we) {
        try {
            throw new UnsupportedOperationException(ERROR_MESSAGE);
        } catch (java.lang.UnsupportedOperationException e) {
            log.info(ERROR_MESSAGE);
        }

    }

    public void windowIconified(WindowEvent we) {
        try {
            throw new UnsupportedOperationException(ERROR_MESSAGE);
        } catch (java.lang.UnsupportedOperationException e) {
            log.info(ERROR_MESSAGE);
        }

    }

    public void windowOpened(WindowEvent we) {
        try {
            throw new UnsupportedOperationException(ERROR_MESSAGE);
        } catch (java.lang.UnsupportedOperationException e) {
            log.info(ERROR_MESSAGE);
        }

    }

    public void windowClosing(WindowEvent we) {
        if (!this.edgeConvertGUI.dataSaved) {
            int answer = JOptionPane.showOptionDialog(null,
                    "You currently have unsaved data. Would you like to save?",
                    "Are you sure?",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, null, null);
            if (answer == JOptionPane.YES_OPTION) {
                if (this.edgeConvertGUI.saveFile == null) {
                    this.edgeConvertGUI.saveAs();
                }
                this.edgeConvertGUI.writeSave();
            }
            if ((answer == JOptionPane.CANCEL_OPTION) || (answer == JOptionPane.CLOSED_OPTION)) {
                if (we.getSource() == edgeConvertGUI.jfDT) {
                    edgeConvertGUI.jfDT.setVisible(true);
                }
                if (we.getSource() == edgeConvertGUI.jfDR) {
                    edgeConvertGUI.jfDR.setVisible(true);
                }
                return;
            }
        }
        System.exit(0);
    }
}
