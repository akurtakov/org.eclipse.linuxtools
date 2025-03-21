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
package org.eclipse.linuxtools.internal.docker.ui.launch;

import static org.eclipse.linuxtools.internal.docker.ui.launch.IRunDockerImageLaunchConfigurationConstants.MEMORY_LIMIT;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.linuxtools.docker.ui.Activator;
import org.eclipse.linuxtools.internal.docker.ui.SWTImagesFactory;
import org.eclipse.linuxtools.internal.docker.ui.wizards.ImageRunResourceVolumesVariablesModel;
import org.eclipse.linuxtools.internal.docker.ui.wizards.WizardMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

public class RunImageResourcesTab extends AbstractLaunchConfigurationTab {

	private static final String TAB_NAME = "RunResourcesTab.name"; //$NON-NLS-1$

	private static final int COLUMNS = 2;

	private final DataBindingContext dbc = new DataBindingContext();
	private ImageRunResourceVolumesVariablesModel model = null;

	private Composite container;

	public RunImageResourcesTab(ImageRunResourceVolumesVariablesModel model) {
		this.model = model;
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).span(1, 1)
				.grab(true, false).applyTo(container);
		GridLayoutFactory.fillDefaults().numColumns(COLUMNS).margins(6, 6)
				.applyTo(container);
		if (model == null) {
			setErrorMessage(LaunchMessages.getString("NoConnectionError.msg"));
		} else {
			setErrorMessage(null);
			createResourceSettingsContainer(container);
		}
		setControl(container);
	}

	private void createResourceSettingsContainer(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL)
				.span(COLUMNS, 1).grab(true, false).applyTo(container);
		GridLayoutFactory.fillDefaults().spacing(10, 2).applyTo(container);
		final Button enableResourceLimitationButton = new Button(container,
				SWT.CHECK);
		enableResourceLimitationButton.setText(WizardMessages.getString(
				"ImageRunResourceVolVarPage.enableLimitationButton")); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.applyTo(enableResourceLimitationButton);
		dbc.bindValue(
				WidgetProperties.widgetSelection()
						.observe(enableResourceLimitationButton),
				BeanProperties
						.value(ImageRunResourceVolumesVariablesModel.class,
								ImageRunResourceVolumesVariablesModel.ENABLE_RESOURCE_LIMITATIONS)
						.observe(model));

		final int COLUMNS = 5;
		final int INDENT = 20;
		final Composite subContainer = new Composite(container, SWT.NONE);
		setContainer(subContainer);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL)
				.indent(INDENT, 0).span(COLUMNS, 1).grab(true, false)
				.applyTo(subContainer);
		GridLayoutFactory.fillDefaults().numColumns(COLUMNS).margins(6, 6)
				.spacing(10, 2).applyTo(subContainer);

		// specify CPU limitation
		final Label cpuPriorityLabel = new Label(subContainer, SWT.NONE);
		cpuPriorityLabel.setText(WizardMessages
				.getString("ImageRunResourceVolVarPage.cpuPriorityLabel")); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.grab(false, false).applyTo(cpuPriorityLabel);
		final Button lowCPULimitationButton = new Button(subContainer,
				SWT.RADIO);
		lowCPULimitationButton.setText(WizardMessages
				.getString("ImageRunResourceVolVarPage.lowButton")); //$NON-NLS-1$
		bindButton(lowCPULimitationButton,
				ImageRunResourceVolumesVariablesModel.CPU_LOW);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.applyTo(lowCPULimitationButton);
		final Button mediumCPULimitationButton = new Button(subContainer,
				SWT.RADIO);
		mediumCPULimitationButton.setText(WizardMessages
				.getString("ImageRunResourceVolVarPage.mediumButton")); //$NON-NLS-1$
		bindButton(mediumCPULimitationButton,
				ImageRunResourceVolumesVariablesModel.CPU_MEDIUM);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.applyTo(mediumCPULimitationButton);
		final Button highCPULimitationButton = new Button(subContainer,
				SWT.RADIO);
		highCPULimitationButton.setText(WizardMessages
				.getString("ImageRunResourceVolVarPage.highButton")); //$NON-NLS-1$
		bindButton(highCPULimitationButton,
				ImageRunResourceVolumesVariablesModel.CPU_HIGH);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).span(2, 1)
				.applyTo(highCPULimitationButton);

		// Memory limitation
		final Label memoryLimitLabel = new Label(subContainer, SWT.NONE);
		memoryLimitLabel.setText(WizardMessages
				.getString("ImageRunResourceVolVarPage.memoryLimit")); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.grab(false, false).applyTo(memoryLimitLabel);
		final Scale memoryLimitSpinner = new Scale(subContainer, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.grab(false, false).span(2, 1).applyTo(memoryLimitSpinner);
		memoryLimitSpinner.setBackground(
				Display.getDefault().getSystemColor(SWT.COLOR_TRANSPARENT));
		memoryLimitSpinner.setMinimum(0);
		memoryLimitSpinner.setMaximum(this.model.getTotalMemory());
		memoryLimitSpinner.setPageIncrement(64);
		dbc.bindValue(WidgetProperties.widgetSelection().observe(memoryLimitSpinner),
				BeanProperties
						.value(ImageRunResourceVolumesVariablesModel.class,
								ImageRunResourceVolumesVariablesModel.MEMORY_LIMIT)
						.observe(model));

		final Text memoryLimitValueText = new Text(subContainer, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.grab(false, false).hint(50, SWT.DEFAULT)
				.applyTo(memoryLimitValueText);
		dbc.bindValue(
				WidgetProperties.text(SWT.Modify).observe(memoryLimitValueText),
				BeanProperties
						.value(ImageRunResourceVolumesVariablesModel.class,
								ImageRunResourceVolumesVariablesModel.MEMORY_LIMIT)
						.observe(model));
		dbc.bindValue(WidgetProperties.widgetSelection().observe(memoryLimitSpinner),
				BeanProperties
						.value(ImageRunResourceVolumesVariablesModel.class,
								ImageRunResourceVolumesVariablesModel.MEMORY_LIMIT)
						.observe(model));
		final Label memoryLimitValueLabel = new Label(subContainer, SWT.NONE);
		memoryLimitValueLabel.setText("MB"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
				.grab(false, false).applyTo(memoryLimitValueLabel);

		// enable/disable controls
		final IObservableValue<?> enableResourceLimitationsObservable = BeanProperties
				.value(ImageRunResourceVolumesVariablesModel.class,
						ImageRunResourceVolumesVariablesModel.ENABLE_RESOURCE_LIMITATIONS)
				.observe(model);
		dbc.bindValue(
				WidgetProperties.widgetSelection()
						.observe(enableResourceLimitationButton),
				enableResourceLimitationsObservable);
		enableResourceLimitationsObservable
				.addChangeListener(onEnableResourceLimitation(subContainer));
		toggleResourceLimitationControls(subContainer);

	}

	private void setContainer(final Composite container) {
		this.container = container;
	}

	private Composite getContainer() {
		return container;
	}

	/**
	 * Binds the given <code>cpuShares</code> value to the given {@link Button}
	 * when it is selected.
	 *
	 * @param button
	 *            the {@link Button} to bind
	 * @param cpuShares
	 *            the <code>cpuShares</code> to bind to the {@link Button}
	 * @return
	 */
	private Binding bindButton(final Button button, final long cpuShares) {
		return dbc.bindValue(WidgetProperties.widgetSelection().observe(button),
				BeanProperties
						.value(ImageRunResourceVolumesVariablesModel.class,
								ImageRunResourceVolumesVariablesModel.CPU_SHARE_WEIGHT)
						.observe(model),
				new UpdateValueStrategy<>() {
					@Override
					public Object convert(Object value) {
						if (value.equals(Boolean.TRUE)) {
							return cpuShares;
						}
						return 0l;
					}

				}, new UpdateValueStrategy<>() {
					@Override
					public Object convert(final Object value) {
						return value.equals(cpuShares);
					}
				});
	}

	private IChangeListener onEnableResourceLimitation(
			final Composite container) {
		return event -> toggleResourceLimitationControls(container);
	}

	private void toggleResourceLimitationControls(Composite container) {
		for (Control childControl : container.getChildren()) {
			if (model.isEnableResourceLimitations()) {
				childControl.setEnabled(true);
			} else {
				childControl.setEnabled(false);
			}
			updateLaunchConfigurationDialog();
		}
	}


	@Override
	public Image getImage() {
		return SWTImagesFactory.get(SWTImagesFactory.IMG_RESOURCE);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		if (model == null)
			return;
		try {
			boolean enableLimits = configuration.getAttribute(
					IRunDockerImageLaunchConfigurationConstants.ENABLE_LIMITS,
					false);
			model.setEnableResourceLimitations(enableLimits);
			long cpuShareWeight = Long.parseLong(configuration.getAttribute(
					IRunDockerImageLaunchConfigurationConstants.CPU_PRIORITY,
					Long.toString(
							ImageRunResourceVolumesVariablesModel.CPU_MEDIUM)));
			model.setCpuShareWeight(cpuShareWeight);

			int maxMemory = this.model.getTotalMemory();
			// retrieve memory limit stored in MB
			final long memoryLimit = Long
					.parseLong(configuration.getAttribute(MEMORY_LIMIT,
							Long.toString(
							ImageRunResourceVolumesVariablesModel.DEFAULT_MEMORY)));
			// make sure memory limit is not higher than maxMemory
			model.setMemoryLimit(Math.min(maxMemory, memoryLimit));
			toggleResourceLimitationControls(getContainer());
		} catch (CoreException e) {
			Activator.logErrorMessage(
					LaunchMessages.getString(
							"RunDockerImageLaunchConfiguration.load.failure"), //$NON-NLS-1$
					e);
		}
		model.addPropertyChangeListener(
				new LaunchConfigurationChangeListener());
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		if (model == null)
			return;
		configuration.setAttribute(
				IRunDockerImageLaunchConfigurationConstants.ENABLE_LIMITS,
				model.isEnableResourceLimitations());
		configuration.setAttribute(
				IRunDockerImageLaunchConfigurationConstants.MEMORY_LIMIT,
				Long.toString(model.getMemoryLimit()));
		configuration.setAttribute(
				IRunDockerImageLaunchConfigurationConstants.CPU_PRIORITY,
				Long.toString(model.getCpuShareWeight()));
	}

	@Override
	public String getName() {
		return LaunchMessages.getString(TAB_NAME);
	}

	private class LaunchConfigurationChangeListener
			implements PropertyChangeListener {

		@Override
		public void propertyChange(final PropertyChangeEvent evt) {
			updateLaunchConfigurationDialog();
		}
	}

}
