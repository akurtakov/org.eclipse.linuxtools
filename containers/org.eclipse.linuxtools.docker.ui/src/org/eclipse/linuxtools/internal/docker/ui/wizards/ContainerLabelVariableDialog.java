/*******************************************************************************
 * Copyright (c) 2015, 2025 Red Hat Inc. and others.
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

package org.eclipse.linuxtools.internal.docker.ui.wizards;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ContainerLabelVariableDialog extends Dialog {

	private final LabelVariableModel model;

	private final DataBindingContext dbc = new DataBindingContext();

	public ContainerLabelVariableDialog(final Shell parentShell) {
		super(parentShell);
		this.model = new LabelVariableModel();
	}

	public ContainerLabelVariableDialog(final Shell parentShell,
			final LabelVariableModel selectedVariable) {
		super(parentShell);
		this.model = new LabelVariableModel(selectedVariable);
	}

	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		shell.setText(WizardMessages
				.getString("ContainerLabelVariableDialog.title")); //$NON-NLS-1$
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, super.getInitialSize().y);
	}

	/**
	 * Disable the 'OK' button by default
	 */
	@Override
	protected Button createButton(Composite parent, int id, String label,
			boolean defaultButton) {
		final Button button = super.createButton(parent, id, label,
				defaultButton);
		if (id == IDialogConstants.OK_ID) {
			button.setEnabled(false);
		}
		return button;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final int COLUMNS = 2;
		final Composite container = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL)
				.span(COLUMNS, 1).grab(true, true).applyTo(container);
		GridLayoutFactory.fillDefaults().numColumns(COLUMNS).margins(10, 10)
				.applyTo(container);
		final Label explanationLabel = new Label(container, SWT.NONE);
		explanationLabel.setText(WizardMessages.getString(
				"ContainerLabelVariableDialog.explanationLabel")); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.span(COLUMNS, 1).grab(false, false).applyTo(explanationLabel);
		final Label variableNameLabel = new Label(container, SWT.NONE);
		variableNameLabel.setText(WizardMessages
				.getString("ContainerLabelVariableDialog.nameLabel")); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.grab(false, false).applyTo(variableNameLabel);
		final Text variableNameText = new Text(container, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.grab(true, false).applyTo(variableNameText);
		final Label variableValueLabel = new Label(container, SWT.NONE);
		variableValueLabel.setText(WizardMessages
				.getString("ContainerLabelVariableDialog.valueLabel")); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.grab(false, false).applyTo(variableValueLabel);
		final Text variableValueText = new Text(container, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.grab(true, false).applyTo(variableValueText);
		// error message
		final Label errorMessageLabel = new Label(container, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.span(COLUMNS, 1).grab(true, false).applyTo(errorMessageLabel);

		// listening to changes
		final ISWTObservableValue<?> variableNameObservable = WidgetProperties
				.text(SWT.Modify).observe(variableNameText);
		dbc.bindValue(variableNameObservable,
				BeanProperties.value(LabelVariableModel.class,
						LabelVariableModel.NAME).observe(model));
		final ISWTObservableValue<?> variableValueObservable = WidgetProperties
				.text(SWT.Modify).observe(variableValueText);
		dbc.bindValue(variableValueObservable,
				BeanProperties.value(LabelVariableModel.class,
						LabelVariableModel.VALUE).observe(model));

		variableNameObservable
				.addValueChangeListener(this::validateInput);
		variableValueObservable
				.addValueChangeListener(this::validateInput);
		return container;
	}

	private void validateInput(@SuppressWarnings("unused") ValueChangeEvent<?> event) {
		final String variableName = model.getName();
		if (variableName == null || variableName.isEmpty()) {
		} else {
			setOkButtonEnabled(true);
		}
	}

	private void setOkButtonEnabled(final boolean enabled) {
		getButton(IDialogConstants.OK_ID).setEnabled(enabled);
	}

	public LabelVariableModel getLabelVariable() {
		return new LabelVariableModel(model.getName(), model.getValue());
	}

}
