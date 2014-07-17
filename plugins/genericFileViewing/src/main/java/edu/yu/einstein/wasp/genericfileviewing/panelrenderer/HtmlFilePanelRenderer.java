/**
 * 
 */
package edu.yu.einstein.wasp.genericfileviewing.panelrenderer;

import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;

/**
 * @author aj
 * 
 */
public class HtmlFilePanelRenderer {

	public static Panel getPanelForFileGroup(String url) {
		WebPanel panel = new WebPanel();
		WebContent content = new WebContent();

		if (url == null) {
			panel.setContent(content);
			return panel;
		}

		content.setHtmlCode("<div id='includedContent-" + url.hashCode() + "'></div>");
		String jsScript = "$('#includedContent-"+url.hashCode()+"').load('"+url+"');";
		content.setScriptCode(jsScript);

		panel.setContent(content);
		panel.setExecOnRenderCode(jsScript);
		panel.setExecOnResizeCode(jsScript);
		panel.setExecOnExpandCode(jsScript);

		return panel;
	}

}
