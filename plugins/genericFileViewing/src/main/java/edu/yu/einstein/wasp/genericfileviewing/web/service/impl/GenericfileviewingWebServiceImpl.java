/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.genericfileviewing.web.service.impl;

import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.genericfileviewing.panelrenderer.CsvFilePanelRenderer;
import edu.yu.einstein.wasp.genericfileviewing.panelrenderer.PdfFilePanelRenderer;
import edu.yu.einstein.wasp.genericfileviewing.panelrenderer.TextFilePanelRenderer;
import edu.yu.einstein.wasp.genericfileviewing.service.impl.GenericfileviewingServiceImpl;
import edu.yu.einstein.wasp.genericfileviewing.web.service.GenericfileviewingWebService;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

@Service
@Transactional("entityManager")
public class GenericfileviewingWebServiceImpl extends GenericfileviewingServiceImpl implements GenericfileviewingWebService {

	@Autowired
	WaspPlugin genericfileviewingPlugin;
	
	@Autowired
	private FileService fileService;
	
	@Override
	public PanelTab getPanelTabForFileGroup(FileGroup fileGroup) throws PanelException {
		PanelTab panelTab = new PanelTab();
		panelTab.setName(genericfileviewingPlugin.getName());
		panelTab.setDescription(genericfileviewingPlugin.getDescription());
		panelTab.setNumberOfColumns(1);
		
		Set<FileHandle> set = fileGroup.getFileHandles();
		Iterator<FileHandle> it = set.iterator();
		FileHandle file = it.next();

		Panel p;
		if (fileGroup.getFileType().getIName().equals("txt")) {
			p = TextFilePanelRenderer.getPanelForFileGroup(fileService.getInputStreamFromFileHandle(file));
		} else if (fileGroup.getFileType().getIName().equals("csv")) {
			p = CsvFilePanelRenderer.getPanelForFileGroup(fileService.getInputStreamFromFileHandle(file), false);
		} else if (fileGroup.getFileType().getIName().equals("pdf")) {
			p = PdfFilePanelRenderer.getPanelForFileGroup(fileService.getURLStringFromFileHandle(file));
		} else {
			p = new Panel();
		}

		p.setTitle(fileGroup.getDescription());
		panelTab.addPanel(p);

		return panelTab;
	}


}
