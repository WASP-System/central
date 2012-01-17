package org.springframework.web.servlet.view.tiles2;

import javax.servlet.ServletContext;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.servlet.wildcard.WildcardServletTilesApplicationContext;
import org.apache.tiles.startup.AbstractTilesInitializer;

public class WaspTilesInitializer extends AbstractTilesInitializer {

		@Override
		protected TilesApplicationContext createTilesApplicationContext(TilesApplicationContext preliminaryContext) {
			return new WildcardServletTilesApplicationContext((ServletContext) preliminaryContext.getContext());
		}
		
		/** {@inheritDoc} */
	    @Override
	    protected AbstractTilesContainerFactory createContainerFactory(
	            TilesApplicationContext context) {
	        return new BasicTilesContainerFactory();
	    }

	}