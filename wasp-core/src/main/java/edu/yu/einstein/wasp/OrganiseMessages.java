package edu.yu.einstein.wasp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class OrganiseMessages {

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(FileSystems.getDefault().getPath(args[0]), Charset.defaultCharset());
		List<String> prefixesOfFormCodes = new ArrayList<>();
		for (String line: lines){
			if (line.endsWith(".metaposition")){
				//prefixesOfFormCodes.
			}
		}
		
	}

}
