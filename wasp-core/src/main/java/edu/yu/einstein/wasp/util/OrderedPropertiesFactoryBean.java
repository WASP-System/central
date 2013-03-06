/**
 * 
 */
package edu.yu.einstein.wasp.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.util.CollectionUtils;

/**
 * Hacked PropertiesFactoryBean implementation that returns an ordered key set.
 * 
 * @author calder
 *
 */
public class OrderedPropertiesFactoryBean extends PropertiesFactoryBean {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/* (non-Javadoc)
	 * @see org.springframework.core.io.support.PropertiesLoaderSupport#mergeProperties()
	 */
	@Override
	protected Properties mergeProperties() throws IOException {
		
		// this is spring code, modified to use linked properteies
		
		Properties result = new LinkedProperties();
		if (this.localOverride) {
		        // Load properties from file upfront, to let local properties override.
		        loadProperties(result);
		}
		if (this.localProperties != null) {
		        for (Properties localProp : this.localProperties) {
		                CollectionUtils.mergePropertiesIntoMap(localProp, result);
		        }
		}
		if (!this.localOverride) {
		        // Load properties from file afterwards, to let those properties override.
		        loadProperties(result);
		}
		return result;

	}

	private class LinkedProperties extends Properties {

	    private final LinkedHashSet<Object> ordered = new LinkedHashSet<Object>();

	    /* (non-Javadoc)
		 * @see java.util.Hashtable#keySet()
		 */
		@Override
		public Set<Object> keySet() {
			return ordered;
		}

		/* (non-Javadoc)
		 * @see java.util.Hashtable#keys()
		 */
		public Enumeration<Object> keys() {
	        return Collections.<Object>enumeration(ordered);
	    }

	    /* (non-Javadoc)
	     * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object)
	     */
	    public Object put(Object key, Object value) {
	        ordered.add(key);
	        return super.put(key, value);
	    }
	}

}
