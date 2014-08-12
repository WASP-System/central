/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.plugin.mps.grid.software;
import edu.yu.einstein.wasp.software.SoftwarePackage;


/**
 * @author asmclellan
 */
public class VcfTools extends SoftwarePackage{

	private static final long serialVersionUID = -2765983552942308803L;

	public VcfTools() {}
	
	public String getVcfSubsetCommand(String inputFilename, String outputFilename, String columns, boolean bgzipOutput){
		// --columns <string>     File or comma-separated list of columns to keep in the vcf file. If file, one column per row
		// --exclude-ref          Exclude rows not containing variants.
		String bgzipCommand = " ";
		if (bgzipOutput)
			bgzipCommand = " | bgzip -c "; // -c      write on standard output, keep original files unchanged
		String command = "vcf-subset --exclude-ref --columns " + columns  + " " + inputFilename + bgzipCommand + "> " + outputFilename;
		logger.debug("Will conduct VCF Tools vcf-subset with command string: " + command);
		return command;
	}

	
}
