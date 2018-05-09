/*******************************************************************************
 * Copyright (c) 2008, 2018 Phil Muldoon and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Phil Muldoon <pkmuldoon@picobot.org> - initial API.
 *    Red Hat - modifications for use with Valgrind plugins.
 *******************************************************************************/
package org.eclipse.linuxtools.internal.valgrind.ui.editor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;

public class SuppressionsElementScanner extends BufferedRuleBasedScanner {

	public static final String MEMCHECK = "Memcheck"; //$NON-NLS-1$
	public static final String[] MEMCHECK_SUPP_TYPES = new String[] { "Value0", "Value1", "Value2", "Value4", "Value8", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"Value16", //$NON-NLS-1$
			"Cond", //$NON-NLS-1$
			"Addr1", "Addr2", "Addr4", "Addr8", "Addr16", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"Jump", //$NON-NLS-1$
			"Param", //$NON-NLS-1$
			"Free", //$NON-NLS-1$
			"Overlap", //$NON-NLS-1$
			"Leak" //$NON-NLS-1$
	};
	public static final String[] CONTEXTS = new String[] { "obj", "fun" //$NON-NLS-1$ //$NON-NLS-2$
	};

	public SuppressionsElementScanner() {
		ColorRegistry colorRegistry = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getColorRegistry();
		String[] tools = { MEMCHECK };
		Map<String, List<String>> kinds = new HashMap<>();
		kinds.put(MEMCHECK, Arrays.asList(MEMCHECK_SUPP_TYPES));

		IToken defaultToken = new Token(new TextAttribute(colorRegistry.get(ISuppressionsColorConstants.DEFAULT)));
		IToken toolToken = new Token(
				new TextAttribute(colorRegistry.get(ISuppressionsColorConstants.TOOL), null, SWT.BOLD));
		IToken suppKindToken = new Token(new TextAttribute(colorRegistry.get(ISuppressionsColorConstants.SUPP_TYPE)));
		IToken contextToken = new Token(
				new TextAttribute(colorRegistry.get(ISuppressionsColorConstants.CONTEXT), null, SWT.BOLD));
		IToken commentToken = new Token(new TextAttribute(colorRegistry.get(ISuppressionsColorConstants.COMMENT)));

		setDefaultReturnToken(defaultToken);
		setRules(new IRule[] { new EndOfLineRule("#", commentToken), //$NON-NLS-1$
				new SuppressionToolRule(tools, toolToken), new SuppressionKindRule(kinds, suppKindToken),
				new SuppressionToolRule(CONTEXTS, contextToken), new WhitespaceRule(c -> Character.isWhitespace(c)) });
	}
}
