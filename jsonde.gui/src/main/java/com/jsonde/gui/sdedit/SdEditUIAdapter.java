package com.jsonde.gui.sdedit;

import com.jsonde.client.ClassListener;
import com.jsonde.client.Client;
import com.jsonde.client.MethodCallListener;
import com.jsonde.client.dao.DaoFactory;
import com.jsonde.client.domain.Clazz;
import com.jsonde.client.domain.Method;
import com.jsonde.client.domain.MethodCall;
import com.jsonde.gui.ApplicationUserInterface;
import com.jsonde.gui.Main;
import com.jsonde.gui.action.*;
import com.jsonde.gui.action.composite.CreateCompositeComponentTabAction;
import com.jsonde.gui.action.reports.ReportActions;
import com.jsonde.gui.components.JActionIcon;
import com.jsonde.gui.components.JActionLabel;
import com.jsonde.gui.components.accordion.JAccordionPanel;
import com.jsonde.gui.components.listpane.DefaultListPaneModel;
import com.jsonde.gui.components.listpane.JListPane;
import com.jsonde.gui.dialog.error.ErrorsDialog;
import com.jsonde.gui.reports.ReportCompositeComponentProvider;
import com.jsonde.gui.reports.ReportException;
import com.jsonde.gui.reports.Reports;
import com.jsonde.gui.tree.Node;
import net.sf.sdedit.config.ConfigurationManager;
import net.sf.sdedit.editor.Editor;
import net.sf.sdedit.ui.UserInterface;
import net.sf.sdedit.ui.components.ATabbedPane;
import net.sf.sdedit.ui.components.ATabbedPaneListener;
import net.sf.sdedit.ui.impl.UserInterfaceImpl;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;

public class SdEditUIAdapter implements MethodCallListener, ApplicationUserInterface, ClassListener {

    private UserInterface userInterface;
    private JFrame frame;
    private Editor editor;
    private JTree tree;
    private DefaultTreeModel treeModel;

    private java.util.List<Action> reportActions;

