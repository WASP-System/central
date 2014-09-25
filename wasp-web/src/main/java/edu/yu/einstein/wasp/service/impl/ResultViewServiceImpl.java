package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.ResultViewService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.viewpanel.Action;
import edu.yu.einstein.wasp.viewpanel.Action.CallbackFunctionType;
import edu.yu.einstein.wasp.viewpanel.GridColumn;
import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridDataField;
import edu.yu.einstein.wasp.viewpanel.GridPanel;



@Service
@Transactional("entityManager")
public class ResultViewServiceImpl extends WaspServiceImpl implements ResultViewService{
	
	@Autowired
	private FileUrlResolver fileUrlResolver;

	@Autowired
	private FileService fileService;
	
	@Autowired
	SampleService sampleService;
	
	
	/**
	 * {@inheritDoc}
	 * @throws WaspException 
	 */
	@Override
	public List<Map<String,String>> getFileDataMapList(List<Integer> fgIdList) throws WaspException {
		List<Map<String,String>> fileMapList = new ArrayList<Map<String,String>>();
		for (int fgId : fgIdList) {
			FileGroup fg = fileService.getFileGroupById(fgId);

			String fgidStr = fg.getId().toString();
			String fgName = fg.getDescription();
			Hyperlink hl = null;
			try {
				hl = new Hyperlink("Download", fileUrlResolver.getURL(fg).toString());
			} catch (GridUnresolvableHostException e) {
				throw new WaspException(e); 
			}
			String fgLink = hl.getTargetLink();
			
			Set<FileHandle> fhSet = fg.getFileHandles();
			for (FileHandle fh : fhSet) {
				Map<String,String> fileInfoMap = new HashMap<String,String>();
				fileInfoMap.put("fgid", fgidStr);
				fileInfoMap.put("fgname", fgName);
				fileInfoMap.put("fglink", fgLink);
				
				fileInfoMap.put("fid", fh.getId().toString());
				fileInfoMap.put("fname", fh.getFileName());
				fileInfoMap.put("md5", fh.getMd5hash());
				fileInfoMap.put("size", fh.getSizek() != null ? fh.getSizek().toString() : "0");
				//fileMap.put("updated", fh.getUpdated().);
				hl = new Hyperlink("Download", fileUrlResolver.getURL(fh).toString());
				fileInfoMap.put("link", hl.getTargetLink());
				
				fileMapList.add(fileInfoMap);
			}
		}
		return fileMapList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridPanel getFileGridPanel(Integer fileTypeId, Integer libraryId, Integer cellId) throws WaspException{
		FileType ft = fileService.getFileType(fileTypeId);
		Set<FileGroup> fgSet = new HashSet<FileGroup>();
		if (libraryId != null) {
			Sample library = sampleService.getSampleById(libraryId);
			if (cellId != null) { 
				if (cellId < 0){ // will be < 0 if a library is imported 
					SampleSource cellLibrary = sampleService.getCellLibrariesForLibrary(library).get((cellId * -1) - 1);
					fgSet.addAll(fileService.getFilesForCellLibraryByType(cellLibrary, ft));
				} else {
					Sample cell = sampleService.getSampleById(cellId);
					fgSet.addAll(fileService.getFilesForCellLibraryByType(cell, library, ft));
				}
			} else {
				fgSet.addAll(fileService.getFilesForLibraryByType(library, ft));
			}
		}
		
		Set<FileHandle> fhSet = new HashSet<FileHandle>();
		Hyperlink hl;

		GridPanel filePanel = new GridPanel();
		filePanel.setTitle("File Download Panel");
		GridContent fileGridContent = new GridContent();
		fileGridContent.addColumn(new GridColumn("File Name", "fname", 1));
		fileGridContent.addColumn(new GridColumn("MD5 Checksum", "md5", 300, 0, "center", "center"));
		fileGridContent.addColumn(new GridColumn("Size", "size", 100, 0, true, false));
		
		fileGridContent.addDataFields(new GridDataField("fgname", "string"));
		fileGridContent.addDataFields(new GridDataField("fid", "string"));
		fileGridContent.addDataFields(new GridDataField("fname", "string"));
		fileGridContent.addDataFields(new GridDataField("md5", "string"));
		fileGridContent.addDataFields(new GridDataField("size", "string"));
		fileGridContent.addDataFields(new GridDataField("dllink", "string"));
//		fileGridContent.addDataFields(new GridDataField("gblink", "string"));
//		fileGridContent.addDataFields(new GridDataField("gbtype", "string"));
//		fileGridContent.addDataFields(new GridDataField("gbttp", "string"));
//		fileGridContent.addDataFields(new GridDataField("hidegb", "boolean"));
		
		try {
			for (FileGroup fg : fgSet) {

				String fgName = fg.getDescription();
				fhSet = fg.getFileHandles();
				for (FileHandle fh : fhSet) {
					
					List<String> filerow = new ArrayList<String>();
					filerow.add(fgName);
					filerow.add(fh.getId().toString());
					filerow.add(fh.getFileName());
					filerow.add(fh.getMd5hash());
					filerow.add(fh.getSizek() != null ? fh.getSizek().toString() : "");
					hl = new Hyperlink("Download", fileUrlResolver.getURL(fh).toString());
					filerow.add(hl.getTargetLink());
/*					
					filerow.add(hl.getTargetLink());
//					filerow.add("ucsc");
//					filerow.add("View in UCSC Genome Browser");
//					filerow.add("false");
					switch ( (int) (Math.random()*3) ) {
						case 0:	filerow.add("ucsc");
								filerow.add("View in UCSC Genome Browser");
								if ((int) (Math.random()*2)==0)
									filerow.add("false");
								else
									filerow.add("true");
								break;
						
						case 1:	filerow.add("ensembl");
								filerow.add("View in Ensembl Genome Browser");
								if ((int) (Math.random()*2)==0)
									filerow.add("false");
								else
									filerow.add("true");
								break;
						
						case 2:	filerow.add("igv");
								filerow.add("View in IGV Genome Browser");
								if ((int) (Math.random()*2)==0)
									filerow.add("false");
								else
									filerow.add("true");
								break;
						
						default: break;
					}
*/					
					fileGridContent.addDataRow(filerow);
					
					List<Action> actionrow = new ArrayList<Action>();
					Action dlAction = new Action();
					switch ( 0 ) {//(int) (Math.random()*3) ) {
						case 0:	dlAction.setIconClassName("icon-download");
								dlAction.setTooltip("Download");
								dlAction.setCallbackFunctionType(CallbackFunctionType.DOWNLOAD);
								dlAction.setCallbackContent("dllink");
								break;
						
						case 1:	dlAction.setIconClassName("icon-gb-ucsc");
								dlAction.setTooltip("View in ucsc");
								break;
						
						case 2:	dlAction.setIconClassName("icon-gb-ensembl");
								dlAction.setTooltip("View in ensemble");
								break;
				
						default: break;
					}
					actionrow.add(dlAction);
					
					fileGridContent.addActions(actionrow);
				}
			}
			
			// after all rows added to the content, call following method to add missing actions as hidden
			// actions to the grid
			fileGridContent.appendActionsToData();
			
		} catch (GridUnresolvableHostException e) {
			throw new WaspException(e);
		}
		
		filePanel.setContent(fileGridContent);
		
		filePanel.setGrouping(true);
		filePanel.setGroupField("fgname");
		
		filePanel.setHasDownload(true);
		filePanel.setDownloadLinkField("link");
		filePanel.setDownloadTooltip("Download");
		
		filePanel.setAllowSelectDownload(true);
		filePanel.setSelectDownloadText("Download selected");
		
		filePanel.setAllowGroupDownload(true);
		filePanel.setGroupDownloadTooltip("Download all");
		filePanel.setGroupDownloadAlign("left");
		
		filePanel.setHasGbLink(true);
		filePanel.setGbLinkField("gblink");
		filePanel.setGbTypeField("gbtype");
		filePanel.setGbTtpField("gbttp");
		filePanel.setHideGbField("hidegb");
		return filePanel;
	}
	
}
