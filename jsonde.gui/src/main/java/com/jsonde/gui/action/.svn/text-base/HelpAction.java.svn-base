package com.jsonde.gui.action;

import com.jsonde.gui.ApplicationUserInterface;
import com.jsonde.gui.Main;
import com.jsonde.gui.sdedit.SdEditUIAdapter;
import net.sf.sdedit.icons.Icons;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

public class HelpAction extends AbstractAction {

    private ApplicationUserInterface applicationUserInterface;

    public HelpAction(SdEditUIAdapter sdEditUIAdapter) {
        this.applicationUserInterface = sdEditUIAdapter;
    }

    {
        putValue(Action.SMALL_ICON,
                new ImageIcon(
                        Icons.class.getResource("help.png")
                ));
        putValue(Action.NAME, "Help");
        putValue(Action.SHORT_DESCRIPTION, "Open jSonde Help");
    }

    public void actionPerformed(ActionEvent e) {

        JComponent helpPane = createHTMLPane();

        applicationUserInterface.addTab(helpPane, "Help");

    }

    private JScrollPane createHTMLPane() {

        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        HTMLDocument htmlDocument =
                (HTMLDocument) htmlEditorKit.createDefaultDocument();

        URL baseURL = getClass().getClassLoader().getResource(("help/help.html"));
        htmlDocument.setBase(baseURL);

        JEditorPane editor = new JEditorPane();
        editor.setEditable(false);

        editor.setEditorKit(htmlEditorKit);
        try {
            String resPath = "help/help.html";
            editor.read(getClass().getClassLoader().getResourceAsStream(resPath), htmlDocument);
        }
        catch (IOException e) {
            Main.getInstance().processException(e);
        }

        return new JScrollPane(editor);
    }

}