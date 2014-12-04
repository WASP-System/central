/**
 * 
 */
package edu.yu.einstein.wasp.genericfileviewing.panelrenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.yu.einstein.wasp.viewpanel.PanelTab;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;

/**
 * @author aj
 * 
 */
public class TextFilePanelRenderer {

	private final static int MAX_NUMBER_CHARS_IN_PANEL = 2000;

	public static PanelTab getPanelForFileGroup(String fileName, InputStream is) {
		PanelTab panelTab = new PanelTab();
		panelTab.setNumberOfColumns(1);
		panelTab.setTabTitle("Text File Viewer");
		panelTab.setDescription("Generic text-file viewing");
		
		WebPanel panel = new WebPanel();
		panel.setTitle(fileName);
		panel.setMaxOnLoad(true);
		
		WebContent content = new WebContent();
		if (is == null) {
			panel.setContent(content);
			return panelTab;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<p>");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "<br>");
				if (sb.length() > MAX_NUMBER_CHARS_IN_PANEL) {
					sb.append("......");
					break;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sb.append("</p>");

		content.setHtmlCode(sb.toString());

		panel.setContent(content);

		panelTab.addPanel(panel);
		return panelTab;
	}

}
