/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package ___package___.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ___package___.service.___PluginIName___Service;

import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

@Service
@Transactional("entityManager")
public class ___PluginIName___ServiceImpl extends WaspServiceImpl implements ___PluginIName___Service {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}


}
