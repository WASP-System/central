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
public class ImageFilePanelRenderer {

	public static Panel getPanelForFileGroup(String url, String desc) {
		WebPanel panel = new WebPanel();
		WebContent content = new WebContent();

		if (url == null) {
			panel.setContent(content);
			return panel;
		}

		String html = "<img src='" + url + "' alt='"+ desc + "'>";

		content.setHtmlCode(html);

		panel.setContent(content);

		return panel;
	}

}
