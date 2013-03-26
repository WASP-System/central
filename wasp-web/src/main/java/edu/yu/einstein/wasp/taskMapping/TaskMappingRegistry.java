package edu.yu.einstein.wasp.taskMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.yu.einstein.wasp.exception.WaspException;


/**
 * Registry for storing and retrieving task-mapping bean references
 * @author asmclellan
 *
 */
public class TaskMappingRegistry {

	private Map<String, WaspTaskMapping> taskMappings;
	
	/**
	 * Constructor
	 */
	public  TaskMappingRegistry(){
		taskMappings = new HashMap<String, WaspTaskMapping>();
	}
	
	/**
	 * Add a message edu.yu.einstein.wasp.taskMapping to the registry
	 * @param edu.yu.einstein.wasp.taskMapping
	 * @param name
	 * @throws WaspException
	 */
	public void add(WaspTaskMapping taskMapping, String name) throws WaspException{
		if (taskMappings.containsKey(name))
			throw new WaspException("Taskmapping with name '"+name+"' already in the registry");
		taskMappings.put(name, taskMapping);
	}
	
	/**
	 * Remove a named edu.yu.einstein.wasp.taskMapping from the registry
	 * @param name
	 * @throws WaspException
	 */
	public void remove(String name) throws WaspException{
		if (taskMappings.containsKey(name))
			taskMappings.remove(name);
		else throw new WaspException("Cannot find edu.yu.einstein.wasp.taskMapping with name '"+name+"' in the registry");
	}
	
	/**
	 * gets a named edu.yu.einstein.wasp.taskMapping from the registry or returns null if there are no matches
	 * to name or the object obtained cannot be cast to the specified type
	 * @param name
	 * @return
	 */
	public WaspTaskMapping getTaskMapping(String name){
		return taskMappings.get(name);
	}
	
	public Set<String> getNames(){
		return taskMappings.keySet();
	}
	
	
}
