/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.partition;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.filetype.service.FastqService;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * {@link Partitioner} that takes a 
 * 
 * @author calder
 *
 */
public class MultipartFilePartitioner implements Partitioner {

	private Integer libraryId;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FastqService fastqService;
	
	/**
	 * 
	 */
	public MultipartFilePartitioner() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.partition.support.Partitioner#partition(int)
	 */
	@Override
	public Map<String, ExecutionContext> partition(int arg0) {
//		try {
//			ExecutionContext ectx = new ExecutionContext();
//			
//			List<FileHandle> files = fileService.getFilesForLibraryByType(sampleService.getSampleById(libraryId), 
//					fileService.getFileType(FastqService.FASTQ_INAME));
//			
//		} catch (SampleTypeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return null;
	}

	/**
	 * @return the primaryFileId
	 */
	public Integer getLibraryId() {
		return libraryId;
	}

	/**
	 * @param primaryFileId the primaryFileId to set
	 */
	public void setLibraryUd(Integer libraryId) {
		this.libraryId = libraryId;
	}

}
