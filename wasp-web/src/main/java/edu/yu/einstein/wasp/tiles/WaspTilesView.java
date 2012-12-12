package edu.yu.einstein.wasp.tiles;

/**
 * Apache Tiles interceptor to make tile definition name available via request attributes
 * 
 *  @author Sasha Levchuk
 */

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WaspTilesView extends org.springframework.web.servlet.view.tiles2.TilesView {
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("Requesting view name '"+getUrl().toString()+"'");
		request.setAttribute("waspViewName", getUrl());
		request.setAttribute("waspViewUrl", request.getRequestURI());
		try{
			super.renderMergedOutputModel(model, request, response);
		} catch (Exception e){
			logger.error("Cannot renderMergedOutputModel ("+getUrl().toString()+")");
			throw new Exception(e);
		}
	}

}
