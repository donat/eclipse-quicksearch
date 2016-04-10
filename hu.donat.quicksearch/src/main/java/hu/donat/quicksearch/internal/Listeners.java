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

import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Donát Csikós
 */
public final class Listeners {

    private Listeners() {
    }

    public static void addListeners() {
        PlatformUI.getWorkbench().addWindowListener(new WindowListener());
        for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
            Map<IWorkbenchPage, IPartListener> partListeners = new HashMap<>();
            for (IWorkbenchPage page : window.getPages()) {
                PartListener partListener = new PartListener();
                partListeners.put(page, partListener);
                page.addPartListener(partListener);
            }
            window.addPageListener(new PageListener(partListeners));
        }
    }

    public static class WindowListener implements IWindowListener {

        private final Map<IWorkbenchWindow, IPageListener> listeners = new HashMap<>();

        @Override
        public void windowActivated(IWorkbenchWindow window) {
        }

        @Override
        public void windowDeactivated(IWorkbenchWindow window) {
        }

        @Override
        public void windowClosed(IWorkbenchWindow window) {
            IPageListener listener = this.listeners.remove(window);
            if (listener != null) {
                window.removePageListener(listener);
            }
        }

        @Override
        public void windowOpened(IWorkbenchWindow window) {
            PageListener listener = new PageListener();
            this.listeners.put(window, listener);
            window.addPageListener(listener);
        }
    }

    public static class PageListener implements IPageListener {

        private final Map<IWorkbenchPage, IPartListener> listeners;

        public PageListener() {
            this.listeners = new HashMap<>();
        }

        public PageListener(Map<IWorkbenchPage, IPartListener> partListeners) {
            this.listeners = new HashMap<>(partListeners);
        }

        @Override
        public void pageActivated(IWorkbenchPage page) {
        }

        @Override
        public void pageClosed(IWorkbenchPage page) {
            IPartListener listener = this.listeners.remove(page);
            if (listener != null) {
                page.removePartListener(listener);
            }
        }

        @Override
        public void pageOpened(IWorkbenchPage page) {
            PartListener listener = new PartListener();
            this.listeners.put(page, listener);
            page.addPartListener(listener);
        }

    }

    public static class PartListener implements IPartListener {

        @Override
        public void partActivated(IWorkbenchPart part) {
        }

        @Override
        public void partBroughtToTop(IWorkbenchPart part) {
        }

        @Override
        public void partClosed(IWorkbenchPart part) {
        }

        @Override
        public void partDeactivated(IWorkbenchPart part) {
        }

        @Override
        public void partOpened(IWorkbenchPart part) {
            QuickSearchManager.addQuickSearchToAllTrees(part.getSite().getShell().getDisplay());
        }

    }
}
