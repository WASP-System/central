/**
 * 
 */
package edu.yu.einstein.wasp.genericfileviewing.panelrenderer;

import edu.yu.einstein.wasp.viewpanel.PDFContent;
import edu.yu.einstein.wasp.viewpanel.PDFPanel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * @author aj
 *
 */
public class PdfFilePanelRenderer {

	public static PanelTab getPanelForFileGroup(String fileName, String fileURL) {
		PanelTab panelTab = new PanelTab();
		panelTab.setNumberOfColumns(1);
		panelTab.setName("PDF File Viewer");
		panelTab.setDescription("Generic PDF file viewing");
		panelTab.setMaxOnLoad(true);
		PDFPanel panel = new PDFPanel();
		panel.setTitle(fileName);
		panelTab.addPanel(panel);
		PDFContent content = new PDFContent();
		if (fileURL == null) {
			panel.setContent(content);
			return panelTab;
		}
		
		content.setPdfURL(fileURL);
		panel.setContent(content);

		return panelTab;
	}

}
