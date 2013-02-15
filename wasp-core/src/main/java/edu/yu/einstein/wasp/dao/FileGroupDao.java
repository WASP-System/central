/**
 * 
 */
package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.FileGroup;

/**
 * @author calder
 *
 */
public interface FileGroupDao extends WaspDao<FileGroup> {

	public FileGroup getFileGroupById(Integer id);

}
