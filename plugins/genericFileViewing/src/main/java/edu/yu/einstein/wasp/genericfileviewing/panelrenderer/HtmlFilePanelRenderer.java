/**
 * 
 */
package edu.yu.einstein.wasp.genericfileviewing.panelrenderer;

import edu.yu.einstein.wasp.viewpanel.PanelTab;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;

/**
 * @author aj
 * 
 */
public class HtmlFilePanelRenderer {

	public static PanelTab getPanelForFileGroup(String fileName, String url) {
		PanelTab panelTab = new PanelTab();
		panelTab.setNumberOfColumns(1);
		panelTab.setTabTitle("HTML File Viewer");
		panelTab.setDescription("Generic HTML file viewing");
		
		WebPanel panel = new WebPanel();
		panel.setTitle(fileName);
		panel.setMaxOnLoad(true);
		WebContent content = new WebContent();
		if (url == null) {
			panel.setContent(content);
			return panelTab;
		}

		content.setHtmlCode("<div class='scrollable' id='includedContent-" + url.hashCode() + "'></div>");
		String jsScript = "$('#includedContent-"+url.hashCode()+"').load('"+url+"');";
		content.setScriptCode(jsScript);

		panel.setContent(content);
		panel.setExecOnRenderCode(jsScript);
		panel.setExecOnResizeCode(jsScript);
		panel.setExecOnExpandCode(jsScript);

		panelTab.addPanel(panel);
		return panelTab;
	}

}
