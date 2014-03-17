package edu.yu.einstein.wasp.eclipse.internal;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "edu.yu.einstein.wasp.eclipse.internal.messages"; //$NON-NLS-1$

	public static String WizardConfigurationPage_defaultWorkspace;
	public static String WizardConfigurationPage_pageName;
	public static String WizardConfigurationPage_pageDescription;
	public static String WizardConfigurationPage_location;
	public static String WizardConfigurationPage_locationButton;
	public static String WizardConfigurationPage_locationButtonMessage;
	public static String WizardConfigurationPage_projectIName;
	public static String WizardConfigurationPage_projectName;
	public static String WizardConfigurationPage_projectNamespace;
	public static String WizardConfigurationPage_projectDescription;
	public static String WizardConfigurationPage_projectGroup;
	public static String WizardConfigurationPage_featureGroup;
	public static String WizardConfigurationPage_featureForms;
	public static String WizardConfigurationPage_featureResource;
	public static String WizardConfigurationPage_featurePipeline;
	public static String WizardConfigurationPage_featureVis;
	public static String WizardConfigurationPage_enterLocation;
	public static String WizardConfigurationPage_invalidLocation;
	public static String WizardConfigurationPage_enterProjIName;
	public static String WizardConfigurationPage_enterValidProjIName;
	public static String WizardConfigurationPage_enterProjName;
	public static String WizardConfigurationPage_enterProjNamespace;
	public static String WizardConfigurationPage_enterValidProjNamespace;
	public static String WizardFailed;
	public static String WizardFailed_projectOrPomFileAlreadyExists;
	public static String WizardFailed_errorCreatingProject;
	public static String Project_webFormControllerSource;
	public static String Project_prefix;
	public static String Project_defaultVersion;
	public static String Project_waspRepositoryID;
	public static String Project_waspRepositoryName;
	public static String Project_waspRepositoryURL;
	public static String Project_waspArtifactID;
	public static String Project_waspGroupID;
	public static String Project_waspVersion;
	public static String Project_waspType;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}

}
