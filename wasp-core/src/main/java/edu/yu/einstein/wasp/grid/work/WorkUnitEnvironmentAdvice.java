/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;

/**
 * @author calder
 *
 */
@Aspect
public class WorkUnitEnvironmentAdvice {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static enum WorkUnitEnvironmentVariable {
		FILE, VALUE
	}
	
	private String key;
	private String name;
	private WorkUnitEnvironmentVariable type = WorkUnitEnvironmentVariable.VALUE;

	/**
	 * 
	 */
	public WorkUnitEnvironmentAdvice() {
		// proxy
	}
	
	/**
	 * 
	 * @param key Host specific value setting key
	 * @param name name of the environment variable
	 */
	public WorkUnitEnvironmentAdvice(String key, String name, WorkUnitEnvironmentVariable type) {
		this.key = key;
		this.name = name;
		this.type = type;
	}
	
	@Before("execution(* *..GridWorkService+.execute(..)) && args(workunit)")
	public void resolveEnvironmentVariables(WorkUnit workunit) {
		logger.debug("advising environment variables on work unit " + workunit.getId());
		if (name==null || key==null) return;
		GridWorkService gws = (GridWorkService) AopContext.currentProxy();
		String value = gws.getTransportConnection().getConfiguredSetting(key);
		logger.debug("key=" + key);
		logger.debug("name=" + name);
		logger.debug("type=" + type);
		logger.debug("value=" + value);
		if (type == WorkUnitEnvironmentVariable.FILE) {
			value = gws.getTransportConnection().prefixRemoteFile(value);
		}
		logger.debug("setting environment variable in work unit: " + name + "=" + value);
		workunit.putEnvironmentVariable(name, value);	
	}

}
