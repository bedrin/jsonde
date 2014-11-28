package com.jsonde.gui.components.composite;

import javax.swing.*;

public interface CompositeComponentProvider {

    JComponent createCompositeComponent();

    String getTitle();

    Icon getSmallIcon();

    Icon getLargeIcon();

}
