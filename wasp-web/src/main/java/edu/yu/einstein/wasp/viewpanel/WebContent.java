package edu.yu.einstein.wasp.viewpanel;


import java.net.URI;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.yu.einstein.wasp.viewpanel.Content;

/**
 * 
 * @author asmclellan
 *
 */
public class WebContent extends Content {
	
	private String html;
	
	private String script;
	
	private Set<URL> scriptDependencies;
	
	private Set<URL> cssDependencies;

	public WebContent() {
		scriptDependencies = new LinkedHashSet<URL>();
		cssDependencies = new LinkedHashSet<URL>();
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

	public Set<URL> getScriptDependencies() {
		return scriptDependencies;
	}

	public void setScriptDependencies(Set<URL> set) {
		this.scriptDependencies = set;
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
