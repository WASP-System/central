/**
 * 
 */
package edu.yu.einstein.wasp.genericfileviewing.panelrenderer;

import java.io.InputStream;

import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridPanel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * @author aj
 *
 */
public class CsvFilePanelRenderer extends AbstractSvFilePanelRender{
	
	private static int LINE_LIMIT = 1000;
	
	private static String DELIMITER = ",";
	
	public static PanelTab getPanelForFileGroup(String fileName, InputStream is, boolean header) {
		PanelTab panelTab = new PanelTab();
		panelTab.setNumberOfColumns(1);
		panelTab.setMaxOnLoad(true);
		panelTab.setName("CSV File Viewer");
		panelTab.setDescription("Generic CSV file viewing");
		GridPanel panel = new GridPanel();
		panel.setTitle(fileName + " (up to " + LINE_LIMIT + " lines shown)");
		panelTab.addPanel(panel);
		if (is == null) {
			panel.setContent(new GridContent());
			return panelTab;
		}
		panel.setContent(getGridContent(is, header, DELIMITER, LINE_LIMIT));
		return panelTab;
	}

}
