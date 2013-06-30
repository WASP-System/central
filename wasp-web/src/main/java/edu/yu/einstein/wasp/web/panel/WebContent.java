package edu.yu.einstein.wasp.web.panel;


import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author asmclellan
 *
 */
public class WebContent {
	
	private String htmlContent = "";
	
	private Set<URL> scriptDependencies = new HashSet<URL>();
	
	private Set<URL> cssDependencies = new HashSet<URL>();

	public WebContent() {}
	
	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public Set<URL> getScriptDependencies() {
		return scriptDependencies;
	}

	public void setScriptDependencies(Set<URL> scriptDependencies) {
		this.scriptDependencies = scriptDependencies;
	}

	public Set<URL> getCssDependencies() {
		return cssDependencies;
	}

	public void setCssDependencies(Set<URL> cssDependencies) {
		this.cssDependencies = cssDependencies;
	}

	

}
