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
public class TsvFilePanelRenderer extends AbstractSvFilePanelRender{
	
	private static int LINE_LIMIT = 10;
	
	private static String DELIMITER = "\t";

	public static PanelTab getPanelForFileGroup(String fileName, InputStream is, boolean header) {
		PanelTab panelTab = new PanelTab();
		panelTab.setNumberOfColumns(1);
		panelTab.setName("TSV File Viewer");
		panelTab.setDescription("Generic TSV file viewing");
		panelTab.setMaxOnLoad(true);
		GridPanel panel = new GridPanel();
		panel.setTitle(fileName);
		panelTab.addPanel(panel);
		if (is == null) {
			panel.setContent(new GridContent());
			return panelTab;
		}
		panel.setContent(getGridContent(is, header, DELIMITER, LINE_LIMIT));
		return panelTab;
	}

}
