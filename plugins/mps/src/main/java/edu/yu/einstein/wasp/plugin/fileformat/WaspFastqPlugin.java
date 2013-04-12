/**
 * 
 */
package edu.yu.einstein.wasp.plugin.fileformat;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.dao.FileGroupDao;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.FileTypeViewProviding;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author asmclellan
 * 
 */
public class WaspFastqPlugin extends WaspPlugin implements ClientMessageI, FileTypeViewProviding {

	@Autowired
	private FileGroupDao fileGroupDao;

	@Autowired
	protected FileService fileService;

	@Autowired
	private FileUrlResolver fileUrlResolver;
	
	@Autowired
	protected SampleService sampleService;

	private static Logger logger = LoggerFactory.getLogger(WaspFastqPlugin.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -654454985142650980L;

	public WaspFastqPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
		super(pluginName, waspSiteProperties, channel);
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Hyperlink getDescriptionPageHyperlink(){
		return new Hyperlink("waspFastq.hyperlink.label", "/wasp-fastq/description.do");
	}
	
	@Override
	public Map<String, Hyperlink> getFileDetails(Integer fileGroupId) {
		FileGroup fg = fileGroupDao.getById(fileGroupId);
		Set<FileHandle> fhSet = fg.getFileHandles();
		
		Map<String, Hyperlink> details = new HashMap<String, Hyperlink>();
		Hyperlink hl;
		for (FileHandle fh : fhSet) {
			try {
				hl = new Hyperlink("Download", fileUrlResolver.getURL(fh).toString());
				details.put(fh.getFileName(), hl);
			} catch (GridUnresolvableHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return details;
	}

	@Override
	public String getDownloadPageForCellLibraryByFileType(Integer cellLibraryId, Integer fileTypeId) {
		try {
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
			FileType fileType = fileService.getFileType(fileTypeId);
		} catch (SampleTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "<div>Here is a test!</div>";
	}
}
