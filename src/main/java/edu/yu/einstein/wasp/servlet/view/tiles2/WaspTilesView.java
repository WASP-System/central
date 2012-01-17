package edu.yu.einstein.wasp.servlet.view.tiles2;

/**
 * Apache Tiles interceptor to make tile definition name available via request attributes
 * 
 *  @author Sasha Levchuk
 */

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.tiles2.TilesView;

public class WaspTilesView extends TilesView {
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setAttribute("waspViewName", getUrl());
		super.renderMergedOutputModel(model, request, response);
	}

}
