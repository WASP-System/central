/**
 * 
 */
package edu.yu.einstein.wasp.genericfileviewing.panelrenderer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;

/**
 * @author aj
 *
 */
public class PdfFilePanelRenderer {

	public static Panel getPanelForFileGroup(String fileURL) {
		WebPanel panel = new WebPanel();
		WebContent content = new WebContent();

		if (fileURL == null) {
			panel.setContent(content);
			return panel;
		}

		content.setHtmlCode("<iframe src='http://docs.google.com/gview?url="
				+ fileURL + "&embedded=true' class='auto-height' scrolling='no' frameborder='0'></iframe>");
//		String strHtml = "<object data='" + fileURL + "' type='application/pdf'>";
//		strHtml += "<embed src='" + fileURL + "' type='application/pdf' />";
//		strHtml += "</object>";
//		content.setHtmlCode(strHtml);
		
		Set<URI> dependencies =  new LinkedHashSet<URI>(); // load order is important
		try {
			dependencies.add(new URI("https://raw.githubusercontent.com/house9/jquery-iframe-auto-height/master/release/jquery.browser.js"));
			dependencies.add(new URI("https://raw.githubusercontent.com/house9/jquery-iframe-auto-height/master/release/jquery.iframe-auto-height.js"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		content.setScriptDependencies(dependencies);
		
		String scriptCode = " $('iframe').iframeAutoHeight({debug: true});";
//		content.setScriptCode(scriptCode);

		panel.setContent(content);
		panel.setExecOnRenderCode(scriptCode);
		panel.setExecOnResizeCode(scriptCode);
		panel.setExecOnExpandCode(scriptCode);

		return panel;
	}

}
