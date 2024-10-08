/*******************************************************************************
 * Copyright (c) 2016, 2018 Red Hat.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - Initial Contribution
 *******************************************************************************/

package org.eclipse.linuxtools.internal.docker.ui.testutils.swt;

import static org.junit.Assert.fail;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.junit.rules.ExternalResource;

/**
 *
 */
public class SWTBotViewRule extends ExternalResource {

	private final SWTWorkbenchBot bot = new SWTWorkbenchBot();

	private final String viewId;

	private SWTBotView botView = null;

	private IViewPart view = null;

	public SWTBotViewRule(final String viewId) {
		this.viewId = viewId;
	}

	@Override
	protected void before() {
		bot.getDisplay().asyncExec(() -> {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(this.viewId);
			} catch (Exception e) {
				e.printStackTrace();
				fail("Failed to open view with id '" + this.viewId + "': " + e.getMessage());
			}
		});
		this.botView = this.bot.viewById(this.viewId);
		this.botView.show();
		this.view = this.botView.getViewReference().getView(true);
	}

	public SWTBotView bot() {
		return this.botView;
	}

	@SuppressWarnings("unchecked")
	public <T> T view() {
		return (T) view;
	}

	public void close() {
		this.botView.close();
	}
}
