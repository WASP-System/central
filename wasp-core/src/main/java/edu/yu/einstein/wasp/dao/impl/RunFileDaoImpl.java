
/**
 *
 * RunFileDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RunFile Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.RunFile;


@Transactional
@Repository
public class RunFileDaoImpl extends WaspDaoImpl<RunFile> implements edu.yu.einstein.wasp.dao.RunFileDao {

	/**
	 * RunFileDaoImpl() Constructor
	 *
	 *
	 */
	public RunFileDaoImpl() {
		super();
		this.entityClass = RunFile.class;
	}


	/**
	 * getRunFileByRunlanefileId(final int runcellfileId)
	 *
	 * @param final int runcellfileId
	 *
	 * @return runFile
	 */

	@Override
	@Transactional
	public RunFile getRunFileByRunlanefileId (final int runcellfileId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("runcellfileId", runcellfileId);

		List<RunFile> results = this.findByMap(m);

		if (results.size() == 0) {
			RunFile rt = new RunFile();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getRunFileByFileId(final int fileId)
	 *
	 * @param final int fileId
	 *
	 * @return runFile
	 */

	@Override
	@Transactional
	public RunFile getRunFileByFileId (final int fileId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("fileId", fileId);

		List<RunFile> results = this.findByMap(m);

		if (results.size() == 0) {
			RunFile rt = new RunFile();
			return rt;
		}
		return results.get(0);
	}



}

