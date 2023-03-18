

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class EdgeConvertGUI {

    private static final Logger logger = LogManager.getLogger(EdgeConvertGUI.class);

    public static final int HORIZ_SIZE = 635;
    public static final int VERT_SIZE = 400;
    public static final int HORIZ_LOC = 100;
    public static final int VERT_LOC = 100;
    public static final String DEFINE_TABLES = "Define Tables";
    public static final String DEFINE_RELATIONS = "Define Relations";
    public static final String CREATE_DDL = "Create DDL";
    protected static final String CANCELLED = "CANCELLED";
    protected JMenuItem jmiDTOpenEdge;
    protected JFileChooser jfcEdge;
    protected JFileChooser jfcOutputDir;
    protected ExampleFileFilter effEdge;
    protected ExampleFileFilter effSave;
    protected File parseFile;
    protected File saveFile;
    protected File outputDir;
    protected String truncatedFilename;
    protected String sqlString;
    protected String databaseName;

    protected EdgeMenuListener menuListener;
    protected EdgeRadioButtonListener radioListener;
    protected EdgeWindowListener edgeWindowListener;
    protected CreateDDLButtonListener createDDLListener;
    protected DefineRelationsButtonListener defineRelationsButtonListener;
    protected MoveUpButtonListener moveUpButtonListener;
    protected MoveDownButtonListener moveDownButtonListener;
    protected DefaultValueButtonListener defaultValueButtonListener;
    protected SetVarcharLengthButtonListener setVarcharLengthButtonListener;
    protected DefineTablesButtonListener defineTablesButtonListener;
    protected BindRelationButtonListener bindRelationButtonListener;
    protected JButton jbDRDefineTables;
    protected JPanel jpDRCenter;
    protected JPanel jpDRBottom;
    protected JPanel jpDRCenter1;
    protected JPanel jpDRCenter2;
    protected JPanel jpDRCenter3;
    protected JPanel jpDRCenter4;
    protected JLabel jlabDRTablesRelations;
    protected JLabel jlabDRTablesRelatedTo;
    protected JLabel jlabDRFieldsTablesRelations;
    protected JLabel jlabDRFieldsTablesRelatedTo;
    protected JScrollPane jspDRTablesRelations;
    protected JScrollPane jspDRTablesRelatedTo;
    protected JScrollPane jspDRFieldsTablesRelations;
    protected JScrollPane jspDRFieldsTablesRelatedTo;
    protected JMenu jmDRFile;
    protected JMenu jmDROptions;
    protected JMenu jmDRHelp;

    protected JMenuItem jmiDROpenSave;
    protected JMenuBar jmbDRMenuBar;
    protected JMenuItem jmiDRSave;
    protected JMenuItem jmiDRSaveAs;
    protected JMenuItem jmiDRExit;
    protected JMenuItem jmiDROptionsOutputLocation;
    protected JMenuItem jmiDRHelpAbout;

    protected FileParser ecfp;
    protected PrintWriter pw;
    protected Table[] tables; //master copy of EdgeTable objects

    protected EdgeField[] fields; //master copy of EdgeField objects

    protected Table currentDTTable; //pointers to currently selected table(s) on Define Tables (DT) and Define Relations (DR) screens
    protected Table currentDRTable1; //pointers to currently selected table(s) on Define Tables (DT) and Define Relations (DR) screens
    protected Table currentDRTable2; //pointers to currently selected table(s) on Define Tables (DT) and Define Relations (DR) screens

    protected EdgeField currentDTField; //pointers to currently selected field(s) on Define Tables (DT) and Define Relations (DR) screens

    EdgeField currentDRField1;
    protected EdgeField currentDRField2;

    protected static boolean readSuccess = true; //this tells GUI whether to populate JList components or not
    protected boolean dataSaved = true;
    protected ArrayList<Object> alSubclasses;
    protected ArrayList<Object> alProductNames;
    protected String[] productNames;
    protected Object[] objSubclasses;

    protected JPanel jpDTBottom;
    protected JPanel jpDTCenter;
    protected JPanel jpDTCenter1;
    protected JPanel jpDTCenter2;
    protected JPanel jpDTCenterRight;
    protected JPanel jpDTCenterRight1;
    protected JPanel jpDTCenterRight2;
    protected JPanel jpDTMove;
    protected ButtonGroup bgDTDataType;
    protected JLabel jlabDTFields;
    protected JLabel jlabDTTables;
    protected JScrollPane jspDTFieldsTablesAll;
    protected JScrollPane jspDTTablesAll;
    protected JMenu jmDTHelp;
    protected JMenu jmDTOptions;
    protected JMenu jmDTFile;
    protected JMenuItem jmiDTSaveAs;
    protected JMenuItem jmiDTHelpAbout;
    protected JMenuItem jmiDTExit;
    protected JMenuItem jmiDTOptionsOutputLocation;
    protected JButton jbDTDefineRelations;
    protected JMenuBar jmbDTMenuBar;
    //Define Tables screen objects
    protected JFrame jfDT;

    protected JButton jbDTCreateDDL;
    protected JButton jbDTVarchar;
    protected JButton jbDTDefaultValue;
    protected JButton jbDTMoveUp;
    protected JButton jbDTMoveDown;
    protected JMenuItem jmiDROpenEdge;

    protected JRadioButton[] jrbDataType;
    protected String[] strDataType;
    protected JCheckBox jcheckDTDisallowNull;
    protected JCheckBox jcheckDTPrimaryKey;
    protected JTextField jtfDTDefaultValue;
    protected JTextField jtfDTVarchar;
    protected JList<String> jlDTTablesAll;
    protected JList<String> jlDTFieldsTablesAll;
    protected DefaultListModel<String> dlmDTTablesAll;
    protected DefaultListModel<String> dlmDTFieldsTablesAll;

    protected JMenuItem jmiDTSave;
    protected JMenuItem jmiDTOptionsShowProducts;

    //Define Relations screen objects
    protected JFrame jfDR;

    protected JButton jbDRCreateDDL;

    protected JButton jbDRBindRelation;
    protected JList<String> jlDRTablesRelations;
    protected JList<String> jlDRTablesRelatedTo;
    protected JList<String> jlDRFieldsTablesRelations;
    protected JList<String> jlDRFieldsTablesRelatedTo;

    protected DefaultListModel<String> dlmDRTablesRelations;
    protected DefaultListModel<String> dlmDRTablesRelatedTo;
    protected DefaultListModel<String> dlmDRFieldsTablesRelations;
    protected DefaultListModel<String> dlmDRFieldsTablesRelatedTo;

    protected JMenuItem jmiDROptionsShowProducts;

    public EdgeConvertGUI() {
        menuListener = new EdgeMenuListener(this);
        radioListener = new EdgeRadioButtonListener(this);
        edgeWindowListener = new EdgeWindowListener(this);
        createDDLListener = new CreateDDLButtonListener(this);
        defineRelationsButtonListener = new DefineRelationsButtonListener(this);
        moveUpButtonListener = new MoveUpButtonListener(this);
        moveDownButtonListener = new MoveDownButtonListener(this);
        defaultValueButtonListener = new DefaultValueButtonListener(this);
        setVarcharLengthButtonListener = new SetVarcharLengthButtonListener(this);
        defineTablesButtonListener = new DefineTablesButtonListener(this);
        bindRelationButtonListener = new BindRelationButtonListener(this);
        this.showGUI();
    } // EdgeConvertGUI.EdgeConvertGUI()

    public void showGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //use the OS native LAF, as opposed to default Java LAF
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            String s = "Error setting native LAF: " + e;
            logger.log(Level.FATAL, s);
        }
        createDTScreen();
        createDRScreen();
    } //showGUI()

    public void createDTScreen() {//create Define Tables screen

        jfDT = new JFrame(DEFINE_TABLES);
        jfDT.setLocation(HORIZ_LOC, VERT_LOC);
        jfDT.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jfDT.addWindowListener(edgeWindowListener);
        jfDT.getContentPane().setLayout(new BorderLayout());
        jfDT.setVisible(true);
        jfDT.setSize(HORIZ_SIZE + 150, VERT_SIZE);

        //setup menubars and menus
        jmbDTMenuBar = new JMenuBar();
        jfDT.setJMenuBar(jmbDTMenuBar);

        jmDTFile = new JMenu("File");
        jmDTFile.setMnemonic(KeyEvent.VK_F);
        jmbDTMenuBar.add(jmDTFile);

        jmiDTOpenEdge = new JMenuItem("Open File");
        jmiDTOpenEdge.setMnemonic(KeyEvent.VK_E);
        jmiDTOpenEdge.addActionListener(menuListener);

        jmiDTSave = new JMenuItem("Save");
        jmiDTSave.setMnemonic(KeyEvent.VK_S);
        jmiDTSave.setEnabled(false);
        jmiDTSave.addActionListener(menuListener);
        jmiDTSaveAs = new JMenuItem("Save As...");
        jmiDTSaveAs.setMnemonic(KeyEvent.VK_A);
        jmiDTSaveAs.setEnabled(false);
        jmiDTSaveAs.addActionListener(menuListener);
        jmiDTExit = new JMenuItem("Exit");
        jmiDTExit.setMnemonic(KeyEvent.VK_X);
        jmiDTExit.addActionListener(menuListener);
        jmDTFile.add(jmiDTOpenEdge);
        jmDTFile.add(jmiDTSave);
        jmDTFile.add(jmiDTSaveAs);
        jmDTFile.add(jmiDTExit);

        jmDTOptions = new JMenu("Options");
        jmDTOptions.setMnemonic(KeyEvent.VK_O);
        jmbDTMenuBar.add(jmDTOptions);
        jmiDTOptionsOutputLocation = new JMenuItem("Set Output File Definition Location");
        jmiDTOptionsOutputLocation.setMnemonic(KeyEvent.VK_S);
        jmiDTOptionsOutputLocation.addActionListener(menuListener);
        jmiDTOptionsShowProducts = new JMenuItem("Show Database Products Available");
        jmiDTOptionsShowProducts.setMnemonic(KeyEvent.VK_H);
        jmiDTOptionsShowProducts.setEnabled(false);
        jmiDTOptionsShowProducts.addActionListener(menuListener);
        jmDTOptions.add(jmiDTOptionsOutputLocation);
        jmDTOptions.add(jmiDTOptionsShowProducts);

        jmDTHelp = new JMenu("Help");
        jmDTHelp.setMnemonic(KeyEvent.VK_H);
        jmbDTMenuBar.add(jmDTHelp);
        jmiDTHelpAbout = new JMenuItem("About");
        jmiDTHelpAbout.setMnemonic(KeyEvent.VK_A);
        jmiDTHelpAbout.addActionListener(menuListener);
        jmDTHelp.add(jmiDTHelpAbout);

        jfcEdge = new JFileChooser();
        jfcOutputDir = new JFileChooser();
        effSave = new ExampleFileFilter("sav", "Edge Convert Save Files");
        jfcOutputDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        jpDTBottom = new JPanel(new GridLayout(1, 2));

        jbDTCreateDDL = new JButton(CREATE_DDL);
        jbDTCreateDDL.setEnabled(false);
        jbDTCreateDDL.addActionListener(createDDLListener);

        jbDTDefineRelations = new JButton(DEFINE_RELATIONS);
        jbDTDefineRelations.setEnabled(false);
        jbDTDefineRelations.addActionListener(defineRelationsButtonListener);

        jpDTBottom.add(jbDTDefineRelations);
        jpDTBottom.add(jbDTCreateDDL);
        jfDT.getContentPane().add(jpDTBottom, BorderLayout.SOUTH);

        jpDTCenter = new JPanel(new GridLayout(1, 3));
        jpDTCenterRight = new JPanel(new GridLayout(1, 2));
        dlmDTTablesAll = new DefaultListModel<>();
        jlDTTablesAll = new JList<>(dlmDTTablesAll);
        jlDTTablesAll.addListSelectionListener((ListSelectionEvent lse) -> {
            int selIndex = jlDTTablesAll.getSelectedIndex();
            if (selIndex >= 0) {
                String selText = dlmDTTablesAll.getElementAt(selIndex);
                setCurrentDTTable(selText); //set pointer to the selected table
                int[] currentNativeFields = getCurrentDTTable().getNativeFieldsArray();
                jlDTFieldsTablesAll.clearSelection();
                dlmDTFieldsTablesAll.removeAllElements();
                jbDTMoveUp.setEnabled(false);
                jbDTMoveDown.setEnabled(false);
                for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
                    dlmDTFieldsTablesAll.addElement(getFieldName(currentNativeFields[fIndex]));

                }
            }
            disableControls();
        });

        dlmDTFieldsTablesAll = new DefaultListModel<>();
        jlDTFieldsTablesAll = new JList<>(dlmDTFieldsTablesAll);
        jlDTFieldsTablesAll.addListSelectionListener((ListSelectionEvent lse) -> {
            int selIndex = jlDTFieldsTablesAll.getSelectedIndex();
            if (selIndex >= 0) {
                checkUpButton(selIndex);
                checkDownButton(selIndex);
                String selText = dlmDTFieldsTablesAll.getElementAt(selIndex);
                setCurrentDTField(selText); //set pointer to the selected field
                enableControls();
                jrbDataType[currentDTField.getDataType()].setSelected(true); //select the appropriate radio button, based on value of dataType
                jtfDTVarchar.setText(""); //clear the text field
                jbDTVarchar.setEnabled(false); //disable the button
                if (jrbDataType[0].isSelected()) { //this is the Varchar radio button
                    jbDTVarchar.setEnabled(true); //enable the Varchar button
                    jtfDTVarchar.setText(Integer.toString(currentDTField.getVarcharValue())); //fill text field with varcharValue
                }
                jcheckDTPrimaryKey.setSelected(currentDTField.getIsPrimaryKey()); //clear or set Primary Key checkbox
                jcheckDTDisallowNull.setSelected(currentDTField.getDisallowNull()); //clear or set Disallow Null checkbox
                jtfDTDefaultValue.setText(currentDTField.getDefaultValue()); //fill text field with defaultValue
            }
        });

        jpDTMove = new JPanel(new GridLayout(2, 1));
        jbDTMoveUp = new JButton("^");
        jbDTMoveUp.setEnabled(false);
        jbDTMoveUp.addActionListener(moveUpButtonListener);

        jbDTMoveDown = new JButton("v");
        jbDTMoveDown.setEnabled(false);
        jbDTMoveDown.addActionListener(moveDownButtonListener);
        jpDTMove.add(jbDTMoveUp);
        jpDTMove.add(jbDTMoveDown);

        jspDTTablesAll = new JScrollPane(jlDTTablesAll);
        jspDTFieldsTablesAll = new JScrollPane(jlDTFieldsTablesAll);
        jpDTCenter1 = new JPanel(new BorderLayout());
        jpDTCenter2 = new JPanel(new BorderLayout());
        jlabDTTables = new JLabel("All Tables", SwingConstants.CENTER);
        jlabDTFields = new JLabel("Fields List", SwingConstants.CENTER);
        jpDTCenter1.add(jlabDTTables, BorderLayout.NORTH);
        jpDTCenter2.add(jlabDTFields, BorderLayout.NORTH);
        jpDTCenter1.add(jspDTTablesAll, BorderLayout.CENTER);
        jpDTCenter2.add(jspDTFieldsTablesAll, BorderLayout.CENTER);
        jpDTCenter2.add(jpDTMove, BorderLayout.EAST);
        jpDTCenter.add(jpDTCenter1);
        jpDTCenter.add(jpDTCenter2);
        jpDTCenter.add(jpDTCenterRight);

        strDataType = EdgeField.getStrDataType(); //get the list of currently supported data types
        jrbDataType = new JRadioButton[strDataType.length]; //create array of JRadioButtons, one for each supported data type
        bgDTDataType = new ButtonGroup();
        jpDTCenterRight1 = new JPanel(new GridLayout(strDataType.length, 1));
        for (int i = 0; i < strDataType.length; i++) {
            jrbDataType[i] = new JRadioButton(strDataType[i]); //assign label for radio button from String array
            jrbDataType[i].setEnabled(false);
            jrbDataType[i].addActionListener(radioListener);
            bgDTDataType.add(jrbDataType[i]);
            jpDTCenterRight1.add(jrbDataType[i]);
        }
        jpDTCenterRight.add(jpDTCenterRight1);

        jcheckDTDisallowNull = new JCheckBox("Disallow Null");
        jcheckDTDisallowNull.setEnabled(false);
        jcheckDTDisallowNull.addItemListener((ItemEvent ie) -> {
            currentDTField.setDisallowNull(jcheckDTDisallowNull.isSelected());
            dataSaved = false;
        });

        jcheckDTPrimaryKey = new JCheckBox("Primary Key");
        jcheckDTPrimaryKey.setEnabled(false);
        jcheckDTPrimaryKey.addItemListener((ItemEvent ie) -> {
            currentDTField.setIsPrimaryKey(jcheckDTPrimaryKey.isSelected());
            dataSaved = false;
        });

        jbDTDefaultValue = new JButton("Set Default Value");
        jbDTDefaultValue.setEnabled(false);
        jbDTDefaultValue.addActionListener(defaultValueButtonListener); //jbDTDefaultValue.addActionListener
        jtfDTDefaultValue = new JTextField();
        jtfDTDefaultValue.setEditable(false);

        jbDTVarchar = new JButton("Set Varchar Length");
        jbDTVarchar.setEnabled(false);
        jbDTVarchar.addActionListener(setVarcharLengthButtonListener);
        jtfDTVarchar = new JTextField();
        jtfDTVarchar.setEditable(false);

        jpDTCenterRight2 = new JPanel(new GridLayout(6, 1));
        jpDTCenterRight2.add(jbDTVarchar);
        jpDTCenterRight2.add(jtfDTVarchar);
        jpDTCenterRight2.add(jcheckDTPrimaryKey);
        jpDTCenterRight2.add(jcheckDTDisallowNull);
        jpDTCenterRight2.add(jbDTDefaultValue);
        jpDTCenterRight2.add(jtfDTDefaultValue);
        jpDTCenterRight.add(jpDTCenterRight1);
        jpDTCenterRight.add(jpDTCenterRight2);
        jpDTCenter.add(jpDTCenterRight);
        jfDT.getContentPane().add(jpDTCenter, BorderLayout.CENTER);
        jfDT.validate();
    } //createDTScreen

    void checkDownButton(int selIndex) {
        jbDTMoveDown.setEnabled(true);
        if (selIndex == (dlmDTFieldsTablesAll.getSize() - 1)) {
            jbDTMoveDown.setEnabled(false);
        }
    }

    void checkUpButton(int selIndex) {
        jbDTMoveUp.setEnabled(true);
        if (selIndex == 0) {
            jbDTMoveUp.setEnabled(false);
        }
    }

    public void createDRScreen() {

        //create Define Relations screen
        jfDR = new JFrame(DEFINE_RELATIONS);
        jfDR.setSize(HORIZ_SIZE, VERT_SIZE);
        jfDR.setLocation(HORIZ_LOC, VERT_LOC);
        jfDR.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jfDR.addWindowListener(edgeWindowListener);
        jfDR.getContentPane().setLayout(new BorderLayout());

        //setup menubars and menus
        jmbDRMenuBar = new JMenuBar();
        jfDR.setJMenuBar(jmbDRMenuBar);
        jmDRFile = new JMenu("File");
        jmDRFile.setMnemonic(KeyEvent.VK_F);
        jmbDRMenuBar.add(jmDRFile);
        jmiDROpenEdge = new JMenuItem("Open Edge File");
        jmiDROpenEdge.setMnemonic(KeyEvent.VK_E);
        jmiDROpenEdge.addActionListener(menuListener);
        jmiDROpenSave = new JMenuItem("Open Save File");
        jmiDROpenSave.setMnemonic(KeyEvent.VK_V);
        jmiDROpenSave.addActionListener(menuListener);
        jmiDRSave = new JMenuItem("Save");
        jmiDRSave.setMnemonic(KeyEvent.VK_S);
        jmiDRSave.setEnabled(false);
        jmiDRSave.addActionListener(menuListener);
        jmiDRSaveAs = new JMenuItem("Save As...");
        jmiDRSaveAs.setMnemonic(KeyEvent.VK_A);
        jmiDRSaveAs.setEnabled(false);
        jmiDRSaveAs.addActionListener(menuListener);
        jmiDRExit = new JMenuItem("Exit");
        jmiDRExit.setMnemonic(KeyEvent.VK_X);
        jmiDRExit.addActionListener(menuListener);
        jmDRFile.add(jmiDROpenEdge);
        jmDRFile.add(jmiDROpenSave);
        jmDRFile.add(jmiDRSave);
        jmDRFile.add(jmiDRSaveAs);
        jmDRFile.add(jmiDRExit);

        jmDROptions = new JMenu("Options");
        jmDROptions.setMnemonic(KeyEvent.VK_O);
        jmbDRMenuBar.add(jmDROptions);
        jmiDROptionsOutputLocation = new JMenuItem("Set Output File Definition Location");
        jmiDROptionsOutputLocation.setMnemonic(KeyEvent.VK_S);
        jmiDROptionsOutputLocation.addActionListener(menuListener);
        jmiDROptionsShowProducts = new JMenuItem("Show Database Products Available");
        jmiDROptionsShowProducts.setMnemonic(KeyEvent.VK_H);
        jmiDROptionsShowProducts.setEnabled(false);
        jmiDROptionsShowProducts.addActionListener(menuListener);
        jmDROptions.add(jmiDROptionsOutputLocation);
        jmDROptions.add(jmiDROptionsShowProducts);

        jmDRHelp = new JMenu("Help");
        jmDRHelp.setMnemonic(KeyEvent.VK_H);
        jmbDRMenuBar.add(jmDRHelp);
        jmiDRHelpAbout = new JMenuItem("About");
        jmiDRHelpAbout.setMnemonic(KeyEvent.VK_A);
        jmiDRHelpAbout.addActionListener(menuListener);
        jmDRHelp.add(jmiDRHelpAbout);

        jpDRCenter = new JPanel(new GridLayout(2, 2));
        jpDRCenter1 = new JPanel(new BorderLayout());
        jpDRCenter2 = new JPanel(new BorderLayout());
        jpDRCenter3 = new JPanel(new BorderLayout());
        jpDRCenter4 = new JPanel(new BorderLayout());

        dlmDRTablesRelations = new DefaultListModel<>();
        jlDRTablesRelations = new JList<>(dlmDRTablesRelations);
        jlDRTablesRelations.addListSelectionListener((ListSelectionEvent lse) -> {
            int selIndex = jlDRTablesRelations.getSelectedIndex();
            if (selIndex >= 0) {
                String selText = dlmDRTablesRelations.getElementAt(selIndex);
                setCurrentDRTable1(selText);
                int[] currentNativeFields;
                int[] currentRelatedTables;
                currentNativeFields = getCurrentDRTable1().getNativeFieldsArray();
                currentRelatedTables = getCurrentDRTable1().getRelatedTablesArray();
                jlDRFieldsTablesRelations.clearSelection();
                jlDRTablesRelatedTo.clearSelection();
                jlDRFieldsTablesRelatedTo.clearSelection();
                dlmDRFieldsTablesRelations.removeAllElements();
                dlmDRTablesRelatedTo.removeAllElements();
                dlmDRFieldsTablesRelatedTo.removeAllElements();
                for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
                    dlmDRFieldsTablesRelations.addElement(getFieldName(currentNativeFields[fIndex]));
                }
                for (int rIndex = 0; rIndex < currentRelatedTables.length; rIndex++) {
                    dlmDRTablesRelatedTo.addElement(getTableName(currentRelatedTables[rIndex]));
                }
            }
        });

        dlmDRFieldsTablesRelations = new DefaultListModel<>();
        jlDRFieldsTablesRelations = new JList<>(dlmDRFieldsTablesRelations);
        jlDRFieldsTablesRelations.addListSelectionListener((ListSelectionEvent lse) -> {
            int selIndex = jlDRFieldsTablesRelations.getSelectedIndex();
            if (selIndex >= 0) {
                String selText = dlmDRFieldsTablesRelations.getElementAt(selIndex);
                setCurrentDRField1(selText);
                if (getCurrentDRField1().getFieldBound() == 0) {
                    jlDRTablesRelatedTo.clearSelection();
                    jlDRFieldsTablesRelatedTo.clearSelection();
                    dlmDRFieldsTablesRelatedTo.removeAllElements();
                } else {
                    jlDRTablesRelatedTo.setSelectedValue(getTableName(getCurrentDRField1().getTableBound()), true);
                    jlDRFieldsTablesRelatedTo.setSelectedValue(getFieldName(getCurrentDRField1().getFieldBound()), true);
                }
            }
        });

        dlmDRTablesRelatedTo = new DefaultListModel<>();
        jlDRTablesRelatedTo = new JList<>(dlmDRTablesRelatedTo);
        jlDRTablesRelatedTo.addListSelectionListener((ListSelectionEvent lse) -> {
            int selIndex = jlDRTablesRelatedTo.getSelectedIndex();
            relatedToText(selIndex);
        });

        dlmDRFieldsTablesRelatedTo = new DefaultListModel<>();
        jlDRFieldsTablesRelatedTo = new JList<>(dlmDRFieldsTablesRelatedTo);
        jlDRFieldsTablesRelatedTo.addListSelectionListener((ListSelectionEvent lse) -> {
            int selIndex = jlDRFieldsTablesRelatedTo.getSelectedIndex();
            jbDRBindRelation.setEnabled(false);
            selectedText(selIndex);
        });

        jspDRTablesRelations = new JScrollPane(jlDRTablesRelations);
        jspDRFieldsTablesRelations = new JScrollPane(jlDRFieldsTablesRelations);
        jspDRTablesRelatedTo = new JScrollPane(jlDRTablesRelatedTo);
        jspDRFieldsTablesRelatedTo = new JScrollPane(jlDRFieldsTablesRelatedTo);
        jlabDRTablesRelations = new JLabel("Tables With Relations", SwingConstants.CENTER);
        jlabDRFieldsTablesRelations = new JLabel("Fields in Tables with Relations", SwingConstants.CENTER);
        jlabDRTablesRelatedTo = new JLabel("Related Tables", SwingConstants.CENTER);
        jlabDRFieldsTablesRelatedTo = new JLabel("Fields in Related Tables", SwingConstants.CENTER);
        jpDRCenter1.add(jlabDRTablesRelations, BorderLayout.NORTH);
        jpDRCenter2.add(jlabDRFieldsTablesRelations, BorderLayout.NORTH);
        jpDRCenter3.add(jlabDRTablesRelatedTo, BorderLayout.NORTH);
        jpDRCenter4.add(jlabDRFieldsTablesRelatedTo, BorderLayout.NORTH);
        jpDRCenter1.add(jspDRTablesRelations, BorderLayout.CENTER);
        jpDRCenter2.add(jspDRFieldsTablesRelations, BorderLayout.CENTER);
        jpDRCenter3.add(jspDRTablesRelatedTo, BorderLayout.CENTER);
        jpDRCenter4.add(jspDRFieldsTablesRelatedTo, BorderLayout.CENTER);
        jpDRCenter.add(jpDRCenter1);
        jpDRCenter.add(jpDRCenter2);
        jpDRCenter.add(jpDRCenter3);
        jpDRCenter.add(jpDRCenter4);
        jfDR.getContentPane().add(jpDRCenter, BorderLayout.CENTER);
        jpDRBottom = new JPanel(new GridLayout(1, 3));

        jbDRDefineTables = new JButton(DEFINE_TABLES);
        jbDRDefineTables.addActionListener(defineTablesButtonListener);

        jbDRBindRelation = new JButton("Bind/Unbind Relation");
        jbDRBindRelation.setEnabled(false);
        jbDRBindRelation.addActionListener(bindRelationButtonListener);

        jbDRCreateDDL = new JButton(CREATE_DDL);
        jbDRCreateDDL.setEnabled(false);
        jbDRCreateDDL.addActionListener(createDDLListener);

        jpDRBottom.add(jbDRDefineTables);
        jpDRBottom.add(jbDRBindRelation);
        jpDRBottom.add(jbDRCreateDDL);
        jfDR.getContentPane().add(jpDRBottom, BorderLayout.SOUTH);
    } //createDRScreen

    void selectedText(int selIndex) {
        if (selIndex >= 0) {
            String selText = dlmDRFieldsTablesRelatedTo.getElementAt(selIndex);
            setCurrentDRField2(selText);
            jbDRBindRelation.setEnabled(true);
        }
    }

    void relatedToText(int selIndex) {
        if (selIndex >= 0) {
            String selText = dlmDRTablesRelatedTo.getElementAt(selIndex);
            setCurrentDRTable2(selText);
            int[] currentNativeFields = getCurrentDRTable2().getNativeFieldsArray();
            dlmDRFieldsTablesRelatedTo.removeAllElements();
            for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
                dlmDRFieldsTablesRelatedTo.addElement(getFieldName(currentNativeFields[fIndex]));
            }
        }
    }

    public static void setReadSuccess(boolean value) {
        readSuccess = value;
    }

    public boolean getReadSuccess() {
        return readSuccess;
    }

    private void setCurrentDTTable(String selText) {
        for (Table table : tables) {
            if (selText.equals(table.getName())) {
                currentDTTable = table;
                return;
            }
        }
    }

    public Table getCurrentDTTable() {
        return currentDTTable;
    }

    public void setCurrentDTField(String selText) {
        for (EdgeField field : fields) {
            if (selText.equals(field.getName()) && field.getTableID() == getCurrentDTTable().getNumFigure()) {
                currentDTField = field;
                return;
            }
        }
    }

    private void setCurrentDRTable1(String selText) {
        for (Table table : tables) {
            if (selText.equals(table.getName())) {
                currentDRTable1 = table;
                return;
            }
        }
    }

    public Table getCurrentDRTable1() {
        return currentDRTable1;
    }

    private void setCurrentDRTable2(String selText) {
        for (Table table : tables) {
            if (selText.equals(table.getName())) {
                currentDRTable2 = table;
                return;
            }
        }
    }

    public Table getCurrentDRTable2() {
        return currentDRTable2;
    }

    private void setCurrentDRField1(String selText) {
        for (EdgeField field : fields) {
            if (selText.equals(field.getName()) && field.getTableID() == currentDRTable1.getNumFigure()) {
                currentDRField1 = field;
                return;
            }
        }
    }

    public EdgeField getCurrentDRField1() {
        return currentDRField1;
    }

    private void setCurrentDRField2(String selText) {
        for (EdgeField field : fields) {
            if (selText.equals(field.getName()) && field.getTableID() == getCurrentDRTable2().getNumFigure()) {
                currentDRField2 = field;
                return;
            }
        }
    }

    public EdgeField getCurrentDRField2() {
        return currentDRField2;
    }

    public String getTableName(int numFigure) {
        for (Table table : tables) {
            if (table.getNumFigure() == numFigure) {
                return table.getName();
            }
        }
        return "";
    }

    public String getFieldName(int numFigure) {
        for (EdgeField field : fields) {
            if (field.getNumFigure() == numFigure) {
                return field.getName();
            }
        }
        return "";
    }

    private void enableControls() {
        for (int i = 0; i < strDataType.length; i++) {
            jrbDataType[i].setEnabled(true);
        }
        jcheckDTPrimaryKey.setEnabled(true);
        jcheckDTDisallowNull.setEnabled(true);
        jbDTVarchar.setEnabled(true);
        jbDTDefaultValue.setEnabled(true);
    }

    private void disableControls() {
        for (int i = 0; i < strDataType.length; i++) {
            jrbDataType[i].setEnabled(false);
        }
        jcheckDTPrimaryKey.setEnabled(false);
        jcheckDTDisallowNull.setEnabled(false);
        jbDTDefaultValue.setEnabled(false);
        jtfDTVarchar.setText("");
        jtfDTDefaultValue.setText("");
    }

    public void clearDTControls() {
        jlDTTablesAll.clearSelection();
        jlDTFieldsTablesAll.clearSelection();
    }

    public void clearDRControls() {
        jlDRTablesRelations.clearSelection();
        jlDRTablesRelatedTo.clearSelection();
        jlDRFieldsTablesRelations.clearSelection();
        jlDRFieldsTablesRelatedTo.clearSelection();
    }

    public void depopulateLists() {
        dlmDTTablesAll.clear();
        dlmDTFieldsTablesAll.clear();
        dlmDRTablesRelations.clear();
        dlmDRFieldsTablesRelations.clear();
        dlmDRTablesRelatedTo.clear();
        dlmDRFieldsTablesRelatedTo.clear();
    }

    public synchronized void populateLists() {
        if (readSuccess) {
            jfDT.setVisible(true);
            jfDR.setVisible(false);
            disableControls();
            depopulateLists();
            for (Table table : tables) {
                String tempName = table.getName();
                dlmDTTablesAll.addElement(tempName);
                int[] relatedTables = table.getRelatedTablesArray();
                if (relatedTables.length > 0) {
                    dlmDRTablesRelations.addElement(tempName);
                }
            }
        }
        readSuccess = true;
    }

    protected void saveAs() {
        int returnVal;
        jfcEdge.addChoosableFileFilter(effSave);
        returnVal = jfcEdge.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            saveFile = jfcEdge.getSelectedFile();
            if (saveFile.exists()) {
                int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "Confirm Overwrite",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            if (!saveFile.getName().endsWith("sav")) {
                String temp = saveFile.getAbsolutePath() + ".sav";
                saveFile = new File(temp);
            }
            jmiDTSave.setEnabled(true);
            truncatedFilename = saveFile.getName().substring(saveFile.getName().lastIndexOf(File.separator) + 1);
            jfDT.setTitle(DEFINE_TABLES + " - " + truncatedFilename);
            jfDR.setTitle(DEFINE_RELATIONS + " - " + truncatedFilename);
        } else {
            return;
        }
        writeSave();
    }

    protected void writeSave() {
        if (saveFile != null) {
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(saveFile, false)));
                //write the identification line
                pw.println(EdgeConvertFileParser.SAVE_ID);
                //write the tables 
                pw.println("#Tables#");
                for (Table table : tables) {
                    pw.println(table);
                }
                //write the fields
                pw.println("#Fields#");
                for (EdgeField field : fields) {
                    pw.println(field);
                }
                //close the file
                pw.close();
            } catch (IOException ioe) {
                logger.log(Level.FATAL, ioe);
            }
            dataSaved = true;
        }
    }

    public int setOutputDir() {
        int returnVal;
        File outputDirOld;
        outputDirOld = outputDir;
        alSubclasses = new ArrayList<>();
        alProductNames = new ArrayList<>();

        returnVal = jfcOutputDir.showOpenDialog(null);

        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return 0;
        }

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            outputDir = jfcOutputDir.getSelectedFile();
        }

        getOutputClasses();

        if (alProductNames.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The path:\n" + outputDir + "\ncontains no valid output definition files.");
            outputDir = outputDirOld;
        }

        if ((parseFile != null || saveFile != null) && outputDir != null) {
            jbDTCreateDDL.setEnabled(true);
            jbDRCreateDDL.setEnabled(true);
        }

        JOptionPane.showMessageDialog(null, "The available products to create DDL statements are:\n" + displayProductNames());
        jmiDTOptionsShowProducts.setEnabled(true);
        jmiDROptionsShowProducts.setEnabled(true);
        return 1;
    }

    protected String displayProductNames() {
        StringBuilder sb = new StringBuilder();
        for (String productName : productNames) {
            sb.append(productName).append("\n");
        }
        return sb.toString();
    }

    protected void getOutputClasses() {
        File[] resultFiles;
        Class<?> resultClass;
        Class[] paramTypes = {Table[].class, EdgeField[].class};
        Class[] paramTypesNull = {};
        Constructor<?> conResultClass;
        Object[] args = {tables, fields};
        Object objOutput = null;

        resultFiles = outputDir.listFiles();
        alProductNames.clear();
        alSubclasses.clear();
        try {
            for (File resultFile : resultFiles) {

                try {
                    logger.log(Level.FATAL, resultFile.getName());
                    if (!resultFile.getName().endsWith(".class")) {
                        continue; //ignore all files that are not .class files
                    }
                    resultClass = Class.forName(resultFile.getName().substring(0, resultFile.getName().lastIndexOf(".")));
                    
                    if (resultClass.getSuperclass() != null && resultClass.getSuperclass().getName().equals("DDLGenerator")) {
                        if (parseFile == null && saveFile == null) {
                            resultClass.getConstructor(paramTypesNull);
                        } else {
                            conResultClass = resultClass.getConstructor(paramTypes);
                            objOutput = conResultClass.newInstance(args);
                        }
                        alSubclasses.add(objOutput);
                        Method getProductName = resultClass.getMethod("getProductName", null);
                        String productName = (String) getProductName.invoke(objOutput, null);
                        alProductNames.add(productName);
                    }

                } catch (Exception e) {
                    logger.log(Level.FATAL, e);
                }
            }
        } catch (Exception ie) {
            logger.log(Level.FATAL, ie);
        }
        if (!alProductNames.isEmpty() && !alSubclasses.isEmpty()) { //do not recreate productName and objSubClasses arrays if the new path is empty of valid files
            productNames = alProductNames.toArray(new String[alProductNames.size()]);
            objSubclasses = alSubclasses.toArray(new Object[alSubclasses.size()]);
        }
    }

    protected String getSQLStatements() {
        String strSQLString = "";
        String response = (String) JOptionPane.showInputDialog(
                null,
                "Select a product:",
                CREATE_DDL,
                JOptionPane.PLAIN_MESSAGE,
                null,
                productNames,
                null);

        if (response == null) {
            return EdgeConvertGUI.CANCELLED;
        }

        
        DDLGenerator generator;
        generator = DDLGeneratorFactory.getInstance(response);
        
        generator.initialize(tables, fields);
        
        strSQLString = generator.getSQLString();
        databaseName = generator.getDatabaseName();

        return strSQLString;
    }

    protected void writeSQL(String output) {
        jfcEdge.resetChoosableFileFilters();
        File outputFile;
        if (parseFile != null) {
            outputFile = new File(parseFile.getAbsolutePath().substring(0, (parseFile.getAbsolutePath().lastIndexOf(File.separator) + 1)) + databaseName + ".sql");
        } else {
            outputFile = new File(saveFile.getAbsolutePath().substring(0, (saveFile.getAbsolutePath().lastIndexOf(File.separator) + 1)) + databaseName + ".sql");
        }
        if (this.databaseName == null || databaseName.equals("")) {
            databaseName = "MySQL";
        }
        jfcEdge.setSelectedFile(outputFile);
        int returnVal = jfcEdge.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            outputFile = jfcEdge.getSelectedFile();
            if (outputFile.exists()) {
                int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "Confirm Overwrite",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, false)));
                //write the SQL statements
                pw.println(output);
                //close the file
                pw.close();
            } catch (IOException ioe) {
                logger.log(Level.FATAL, ioe);
            }
        }
    }
} // EdgeConvertGUI
