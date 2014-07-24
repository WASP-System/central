/**
 * 
 */
package edu.yu.einstein.wasp.genericfileviewing.panelrenderer;

import edu.yu.einstein.wasp.viewpanel.PDFContent;
import edu.yu.einstein.wasp.viewpanel.PDFPanel;
import edu.yu.einstein.wasp.viewpanel.Panel;

/**
 * @author aj
 *
 */
public class PdfFilePanelRenderer {

	public static Panel getPanelForFileGroup(String fileURL) {
		PDFPanel panel = new PDFPanel();
		PDFContent content = new PDFContent();

		if (fileURL == null) {
			panel.setContent(content);
			return panel;
		}
		
		content.setPdfURL(fileURL);
		panel.setContent(content);

		return panel;
	}

}
