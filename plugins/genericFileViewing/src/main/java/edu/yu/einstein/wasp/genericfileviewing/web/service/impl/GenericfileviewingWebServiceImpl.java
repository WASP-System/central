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
import edu.yu.einstein.wasp.genericfileviewing.panelrenderer.HtmlFilePanelRenderer;
import edu.yu.einstein.wasp.genericfileviewing.panelrenderer.ImageFilePanelRenderer;
import edu.yu.einstein.wasp.genericfileviewing.panelrenderer.PdfFilePanelRenderer;
import edu.yu.einstein.wasp.genericfileviewing.panelrenderer.TextFilePanelRenderer;
import edu.yu.einstein.wasp.genericfileviewing.panelrenderer.TsvFilePanelRenderer;
import edu.yu.einstein.wasp.genericfileviewing.panelrenderer.VcfFilePanelRenderer;
import edu.yu.einstein.wasp.genericfileviewing.service.impl.GenericfileviewingServiceImpl;
import edu.yu.einstein.wasp.genericfileviewing.web.service.GenericfileviewingWebService;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.service.FileService;
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
		PanelTab panelTab = null;
		
		Set<FileHandle> set = fileGroup.getFileHandles();
		Iterator<FileHandle> it = set.iterator();
		FileHandle file = it.next();
		String fileName = fileGroup.getDescription();
		logger.debug("Getting PanelTab for file of type " + fileGroup.getFileType().getIName());
		if (fileGroup.getFileType().getIName().equals("txt")) {
			panelTab = TextFilePanelRenderer.getPanelForFileGroup(fileName, fileService.getInputStreamFromFileHandle(file));
		} else if (fileGroup.getFileType().getIName().equals("html")) {
			panelTab = HtmlFilePanelRenderer.getPanelForFileGroup(fileName, fileService.getURLStringFromFileHandle(file));
		} else if (fileGroup.getFileType().getIName().equals("csv")) {
			panelTab = CsvFilePanelRenderer.getPanelForFileGroup(fileName, fileService.getInputStreamFromFileHandle(file), false);
		} else if (fileGroup.getFileType().getIName().equals("tsv")) {
			panelTab = TsvFilePanelRenderer.getPanelForFileGroup(fileName, fileService.getInputStreamFromFileHandle(file), false);
		} else if (fileGroup.getFileType().getIName().equals("vcf")) {
			panelTab = VcfFilePanelRenderer.getPanelForFileGroup(fileName, fileService.getInputStreamFromFileHandle(file), false);
		} else if (fileGroup.getFileType().getIName().equals("pdf")) {
			panelTab = PdfFilePanelRenderer.getPanelForFileGroup(fileName, fileService.getURLStringFromFileHandle(file));
		} else if (fileGroup.getFileType().getIName().equals("jpg") ||
				fileGroup.getFileType().getIName().equals("bmp") || 
				fileGroup.getFileType().getIName().equals("gif") || 
				fileGroup.getFileType().getIName().equals("png") || 
				fileGroup.getFileType().getIName().equals("tif")) {
			panelTab = ImageFilePanelRenderer.getPanelForFileGroup(fileName, fileService.getURLStringFromFileHandle(file));
		} else {
			panelTab = new PanelTab();
			panelTab.setDescription(genericfileviewingPlugin.getDescription());
			panelTab.setNumberOfColumns(1);
			panelTab.setMaxOnLoad(true);
		}

		return panelTab;
	}


}
