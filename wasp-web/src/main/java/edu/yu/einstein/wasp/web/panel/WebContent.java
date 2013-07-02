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
	
	private String htmlContent;
	
	private Set<URL> scriptDependencies;
	
	private Set<URL> cssDependencies;

	public WebContent() {
		scriptDependencies = new HashSet<URL>();
		cssDependencies = new HashSet<URL>();
		htmlContent = "";
	}
	
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
	
	public void addScriptDependency(URL dependency){
		this.scriptDependencies.add(dependency);
	}

	public Set<URL> getCssDependencies() {
		return cssDependencies;
	}

	public void setCssDependencies(Set<URL> cssDependencies) {
		this.cssDependencies = cssDependencies;
	}
	
	public void addCssDependency(URL dependency){
		this.cssDependencies.add(dependency);
	}

	

}
