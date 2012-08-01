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
public abstract class WaspResourceLoader extends WaspLoader {

	protected List<String> dependencies;

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}

}
