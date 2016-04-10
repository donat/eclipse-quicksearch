/*
 * Copyright (c) 2016 the original author or authors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package hu.donat.quicksearch.internal;

import org.osgi.framework.BundleContext;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author Donát Csikós
 */
public class QuicksearchPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "hu.donat.quicksearch"; //$NON-NLS-1$
    private static QuicksearchPlugin plugin;

    public QuicksearchPlugin() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
    }

    public static QuicksearchPlugin getDefault() {
        return plugin;
    }

}
