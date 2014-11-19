/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.macstwo.service;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface MacstwoService extends WaspService {

		/**
		 * Perform Service
		 * @return String
		 */
		public String performAction();
		
		public FileHandle createAndSaveInnerFileHandle(String fileName, FileType fileType);
		
		public FileGroup createAndSaveInnerFileGroup(FileHandle fileHandle, Software software, String description);

}
