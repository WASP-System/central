/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.r.software;
import edu.yu.einstein.wasp.software.SoftwarePackage;
// Un-comment the following if using the plugin service
// import org.springframework.beans.factory.annotation.Autowired;
// import package edu.yu.einstein.wasp.r.service. RService;




/**
 * @author
 */
public class R extends SoftwarePackage{

	// Un-comment the following if using the plugin service
	//@Autowired
	//RService  rService;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1557112568796481994L;

	public R() {
		setSoftwareVersion("3.0.1"); // This default may also be overridden in wasp.site.properties
	}

	
}
