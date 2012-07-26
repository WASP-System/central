/**
 * 
 */
package edu.yu.einstein.wasp.load;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author calder
 * 
 */
public abstract class WaspResourceLoader extends WaspLoader implements InitializingBean {

	protected List<String> dependencies;

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}

}
