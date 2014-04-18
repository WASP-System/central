package edu.yu.einstein.wasp.plugin.fileformat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.FileTypeDao;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.plugin.fileformat.service.BamService;

@Service
@Transactional("entityManager")
public class BamServiceImpl implements BamService {
	
	@Autowired
	private FileTypeDao fileTypeDao;
	
	@Autowired
	private FileType bamFileType;

	@Override
	public FileType getBamFileType() {
		return bamFileType;
	}

}
