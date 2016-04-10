/*
 * Copyright (c) 2016 the original author or authors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package hu.donat.quicksearch.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

/**
 * @author Donát Csikós
 */
public final class QuickSearchManager {

    private static Map<Tree, QuickSearch> quickSearches = new HashMap<>();

    private QuickSearchManager() {
    }

    public static void addQuickSearchToAllTrees(Display display) {
        for (Shell shell : display.getShells()) {
            for (Control c : shell.getChildren()) {
                traverseControls(c);
            }
        }
    }

    private  static void traverseControls(Control c) {
        if (c instanceof Tree) {
            addQuickSearchToTree((Tree) c);
        }
        if (c instanceof Composite) {
            for (Control child : ((Composite) c).getChildren()) {
                traverseControls(child);
            }
        }
    }

    private static void addQuickSearchToTree(Tree tree) {
        if (!quickSearches.containsKey(tree)) {
            OverlayLabel overlayLabel = OverlayLabel.attach(tree);
            quickSearches.put(tree, new QuickSearch(tree, overlayLabel));
        }
    }
}
