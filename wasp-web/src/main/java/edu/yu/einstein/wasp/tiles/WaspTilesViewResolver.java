package edu.yu.einstein.wasp.tiles;

/**
 * Apache Tiles interceptor to make tile definition name available via request attributes
 * 
 *  @author Sasha Levchuk
 */


import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesView;

public class WaspTilesViewResolver extends UrlBasedViewResolver {

	public WaspTilesViewResolver() {
		setViewClass(requiredViewClass());
	}

	/**
	 * Requires {@link TilesView}.
	 */
	@Override
	protected Class<WaspTilesView> requiredViewClass() {
		return WaspTilesView.class;
	}

}
