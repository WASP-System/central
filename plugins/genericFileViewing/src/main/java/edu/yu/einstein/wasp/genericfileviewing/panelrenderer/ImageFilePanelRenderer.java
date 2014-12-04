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
public class ImageFilePanelRenderer {

	public static PanelTab getPanelForFileGroup(String fileName, String url) {
		PanelTab panelTab = new PanelTab();
		panelTab.setNumberOfColumns(1);
		panelTab.setTabTitle("Image File Viewer");
		panelTab.setDescription("Generic image file viewing");
		
		WebPanel panel = new WebPanel();
		panel.setTitle(fileName);
		panel.setMaxOnLoad(true);

		WebContent content = new WebContent();
		if (url == null) {
			panel.setContent(content);
			return panelTab;
		}

		String html = "<img src='" + url + "' alt='"+ fileName + "'>";
		content.setHtmlCode(html);

		panel.setContent(content);

		panelTab.addPanel(panel);
		return panelTab;
	}

}
