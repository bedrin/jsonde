package com.jsonde.gui.dialog;

import javax.swing.*;
import java.awt.*;

public abstract class JSondeDialog extends JDialog {

    protected Rectangle getFrameBounds() {

        Dimension wizardDimension = getSize();

        double width = wizardDimension.getWidth();
        double height = wizardDimension.getHeight();

        Rectangle rectangle = new Rectangle(
                new Point(0, 0), Toolkit.getDefaultToolkit().getScreenSize()
        );

        double f = 1.0F;
        int i = (int) (f * width);
        int j = (int) (f * height);
        int k = rectangle.width - (i != -1 ? i : getWidth());
        int l = rectangle.height - (j != -1 ? j : getHeight());
        rectangle.x += k / 2;
        rectangle.width -= k;
        rectangle.y += l / 2;
        rectangle.height -= l;
        return rectangle;

    }

}
