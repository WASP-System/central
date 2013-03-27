package edu.yu.einstein.wasp.service.impl.illumina;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.service.illumina.WaspIlluminaService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.PropertyHelper;
import edu.yu.einstein.wasp.util.illumina.IlluminaRunFolderNameParser;

@Service
@Transactional("entityManager")
public class WaspIlluminaServiceImpl extends WaspServiceImpl implements WaspIlluminaService{
	
	@Autowired
	private GridHostResolver hostResolver;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getIlluminaRunFolders() throws GridException{
		Set<String> folderNames = new HashSet<String>();
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
		GridWorkService workService = hostResolver.getGridWorkService(w);
		GridTransportConnection transportConnection = workService.getTransportConnection();
		String stageDir = transportConnection.getConfiguredSetting("illumina.data.stage");
		if (!PropertyHelper.isSet(stageDir))
			throw new GridException("illumina.data.stage is not defined!");
		w.setWorkingDirectory(stageDir);
		w.addCommand("ls -1");
		try {
			GridResult r = transportConnection.sendExecToRemote(w);
			BufferedReader br = new BufferedReader(new InputStreamReader(r.getStdOutStream()));
			boolean keepReading = true;
			while (keepReading){
				String line = br.readLine();
				if (line == null)
					keepReading = false;
				else{
					if (IlluminaRunFolderNameParser.isProperlyFormed(line)){
						logger.debug("Adding Illumina Run Folder " + line + " to working set");
						folderNames.add(line);
					}
					IlluminaRunFolderNameParser parser = new IlluminaRunFolderNameParser(line);
					logger.debug(parser.toString());
				}
			}
		} catch (Exception e) {
			throw new GridException("Caught " + e.getClass().getSimpleName() + " when trying to read Illumina run folder (illumina.data.stage): " + e.getLocalizedMessage());
		}
		return folderNames;
	}


}
