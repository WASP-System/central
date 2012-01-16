package edu.yu.einstein.wasp.batch;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.item.ItemReader;


public class BeeFileReader implements ItemReader<String> {

	List<String> list;
	int i;
	/**
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	@Override
	public String read() throws Exception {
		
		if (i<list.size()) {
			return list.get(i++);
			
		}
		return null;
	}

	public void setFilePath(String filePath) throws IOException {
		
		i=0;
		list=FileUtils.readLines(new File(filePath));
	}
	
}
