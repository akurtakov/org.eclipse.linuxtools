/*******************************************************************************
 * Copyright (c) 2005, 2018 Red Hat, Inc.
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.linuxtools.internal.rpm.core.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.linuxtools.rpm.core.IProjectConfiguration;
import org.eclipse.linuxtools.rpm.core.IRPMConstants;
import org.eclipse.linuxtools.rpm.core.utils.Utils;

/**
 * A utility class for executing RPM commands.
 *
 */
public class RPM {

    private List<String> macroDefines;

    /**
     * Constructs a new RPM object.
     *
     * @param config
     *            the RPM configuration to use
     */
    public RPM(IProjectConfiguration config) {
		IEclipsePreferences node = DefaultScope.INSTANCE.getNode(IRPMConstants.RPM_CORE_ID);
		String rpmCmd = node.get(IRPMConstants.RPM_CMD, ""); //$NON-NLS-1$
		macroDefines = new ArrayList<>();

		macroDefines.add(rpmCmd);
		macroDefines.add("-v"); //$NON-NLS-1$
		macroDefines.addAll(config.getConfigDefines());
    }

    /**
     * Installs a given source RPM
     *
     * @param sourceRPM
     *            The src.rpm file to install.
     * @return The output of the install command.
     * @throws CoreException
     *             If something fails.
     */
    public String install(IFile sourceRPM) throws CoreException {
		List<String> command = new ArrayList<>();
		command.addAll(macroDefines);
		command.add("-i"); //$NON-NLS-1$
		command.add(sourceRPM.getLocation().toOSString());
		try {
			return Utils.runCommandToString(command.toArray(new String[command.size()]));
		} catch (IOException e) {
			throw new CoreException(Status.error(e.getMessage(), e));
		}
    }
}
