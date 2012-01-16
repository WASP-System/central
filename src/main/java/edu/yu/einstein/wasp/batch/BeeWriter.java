package edu.yu.einstein.wasp.batch;
import java.util.List;

import org.springframework.batch.item.ItemWriter;


public class BeeWriter implements ItemWriter<String> {

	@Override
	public void write(List<? extends String> items) throws Exception {
		System.err.println("Processing items "+items);
		//do something with the filePath
		//return "";
	}

	
	
}
