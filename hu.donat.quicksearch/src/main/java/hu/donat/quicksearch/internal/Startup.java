/*
 * Copyright (c) 2016 the original author or authors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package hu.donat.quicksearch.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.progress.UIJob;

/**
 * @author Donát Csikós
 */
public class Startup implements IStartup {

    @Override
    public void earlyStartup() {
        // TODO add/remove listeners when windows and pages appear/disappear
        // do a shell traversal every time a view opened
        new StartupJob().schedule();
    }

    private static final class StartupJob extends UIJob {

        private StartupJob() {
            super("");
            setSystem(true);
        }

        @Override
        public IStatus runInUIThread(IProgressMonitor monitor) {
            QuickSearchManager.addQuickSearchToAllTrees(getDisplay());
            Listeners.addListeners();
            return Status.OK_STATUS;
        }
    }
}
