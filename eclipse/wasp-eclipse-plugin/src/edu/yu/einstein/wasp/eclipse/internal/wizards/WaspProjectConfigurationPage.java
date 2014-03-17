/**
 * 
 */
package edu.yu.einstein.wasp.eclipse.internal.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.yu.einstein.wasp.eclipse.internal.Messages;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author calder
 * 
 */
public class WaspProjectConfigurationPage extends WizardPage {

	Text projIName;
	Text projName;
	Text projNamespace;
	Text projDescription;
	Button defaultWorkspace;
	Button forms;
	Button resource;
	Button pipeline;
	Button viz;
	Label locLabel;
	Combo locCombo;

	private IPath loc;

	boolean initialized = false;
	private Map<String, List<Combo>> fieldsWithHistory;

	protected WaspProjectConfigurationPage(String pageIName,
			String pageDescription) {
		super(pageIName);
		setTitle(pageIName);
		setDescription(pageDescription);
		validate();
		fieldsWithHistory = new HashMap<String, List<Combo>>();
	}

	@Override
	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(3, false));

		// Project information group
		Group projGroup = new Group(container, SWT.NONE);
		projGroup.setText(Messages.WizardConfigurationPage_projectGroup);
		projGroup.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, true, 3,
				1));
		projGroup.setLayout(new GridLayout(2, false));

		Label projectIName = new Label(projGroup, SWT.NONE);
		projectIName.setText(Messages.WizardConfigurationPage_projectIName);
		projIName = new Text(projGroup, SWT.BORDER);
		projIName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		projIName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

		Label projectNamespace = new Label(projGroup, SWT.NONE);
		projectNamespace
				.setText(Messages.WizardConfigurationPage_projectNamespace);
		projNamespace = new Text(projGroup, SWT.BORDER);
		projNamespace.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
				false));
		projNamespace.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		
		Label projectName = new Label(projGroup, SWT.NONE);
		projectName.setText(Messages.WizardConfigurationPage_projectName);
		projName = new Text(projGroup, SWT.BORDER);
		projName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		projName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		
		Label projectDescription = new Label(projGroup, SWT.NONE);
		projectDescription
				.setText(Messages.WizardConfigurationPage_projectDescription);
		projDescription = new Text(projGroup, SWT.BORDER);
		projDescription.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		defaultWorkspace = new Button(container, SWT.CHECK);
		GridData useDefaultWorkspaceData = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 3, 1);
		defaultWorkspace.setLayoutData(useDefaultWorkspaceData);
		defaultWorkspace
				.setText(Messages.WizardConfigurationPage_defaultWorkspace);
		defaultWorkspace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean inWorkspace = inWorkspace();
				locLabel.setEnabled(!inWorkspace);
				locCombo.setEnabled(!inWorkspace);
			}

			private boolean inWorkspace() {
				return defaultWorkspace.getSelection();
			}
		});
		defaultWorkspace.setSelection(true);

		locLabel = new Label(container, SWT.NONE);
		GridData locLabelData = new GridData();
		locLabelData.horizontalIndent = 10;
		locLabel.setLayoutData(locLabelData);
		locLabel.setText(Messages.WizardConfigurationPage_location);
		locLabel.setEnabled(false);

		locCombo = new Combo(container, SWT.NONE);
		GridData locComboData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		locCombo.setLayoutData(locComboData);
		locCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		locCombo.setEnabled(false);
		addFieldWithHistory("location", locCombo); //$NON-NLS-1$

		Button locButton = new Button(container, SWT.NONE);
		GridData locButtonData = new GridData(SWT.FILL, SWT.CENTER, false,
				false);
		locButton.setLayoutData(locButtonData);
		locButton.setText(Messages.WizardConfigurationPage_locationButton);
		locButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				dialog.setText(Messages.WizardConfigurationPage_locationButtonMessage);
				String path = locCombo.getText();
				if (path.length() == 0) {
					path = ResourcesPlugin.getWorkspace().getRoot()
							.getLocation().toPortableString();
				}
				dialog.setFilterPath(path);

				String selectedDir = dialog.open();
				if (selectedDir != null) {
					locCombo.setText(selectedDir);
					defaultWorkspace.setSelection(false);
					validate();
				}
			}
		});

		if (loc != null && !Platform.getLocation().equals(loc)) {
			locCombo.setText(loc.toOSString());
		}

		// Feature information group
		Group featureGroup = new Group(container, SWT.NONE);
		featureGroup.setText(Messages.WizardConfigurationPage_featureGroup);
		GridData featureGD = new GridData(SWT.FILL, SWT.NONE, true, true, 3, 1);
		featureGroup.setLayoutData(featureGD);
		featureGroup.setLayout(new GridLayout(2, false));

		forms = new Button(featureGroup, SWT.CHECK);
		Label formsDesc = new Label(featureGroup, SWT.FILL);
		formsDesc.setText(Messages.WizardConfigurationPage_featureForms);

		resource = new Button(featureGroup, SWT.CHECK);
		Label resourceDesc = new Label(featureGroup, SWT.FILL);
		resourceDesc.setText(Messages.WizardConfigurationPage_featureResource);

		pipeline = new Button(featureGroup, SWT.CHECK);
		Label pipelineDesc = new Label(featureGroup, SWT.FILL);
		pipelineDesc.setText(Messages.WizardConfigurationPage_featurePipeline);
		
		viz = new Button(featureGroup, SWT.CHECK);
		Label vizDesc = new Label(featureGroup, SWT.FILL);
		vizDesc.setText(Messages.WizardConfigurationPage_featureVis);

		forms.setSelection(true);
		resource.setSelection(true);
		pipeline.setSelection(true);
		viz.setSelection(true);

		setControl(container);
	}

	protected void addFieldWithHistory(String id, Combo combo) {
		if (combo != null) {
			List<Combo> combos = fieldsWithHistory.get(id);
			if (combos == null) {
				combos = new ArrayList<Combo>();
				fieldsWithHistory.put(id, combos);
			}
			combos.add(combo);
		}
	}

	protected void validate() {
		if (!initialized) {
			return;
		}
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath projPath = getLocation();
		String loc = projPath.toOSString();

		if (projIName.getText().length() > 0) {
			if ( (!projIName.getText().matches("[a-zA-Z]+")) || javaKeywords.contains(projIName.getText().toLowerCase()) ) {
				setPageComplete(false);
				setErrorMessage(Messages.WizardConfigurationPage_enterValidProjIName);
				return;
			} else {
				setMessage(null);
				setErrorMessage(null);
			}
		} else {
			setPageComplete(false);
			setErrorMessage(null);
			setMessage(Messages.WizardConfigurationPage_enterProjIName);
			return;
		}
		
		if (projName.getText().length() == 0) {
			setPageComplete(false);
			setErrorMessage(null);
			setMessage(Messages.WizardConfigurationPage_enterProjName);
			return;
		}

		if (projNamespace.getText().length() > 0) {
			String[] elements = projNamespace.getText().split(".");
			boolean ok = true;
			if (projNamespace.getText().matches("\\.\\.")) {
				ok = false;
			} else {
				for (String e : elements) {
					if (javaKeywords.contains(e.toLowerCase())) {
						ok = false;
						break;
					}
					if (e.substring(0, 1).matches("[0-9]+")) {
						ok = false;
						break;
					}
				}
			}
			if (!projNamespace.getText().matches("[0-9a-z_\\.]+") || !ok ) {
				setPageComplete(false);
				setErrorMessage(Messages.WizardConfigurationPage_enterValidProjNamespace);
				return;
			} else {
				setMessage(null);
				setErrorMessage(null);
			}
		} else {
			setPageComplete(false);
			setErrorMessage(null);
			setMessage(Messages.WizardConfigurationPage_enterProjNamespace);
			return;
		}

		if (loc.length() == 0) {
			setPageComplete(false);
			setErrorMessage(null);
			setMessage(Messages.WizardConfigurationPage_enterLocation);
			return;
		}

		if (!Path.ROOT.isValidPath(loc)) {
			setPageComplete(false);
			setErrorMessage(Messages.WizardConfigurationPage_invalidLocation);
			return;
		}

		if (!defaultWorkspace.getSelection()) {
			IStatus locStatus = workspace.validateProjectLocation(workspace
					.getRoot().getProject(projIName.getText()), projPath.append(projIName.getText()));
			
			if (!locStatus.isOK()) {
				setErrorMessage(locStatus.getMessage());
				setPageComplete(false);
				return;
			}

		}

		setPageComplete(true);
		setErrorMessage(null);
		setMessage(null);
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible) {
			initialized = true;
			validate();
		}
	}
	
	public String getProjectIName() {
		return projIName.getText();
	}
	public String getProjectName() {
		return projName.getText();
	}
	public String getProjectNamespace() {
		return projNamespace.getText();
	}
	public String getProjectDescription() {
		return projDescription.getText();
	}
	public boolean isDefaultWorkspace() {
		return defaultWorkspace.getSelection();
	}
	public boolean createForms() {
		return forms.getSelection();
	}
	public boolean createResource() {
		return resource.getSelection();
	}
	public boolean createPipeline() {
		return pipeline.getSelection();
	}
	public boolean createViz() {
		return viz.getSelection();
	}
	public IPath getLocation() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return (IPath) (isDefaultWorkspace() ? root.getLocation() : new Path(locCombo.getText()));
	}
	
	private static final Set<String> javaKeywords = new HashSet<String>(Arrays.asList(new String[] {
			"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue",
			"default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto",
			"if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package",
			"private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized",
			"this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while"
	}));

}
