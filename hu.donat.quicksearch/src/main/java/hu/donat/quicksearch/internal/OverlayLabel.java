/*
 * Copyright (c) 2016 the original author or authors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package hu.donat.quicksearch.internal;

import com.google.common.base.Preconditions;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Donát Csikós
 */
public final class OverlayLabel {

    private final Control target;
    private final Shell shell;
    private final Label label;

    private OverlayLabel(Control control) {
        this.target = Preconditions.checkNotNull(control);
        this.shell = new Shell(control.getShell(), SWT.MODELESS);
        this.shell.setLayout(GridLayoutFactory.fillDefaults().create());
        this.label = new Label(this.shell, SWT.NONE);
        this.label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    public void updateText(String text) {
        if (text == null) {
            this.shell.setVisible(false);
            this.label.setText("");
        } else {
            this.label.setText(text);
            reposition();
            this.shell.setVisible(true);
        }
    }

    private void reposition() {
        this.shell.pack();
        this.shell.setLocation(calculateLocation());
    }

    private Point calculateLocation() {
        // display the label on the right side of the tree if it can fit on the screen
        // otherwise display the label inside the tree's bounds on the right side
        int  displayWidth = this.shell.getDisplay().getClientArea().width;
        int shellWidth = this.shell.getBounds().width;
        int targetWidth  = this.target.getBounds().width;
        int targetRightCorner = this.target.toDisplay(this.target.getBounds().width, 0).x;
        if(targetRightCorner + shellWidth < displayWidth) {
            return this.target.toDisplay(targetWidth, 0);
        } else {
            return this.target.toDisplay(targetWidth - shellWidth, 0);
        }
    }

    public static OverlayLabel attach(Control control) {
        OverlayLabel overlayTextShell = new OverlayLabel(control);
        control.addFocusListener(overlayTextShell.new WidgetFocusListener());
        control.addDisposeListener(overlayTextShell.new WidgetDisposedListener());
        control.addControlListener(overlayTextShell.new WidgetControlListener());
        return overlayTextShell;
    }

    private final class WidgetFocusListener extends FocusAdapter {

        @Override
        public void focusLost(FocusEvent e) {
            if (!OverlayLabel.this.shell.isDisposed()) {
                OverlayLabel.this.shell.setVisible(false);
            }
        }
    }

    private final class WidgetDisposedListener implements DisposeListener {

        @Override
        public void widgetDisposed(DisposeEvent e) {
            OverlayLabel.this.shell.dispose();
        }
    }
    private final class WidgetControlListener implements ControlListener {

        @Override
        public void controlMoved(ControlEvent e) {
            reposition();
        }

        @Override
        public void controlResized(ControlEvent e) {
            reposition();
        }
    }

}
