package edu.yu.einstein.wasp.load.service;

import edu.yu.einstein.wasp.model.FileType;

/**
 * 
 * @author asmclellan
 *
 */
public interface FileTypeLoadService extends WaspLoadService {

	public FileType update(String iname, String name, String description, String extensions, int isActive);

}
