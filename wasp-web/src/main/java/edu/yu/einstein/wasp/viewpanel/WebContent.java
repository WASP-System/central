package edu.yu.einstein.wasp.viewpanel;


import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * @author asmclellan
 *
 */
public class WebContent extends Content {
	
	private String html;
	
	private String script;
	
	private Set<URI> scriptDependencies;
	
	private Set<URI> cssDependencies;

	public WebContent() {
		scriptDependencies = new LinkedHashSet<URI>();
		cssDependencies = new LinkedHashSet<URI>();
		html = "";
		script = "";
	}
	
	public String getHtmlCode() {
		return this.html;
	}

	public void setHtmlCode(String htmlCode) {
		this.html = htmlCode;
	}
	
	public String getScriptCode() {
		return this.script;
	}

	public void setScriptCode(String scriptCode) {
		this.script = scriptCode;
	}

	public Set<URI> getScriptDependencies() {
		return scriptDependencies;
	}

	public void setScriptDependencies(Set<URI> scriptDependencies) {
		this.scriptDependencies = scriptDependencies;
	}
	
	public void addScriptDependency(URI dependency){
		this.scriptDependencies.add(dependency);
	}

	public Set<URI> getCssDependencies() {
		return cssDependencies;
	}

	public void setCssDependencies(Set<URI> cssDependencies) {
		this.cssDependencies = cssDependencies;
	}
	
	public void addCssDependency(URI dependency){
		this.cssDependencies.add(dependency);
	}

	

}
