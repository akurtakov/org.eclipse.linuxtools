/*******************************************************************************
 * Copyright (c) 2009, 2018 Red Hat, Inc.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Elliott Baron <ebaron@redhat.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.linuxtools.internal.valgrind.memcheck.tests;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.linuxtools.internal.valgrind.ui.ValgrindUIPlugin;
import org.eclipse.linuxtools.internal.valgrind.ui.ValgrindViewPart;
import org.eclipse.linuxtools.valgrind.core.IValgrindMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SignalTest extends AbstractMemcheckTest {

    @Before
    public void prep() throws Exception {
        proj = createProjectAndBuild("segvtest"); //$NON-NLS-1$
    }

    @Override
    @After
    public void tearDown() throws CoreException {
        deleteProject(proj);
        super.tearDown();
    }

    @Test
    public void testSegfaultHandle() throws Exception {
        ILaunchConfiguration config = createConfiguration(proj.getProject());
        doLaunch(config, "testSegfault"); //$NON-NLS-1$

        ValgrindViewPart view = ValgrindUIPlugin.getDefault().getView();
        IValgrindMessage[] messages = view.getMessages();
        assertTrue(messages.length > 0);
        assertTrue(messages[0].getText().contains("SIGSEGV")); //$NON-NLS-1$
    }
}
