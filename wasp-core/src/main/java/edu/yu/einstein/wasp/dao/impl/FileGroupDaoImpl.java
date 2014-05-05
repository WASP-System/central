/**
 * 
 */
package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.SampleSource;

/**
 * @author calder
 *
 */
@Transactional("entityManager")
@Repository
public class FileGroupDaoImpl extends WaspDaoImpl<FileGroup> implements edu.yu.einstein.wasp.dao.FileGroupDao {

	/**
	 * FileHandleDaoImpl() Constructor
	 *
	 *
	 */
	public FileGroupDaoImpl() {
		super();
		this.entityClass = FileGroup.class;
	}


	/**
	 * getFileByFileId(final Integer fileId)
	 *
	 * @param final Integer fileId
	 *
	 * @return file
	 */

	@Override
	@Transactional("entityManager")
	public FileGroup getFileGroupById (final Integer id) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", id);

		List<FileGroup> results = this.findByMap(m);

		if (results.size() == 0) {
			FileGroup rt = new FileGroup();
			return rt;
		}
		return results.get(0);
	}
	
		
	@Override
	@Transactional("entityManager")
	public Set<FileGroup> getFilesForCellLibraryByType(SampleSource cellLibrary, FileType fileType) {
		TypedQuery<FileGroup> fgq = getEntityManager()
				.createQuery("SELECT DISTINCT fg from FileGroup as fg " +
						"JOIN FETCH fg.sampleSources as cl " +
						"WHERE cl = :cellLibrary AND fg.fileType = :fileType", FileGroup.class)
				.setParameter("cellLibrary", cellLibrary)
				.setParameter("fileType", fileType);
		HashSet<FileGroup> result = new HashSet<FileGroup>();
		result.addAll(fgq.getResultList());
		return result;
	}
	
	@Override
	@Transactional("entityManager")
	public Set<FileGroup> getActiveFilesForCellLibraryByType(SampleSource cellLibrary, FileType fileType) {
		TypedQuery<FileGroup> fgq = getEntityManager()
				.createQuery("SELECT DISTINCT fg from FileGroup as fg " +
						"JOIN FETCH fg.sampleSources as cl " +
						"WHERE cl = :cellLibrary AND fg.fileType = :fileType AND fg.isActive = 1", FileGroup.class)
				.setParameter("cellLibrary", cellLibrary)
				.setParameter("fileType", fileType);
		HashSet<FileGroup> result = new HashSet<FileGroup>();
		result.addAll(fgq.getResultList());
		return result;
	}

	@Override
	@Transactional("entityManager")
	public Set<FileGroup> getFilesForCellLibrary(SampleSource cellLibrary) {
		TypedQuery<FileGroup> fgq = getEntityManager()
				.createQuery("SELECT DISTINCT fg from FileGroup as fg " +
						"JOIN FETCH fg.sampleSources as cl " +
						"WHERE cl = :cellLibrary", FileGroup.class)
				.setParameter("cellLibrary", cellLibrary);
		HashSet<FileGroup> result = new HashSet<FileGroup>();
		result.addAll(fgq.getResultList());
		return result;
	}

}
