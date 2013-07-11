/**
 * 
 */
package edu.yu.einstein.wasp.plugin;

import java.util.Set;

/**
 * @author asmclellan
 *
 */
public interface ViewPanelProviding extends WebInterfacing {
	
	public static enum Status{ COMPLETED, STARTED, PENDING, FAILED, UNKNOWN }
	
	public Status getStatus(Object handle);
	
	public Set<? extends ViewPanel> getViewPanels(Object handle);

	
}