    public SdEditUIAdapter() {

        try {

            reportActions = ReportActions.getInstance(this).getActions();

            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {

                    editor = Editor.getEditor();

                    userInterface = editor.getUI();

                    frame = (JFrame) userInterface;

                    processFrame();
                    processMenuBar();
                    processToolBar();

                    createStatusBar();

                    createTree();
                    addTree();

                    JPanel welcomePanel = new JPanel();
                    welcomePanel.setLayout(new BorderLayout());
                    welcomePanel.add(new JLabel("Welcome to jSonde"), BorderLayout.CENTER);

                    addTab(welcomePanel, "Welcome to jSonde");

                    frame.repaint();

                }
            });



        } catch (Exception e) {
            Main.getInstance().processException(e);
        }
    }

    private JLabel instrumentedClassesLabel;
    private long instrumentedClassesCount;

    private JActionLabel errorsLabel;

    public void onRegisterClass(Clazz clazz) {
        instrumentedClassesCount++;
        instrumentedClassesLabel.setText("Instrumented " + instrumentedClassesCount + " classes");
    }

    private StringWriter exceptionStringWriter = new StringWriter();
    private PrintWriter exceptionPrintWriter = new PrintWriter(exceptionStringWriter);

    private long exceptionsCounter;

    public void processException(Throwable e) {
        e.printStackTrace(exceptionPrintWriter);
        exceptionsCounter++;
        errorsLabel.setText(exceptionsCounter + " Errors");
    }

    private void createStatusBar() {

        JPanel statusBar = new JPanel();

        statusBar.setLayout(new GridBagLayout());

        JLabel statusLabel = new JLabel("volatile text");
        statusLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.DARK_GRAY));

        instrumentedClassesLabel = new JLabel("Instrumented 0 classes");
        instrumentedClassesLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.DARK_GRAY));

        errorsLabel = new JActionLabel(new AbstractAction() {

            {
                putValue(Action.NAME, "0 Errors");
            }

            public void actionPerformed(ActionEvent e) {
                ErrorsDialog errorsDialog = new ErrorsDialog(exceptionStringWriter.getBuffer());
                errorsDialog.setVisible(true);
            }

        });
        errorsLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.DARK_GRAY));

        // Memory usage progress bar

        final JProgressBar jProgressBar = new JProgressBar();
        jProgressBar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.DARK_GRAY));
        jProgressBar.setStringPainted(true);
        jProgressBar.setMinimum(0);

        javax.swing.Timer timer = new javax.swing.Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                Runtime runtime = Runtime.getRuntime();

                int freeMemory = (int)
                        (runtime.freeMemory() / 1024 / 1024);
                int totalMemory = (int)
                        (Runtime.getRuntime().totalMemory() / 1024 / 1024);
                int usedMemory = totalMemory - freeMemory;

                jProgressBar.setMaximum(totalMemory);
                jProgressBar.setValue(usedMemory);
                jProgressBar.setString(usedMemory + "M of " + totalMemory + "M");

            }

        });

        timer.start();

        // Garbage collection button

        JActionIcon gcButton = new JActionIcon(new AbstractAction() {

            {
                putValue(Action.SMALL_ICON,
                        new ImageIcon(
                                getClass().getClassLoader().getResource("trashcan.png")
                        )
                );
            }

            public void actionPerformed(ActionEvent e) {
                System.gc();
            }

        });


        int x = 0;

        statusBar.add(
                statusLabel,
                new GridBagConstraints(
                        x, 0,
                        1, 1,
                        1, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 2, 2, 2), 3, 0
                )
        );

        x++;

        statusBar.add(
                instrumentedClassesLabel,
                new GridBagConstraints(
                        x, 0,
                        1, 1,
                        0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(2, 2, 2, 2), 3, 0
                )
        );

        x++;

        statusBar.add(
                errorsLabel,
                new GridBagConstraints(
                        x, 0,
                        1, 1,
                        0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(2, 2, 2, 2), 3, 0
                )
        );

        x++;

        statusBar.add(
                jProgressBar,
                new GridBagConstraints(
                        x, 0,
                        1, 1,
                        0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(2, 2, 2, 2), 3, 0
                )
        );

        x++;

        statusBar.add(
                gcButton,
                new GridBagConstraints(
                        x, 0,
                        1, 1,
                        0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(2, 2, 2, 2), 3, 0
                )
        );

        frame.getContentPane().add(statusBar, BorderLayout.SOUTH);

    }

    public UserInterface getUserInterface() {
        return userInterface;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Editor getEditor() {
        return editor;
    }

    private ATabbedPane tabbedPane;

    public ATabbedPane getTabbedPane() {
        if (null == tabbedPane) {
            try {
                Field tabbedPaneField = UserInterfaceImpl.class.getDeclaredField("tabbedPane");
                tabbedPaneField.setAccessible(true);
                tabbedPane = (ATabbedPane) tabbedPaneField.get(userInterface);

                Field stainedImageField = ATabbedPane.class.getDeclaredField("stain");
                stainedImageField.setAccessible(true);

                Field cleanImageField = ATabbedPane.class.getDeclaredField("clean");
                cleanImageField.setAccessible(true);

                stainedImageField.set(null, cleanImageField.get(null));

                Field listenersField = ATabbedPane.class.getDeclaredField("listeners");
                listenersField.setAccessible(true);
                java.util.List aTabbedPaneListeners =
                        (java.util.List) listenersField.get(tabbedPane);

                aTabbedPaneListeners.clear();
                tabbedPane.addListener(new ATabbedPaneListener() {

                    public void currentTabClosing() {
                        new CloseDiagramAction(SdEditUIAdapter.this).actionPerformed(null);
                    }

                });

            } catch (Exception e) {
                Main.getInstance().processException(e);
            }
        }
        return tabbedPane;
    }

    public Client getClient() {
        return client;
    }

    public void addTab(Component component, String title) {
        getTabbedPane().addTab(component, title);
    }

    public void onMethodCall(MethodCall methodCall) {

        try {
            Method method = DaoFactory.getMethodDao().get(
                    methodCall.getMethodId()
            );

            String methodName = method.getName();

            String className =
                    null == methodCall.getActualClassId() ?
                            DaoFactory.getClazzDao().get(method.getClassId()).getName() :
                            DaoFactory.getClazzDao().get(methodCall.getActualClassId()).getName();

            //

            Node rootNode = (Node) treeModel.getRoot();

            String[] packages = className.split("\\.");

            Node contextNode = rootNode;

            for (int i = 0; i < packages.length - 1; i++) {

                String packageName = packages[i];

                boolean nodeFound = false;

                Enumeration children = contextNode.children();
                while (children.hasMoreElements()) {
                    Node node = (Node) children.nextElement();

                    if (node.getAllowsChildren() && (node.getValue().equals(packageName))) {
                        contextNode = node;
                        nodeFound = true;
                        break;
                    }

                }

                if (!nodeFound) {
                    Node node = new Node(packageName);
                    node.setAllowsChildren(true);
                    treeModel.insertNodeInto(node, contextNode, contextNode.getChildCount());
                    contextNode = node;
                }

            }

            String value = packages[packages.length - 1] + "." + methodName;
            Node node = new Node(value, methodCall);
            node.setAllowsChildren(false);

            treeModel.insertNodeInto(node, contextNode, contextNode.getChildCount());

        } catch (Exception e) {
            Main.getInstance().processException(e);
        }

    }

    private void createTree() {
        tree = new JTree();

        Node root = new Node("Packages");
        treeModel = new DefaultTreeModel(root);

        tree.setModel(treeModel);

        tree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if ((MouseEvent.BUTTON1 == e.getButton()) && (2 == e.getClickCount())) {

                    TreePath treePath =
                            tree.getPathForLocation(e.getX(), e.getY());

                    if (null != treePath) {
                        Node selectedNode = (Node) treePath.getLastPathComponent();

                        if (selectedNode.isLeaf() && !selectedNode.getAllowsChildren()) {

                            MethodCall methodCall = selectedNode.getRootMethodCall();

                            renderDiagram(methodCall);

                        }

                    }
                }

            }

        });

    }

    private void renderDiagram(MethodCall methodCall) {

        try {
            Method method = DaoFactory.getMethodDao().get(
                    methodCall.getMethodId()
            );

            String methodName = method.getName();

            String className =
                    null == methodCall.getActualClassId() ?
                            DaoFactory.getClazzDao().get(method.getClassId()).getName() :
                            DaoFactory.getClazzDao().get(methodCall.getActualClassId()).getName();

            String title = editor.getUI().addTab(className + "." + methodName,
                    ConfigurationManager.createNewDefaultConfiguration());

            SdEditDataRenderer sdEditDataRenderer = new SdEditDataRenderer();
            editor.getUI().appendText(title, sdEditDataRenderer.processMethodCall(methodCall));

        } catch (Exception e) {
            Main.getInstance().processException(e);
        }

    }


    private void addTree() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(tree);

        JAccordionPanel jAccordionPanel = new JAccordionPanel();
        jAccordionPanel.addPanel(
                "Sequence Diagrams",
                new ImageIcon(
                        getClass().getClassLoader().getResource("kdisknav.png")
                ),
                scrollPane);

        JListPane reportsListPane = getReportsListPane();
        jAccordionPanel.addPanel(
                "Reports",
                new ImageIcon(
                        getClass().getClassLoader().getResource("3d.png")
                ),
                reportsListPane);

        JListPane profilerListPane = getProfilerListPane();
        jAccordionPanel.addPanel(
                "Profiler",
                new ImageIcon(
                        getClass().getClassLoader().getResource("agt_add-to-autorun.png")
                ),
                profilerListPane);

        Component sdEditTabbedPane = frame.getContentPane().getComponent(0);

        JSplitPane jSondeSplitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jAccordionPanel, sdEditTabbedPane);

        frame.getContentPane().remove(sdEditTabbedPane);
        frame.getContentPane().add(jSondeSplitter, BorderLayout.CENTER);
    }

    private JListPane getReportsListPane() {

        DefaultListPaneModel reportsListPaneModel = new DefaultListPaneModel();

        for (Action action : reportActions) {
            reportsListPaneModel.addListPaneItem(action);
        }

        return new JListPane(reportsListPaneModel);
    }

    private JListPane getProfilerListPane() {

        DefaultListPaneModel reportsListPaneModel = new DefaultListPaneModel();

        reportsListPaneModel.addListPaneItem(openMethodCallProfilerViewAction);
        reportsListPaneModel.addListPaneItem(openMemoryHeapViewAction);
        //reportsListPaneModel.addListPaneItem(openMemoryTelemetryViewAction);

        try {
            Action a = new CreateCompositeComponentTabAction(
                    this,
                    new ReportCompositeComponentProvider(
                            Reports.getReport("memory-telemetry")
                    )
            );
            reportsListPaneModel.addListPaneItem(a);
        } catch (ReportException e) {
            Main.getInstance().processException(e);
        }

        try {
            Action a = new CreateCompositeComponentTabAction(
                    this,
                    new ReportCompositeComponentProvider(
                            Reports.getReport("class-telemetry")
                    )
            );
            reportsListPaneModel.addListPaneItem(a);
        } catch (ReportException e) {
            Main.getInstance().processException(e);
        }

        return new JListPane(reportsListPaneModel);
    }

    private void processToolBar() {

        JToolBar toolBar = (JToolBar)
                frame.getContentPane().getComponent(1);

        int[] indexes = {0, 1, 2, 3, 6, 7, 8, 9, 10, 11, 12, 13, 21, 22};

        java.util.List<Component> components = new ArrayList<Component>(indexes.length);

        for (int index : indexes) {
            components.add(toolBar.getComponentAtIndex(index));
        }

        for (Component component : components) {
            toolBar.remove(component);
        }

    }

    private Client client;

    public void setClient(Client client) {
        this.client = client;
        client.addMethodCallListener(this);
        client.addClassListener(this);

        instrumentedClassesLabel.setText("Instrumented " + client.getClassCount() + " classes");
    }

    private Action openMethodCallProfilerViewAction =
            new OpenMethodCallProfilerViewAction(this);
    private Action openMemoryHeapViewAction =
            new OpenMemoryHeapViewAction(this);

    private void processMenuBar() {

        JMenuBar menuBar = frame.getRootPane().getJMenuBar();

        menuBar.removeAll();

        JMenu fileMenu = new JMenu("File");

        fileMenu.add(new NewProjectAction(this));
        fileMenu.add(new NewLocalSunJVMProjectAction(this));
        fileMenu.add(new OpenProjectAction(this));
        fileMenu.add(new CloseAllDiagramsAction(this));
        fileMenu.add(new ExitJSondeAction(this));

        menuBar.add(fileMenu);

        JMenu reportsMenu = new JMenu("Reports");

        for (Action action : reportActions) {
            reportsMenu.add(action);
        }

        menuBar.add(reportsMenu);

        JMenu profilingMenu = new JMenu("Profiling");

        profilingMenu.add(openMethodCallProfilerViewAction);
        profilingMenu.add(openMemoryHeapViewAction);
        //profilingMenu.add(openMemoryTelemetryViewAction);

        try {
            Action a = new CreateCompositeComponentTabAction(
                    this,
                    new ReportCompositeComponentProvider(
                            Reports.getReport("memory-telemetry")
                    )
            );
            profilingMenu.add(a);
        } catch (ReportException e) {
            Main.getInstance().processException(e);
        }

        try {
            Action a = new CreateCompositeComponentTabAction(
                    this,
                    new ReportCompositeComponentProvider(
                            Reports.getReport("class-telemetry")
                    )
            );
            profilingMenu.add(a);
        } catch (ReportException e) {
            Main.getInstance().processException(e);
        }


        menuBar.add(profilingMenu);

        JMenu helpMenu = new JMenu("Help");

        helpMenu.add(new HelpAction(this));
        helpMenu.add(new AboutAction(this));

        menuBar.add(helpMenu);

    }

    private void processFrame() {

        for (WindowListener windowListener : frame.getWindowListeners()) {
            frame.removeWindowListener(windowListener);
        }

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

        });

        frame.setTitle("jSonde GUI version 1.1.0");

        frame.addPropertyChangeListener("title", new PropertyChangeListener() {

            private boolean revertingTitle;

            public void propertyChange(PropertyChangeEvent evt) {
                if (!revertingTitle) {
                    revertingTitle = true;
                    frame.setTitle((String) evt.getOldValue());
                } else {
                    revertingTitle = false;
                }
            }

        });

    }

}
