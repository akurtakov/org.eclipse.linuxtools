/*******************************************************************************
 * Copyright (c) 2013, 2018 Red Hat and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.linuxtools.systemtap.graphing.ui.widgets;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * A convenience class for showing error dialogs which display the full stack
 * trace in the details section.
 * @since 2.0
 */
public class ExceptionErrorDialog {

    public static void openError(final String message, final Exception e) {
        openError(message, message, e);
    }

    public static void openError(final String title, final String message, final Exception e) {
        Display.getDefault().asyncExec(() -> {
		    StringWriter writer = new StringWriter();
		    e.printStackTrace(new PrintWriter(writer));
			IStatus status = Status.error(e.toString(), new Throwable(writer.toString()));
		    ErrorDialog.openError(PlatformUI.getWorkbench()
		            .getActiveWorkbenchWindow().getShell(),
		            title, message, status);
		});
    }

}
