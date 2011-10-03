package org.springframework.web.servlet.view.tiles2;

import org.springframework.web.servlet.view.UrlBasedViewResolver;


public class WaspTilesViewResolver extends UrlBasedViewResolver {

	public WaspTilesViewResolver() {
		setViewClass(requiredViewClass());
	}

	/**
	 * Requires {@link TilesView}.
	 */
	@Override
	protected Class requiredViewClass() {
		return WaspTilesView.class;
	}

}
