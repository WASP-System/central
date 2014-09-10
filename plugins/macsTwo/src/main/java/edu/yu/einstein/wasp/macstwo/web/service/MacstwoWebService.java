package edu.yu.einstein.wasp.macstwo.web.service;

import java.util.Set;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.macstwo.service.MacstwoService;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

public interface MacstwoWebService extends MacstwoService {

	public Set<PanelTab> getMacstwoDataToDisplay(Job job)throws PanelException;

}
