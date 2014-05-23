/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.genericfileviewing.service.impl;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.yu.einstein.wasp.genericfileviewing.service.GenericfileviewingService;

import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.viewpanel.PanelTab;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;
import edu.yu.einstein.wasp.exception.PanelException;

@Service
@Transactional("entityManager")
public class GenericfileviewingServiceImpl extends WaspServiceImpl implements GenericfileviewingService {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}

	@Override
	public PanelTab getPanelTabForFileGroup(Integer id) throws PanelException {
		PanelTab panelTab = new PanelTab();
		panelTab.setName("generic file viewing");
		//panelTab.setDescription("");

		WebPanel wp = new WebPanel();
		WebContent wc = new WebContent();
		wc.setHtmlCode("test");
		wp.setContent(wc);
		panelTab.addPanel(wp);
//		JSONObject json = getJsonForParsedSoftwareOutputByKey(FastQScreenTasklet.FASTQSCREEN_PLOT_META_KEY, fastqscreen, fileGroupId);
//		if (json != null)
//			panelTab.addPanel(BabrahamPanelRenderer.getFastQScreenPanel(json, messageService, servletPath));
		return panelTab;
	}


}
