/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.plugin.mps.grid.software;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.software.SoftwarePackage;


/**
 * @author asmclellan
 */
public class SnpEff extends SoftwarePackage{

	private static final long serialVersionUID = 7543620294651559883L;
	
	public SnpEff() {
		
	}
	
	public static String getSnpEffGenomeDatabaseName(Build build){
		return build.getGenome().getName() + "." + build.getName();
	}
	
	/**
	 * Run SnpEff annotation command 'eff' on the provided vcf file
	 * @param inputVcfFileName
	 * @param outputVcfFileName
	 * @param build
	 * @return
	 */
	public String getAnnotateVcfCommand(String inputVcfFileName, String outputVcfFileName, Build build){
		String command = "snpeff eff -v " + getSnpEffGenomeDatabaseName(build) + " " + inputVcfFileName + " > " + outputVcfFileName;
		logger.debug("Will conduct SnpEFF annotation with command string: " + command);
		return command;
	}
	
	/**
	 * Run SnpEff annotation command 'eff' on the provided vcf file in cancer analysis mode
	 * @param inputVcfFileName
	 * @param outputVcfFileName
	 * @param build
	 * @return
	 */
	public String getAnnotateCancerVcfCommand(String inputVcfFileName, String outputVcfFileName, Build build){
		//  HGVS notations seems to be preferred in the clinical and cancer community hence use -hgvs to provide HGVS notation in AA sub-field.
		String command = "snpeff eff -v -cancer -hgvs " + getSnpEffGenomeDatabaseName(build) + " " + inputVcfFileName + " > " + outputVcfFileName;
		logger.debug("Will conduct SnpEFF annotation with command string: " + command);
		return command;
	}
	
	/**
	 * Annotate IDs in input vcf using those from the provided database file
	 * @param inputVcfFileName
	 * @param idVcfFileName
	 * @param outputVcfFileName
	 * @return
	 */
	public String getAnnotateIdsCommand(String inputVcfFileName, String idVcfFileName, String outputVcfFileName){
		String command = "snpsift annotate -id -v " + idVcfFileName + " " + inputVcfFileName + " > " + outputVcfFileName;
		logger.debug("Will conduct SnpSift id annotation with command string: " + command);
		return command;
	}
	
	/**
	 * If file provided does not already contain a PEDIGREE header line, one will be added with the provided test and control sample names.
	 * The provided file will be edited directly.
	 * @param vcfFileToModify
	 * @param testSampleName
	 * @param controlSampleName
	 * @return
	 */
	public String getAddPedigreeToHeaderCommand(String vcfFileToModify, String testSampleName, String controlSampleName){
		 String command = "grep -q '##PEDIGREE' " + vcfFileToModify + " || sed -i 's/#CHROM/##PEDIGREE=<" + 
				 testSampleName + ",Original=" + controlSampleName + ">\n&/' " + vcfFileToModify;
		logger.debug("Will conduct VCF header modification with command string: " + command);
		return command;
	}

	
}
