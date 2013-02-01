/**
 * 
 */
package edu.yu.einstein.wasp.grid;

import edu.yu.einstein.wasp.grid.work.WorkUnit;

/**
 * Base class for {@link GridHostResolver}s.  This class provides the mechansim for setting how a host resolver
 * handles the case when it can not determine where to send the {@link WorkUnit}.  
 * 
 * @author calder
 *
 */
public abstract class AbstractGridHostResolver implements GridHostResolver {
	
	//private UnresolvableStrategy unresolvableStrategy = UnresolvableStrategy.DEFAULT; 
	
	/**
	 * (@link GridHostResolver}s need to be able to choose how to resolve when a host can
	 * not be determined. This enum provides a mechanism for defining the strategy to take. 
	 * 
	 * DEFAULT - always use the default host (first in the list see {@link WorkUnit})
	 * THROW_UNRESOLVABLE_HOST - throw an exception, let the batch management handle
	 * 
	 * @author calder
	 *
	 */
	public enum UnresolvableStrategy {
		DEFAULT, THROW_UNRESOLVABLE_HOST;
	}

}
