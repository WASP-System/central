/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.helptagham.software;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
// Un-comment the following if using the plugin service
// import org.springframework.beans.factory.annotation.Autowired;
// import package edu.yu.einstein.wasp.helptagham.service. HelptaghamService;




/**
 * @author
 */
public class Helptagham extends SoftwarePackage{

	// Un-comment the following if using the plugin service
	//@Autowired
	//HelptaghamService  helptaghamService;
	
	@Autowired
	SampleService sampleService;

	/**
	 * 
	 */
	private static final long serialVersionUID = -687457148681027425L;

	public Helptagham() {
	}

	private WorkUnit prepareWorkUnit(List<FileHandle> fhlist) {
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();

		c.setProcessMode(ProcessMode.MAX);
		c.setMode(ExecutionMode.PROCESS);

		// require 4GB memory
		c.setMemoryRequirements(4);
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		SoftwarePackage helptag = this.getSoftwareDependencyByIname("helptag");
		Assert.assertParameterNotNull(helptag);
		sd.add(helptag);
		c.setSoftwareDependencies(sd);
		c.setWorkingDirectory(WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER);
		WorkUnit w = new WorkUnit(c);
		w.setRequiredFiles(fhlist);
		w.setSecureResults(true);

		return w;
	}

	@Transactional("entityManager")
	public WorkUnit getAngleMaker(String genome, String prefixForFileName, List<FileHandle> hpa2FileHandleList, List<FileHandle> msp1FileHandleList) {
		Assert.assertTrue(!hpa2FileHandleList.isEmpty());

		List<FileHandle> reqFHList = new ArrayList<FileHandle>();
		reqFHList.addAll(hpa2FileHandleList);
		reqFHList.addAll(msp1FileHandleList);
		WorkUnit w = prepareWorkUnit(reqFHList);

		StringBuilder tempCommand;

		int numHpaFiles = hpa2FileHandleList.size();
		int numMspFiles = msp1FileHandleList.size();

		String mergedHpa2HcountFile = "";
		if (numHpaFiles == 1) {
			mergedHpa2HcountFile = "${" + WorkUnit.INPUT_FILE + "[0]}";
		} else if (numHpaFiles > 1) {
			mergedHpa2HcountFile = prefixForFileName + ".merged_Hpa2.hcount";

			tempCommand = new StringBuilder();
			tempCommand.append("HpaiiCountCombine.pl ");
			for (int i = 0; i < numHpaFiles; i++) {
				tempCommand.append("${" + WorkUnit.INPUT_FILE + "[" + i + "]} ");
			}
			tempCommand.append(" > " + mergedHpa2HcountFile);

			String command1 = new String(tempCommand);
			w.addCommand(command1);
		}

		String mergedMsp1HcountFile = "";
		if (numMspFiles == 1) {
			mergedMsp1HcountFile = "${" + WorkUnit.INPUT_FILE + "[" + numHpaFiles + "]}";
		} else if (numMspFiles > 1) {
			mergedMsp1HcountFile = prefixForFileName + ".merged_Msp1.hcount";

			tempCommand = new StringBuilder();
			tempCommand.append("HpaiiCountCombine.pl ");
			for (int i = numHpaFiles; i < numHpaFiles + numMspFiles; i++) {
				tempCommand.append("${" + WorkUnit.INPUT_FILE + "[" + i + "]} ");
			}
			tempCommand.append(" > " + mergedMsp1HcountFile);

			String command1 = new String(tempCommand);
			w.addCommand(command1);
		}

		tempCommand = new StringBuilder();
		tempCommand.append("htgAngleMaker.pl -i " + mergedHpa2HcountFile);
		if (numMspFiles > 0) {
			tempCommand.append(" -m " + mergedMsp1HcountFile);
		}

		tempCommand.append(" -o " + prefixForFileName);

		tempCommand.append(" -g " + genome);

		w.addCommand(new String(tempCommand));

		return w;
	}
}
