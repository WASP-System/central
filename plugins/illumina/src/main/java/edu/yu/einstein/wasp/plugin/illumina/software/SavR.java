/**
 * 
 */
package edu.yu.einstein.wasp.plugin.illumina.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author calder
 * 
 */
public class SavR extends IlluminaSequenceRunSubProcessor {

    /**
     * 
     */
    private static final long serialVersionUID = 2521660754296864181L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public SavR() {
        setSoftwareVersion("0.2.0"); // this default may be overridden in
                                     // wasp.site.properties
    }

    public String getSavR() {
        String retval = "rm -rf Data/wasp-reports/\n" +
                        "R --vanilla <<EOF\n" +
                        "require(savR)\n" +
                        "fc <- savR('.')\n" +
                        "buildReports(fc, 'Data/wasp-reports')\n" +
                        "EOF\n";
        
        return retval;
                
    }

    @Override
    public String getStageDirectoryName() {
        logger.error("should use CASAVA stage directory, returning null");
        return null;
    }
}
