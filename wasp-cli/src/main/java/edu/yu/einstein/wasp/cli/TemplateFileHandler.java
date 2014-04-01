package edu.yu.einstein.wasp.cli;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TemplateFileHandler {
	
	public static void createTemplateFile(Path path, boolean isUsingExistingCellLibraries) throws IOException{
		Path templateFile;
		try {
			Files.deleteIfExists(path);
			templateFile = Files.createFile(path);
		} catch (IOException e) {
			throw new IOException("Unable to create file " + path.getFileName());
		}
		List<String> lines = new ArrayList<>();
		lines.add("\"# It is not necessary to duplicate entries, e.g. if supplying many samples for one job, a job entry will be matched with samples on subsequent lines until the next job entry is found.\"");
		lines.add("\"# You can specify a metadata entry directly via it's key, e.g. FileMeta.area.key (where the entire metakey equals 'area.key').\"");
		lines.add("\"# e.g. if you are providing columns for a FileGroup, any extra columns you add for FilegroupMeta etries will be linked to the FileGroup when it is created.\"");
		lines.add("# to view available file types use the CLI: 'wasp -u <user> -p <pass> -list fileTypes'");
		lines.add("#");
		lines.add("# enter N/A for uknown values");
		lines.add("#");
		if (isUsingExistingCellLibraries){
			lines.add("\"# You can add additional metadata columns for any of SampleSourceMeta, SampleMeta, FileGroupMeta and FileHandleMeta\"");
			lines.add("# to view available cell libraries use the CLI: 'wasp -u <user> -p <pass> -list cellLibraries'");
			lines.add("cellLibraryId,FileGroup.description,FileGroup.fileTypeId,FileHandle.fileName,FileHandle.fileURI,FileHandle.md5hash");
		} else {
			lines.add("use a '*' in the sample name column to indicate creation of a new cell library for the current library (as per the previous line) is required");
			lines.add("\"# You can add additional metadata columns for any of JobMeta, SampleMeta, FileGroupMeta and FileHandleMeta\"");
			lines.add("# to view available assay workflows use the CLI: 'wasp -u <user> -p <pass> -list workflows'");
			lines.add("# to view available sample subtypes use the CLI: 'wasp -u <user> -p <pass> -list sampleSubtypes'");
			lines.add("# to view available genome builds use the CLI: 'wasp -u <user> -p <pass> -list builds'");
			lines.add("# to view available users use the CLI: 'wasp -u <user> -p <pass> -list users'");
			lines.add("\"# or combined in one command: 'wasp -u <user> -p <pass> -list workflows,sampleSubtypes,builds,users'\"");
			lines.add("Job.name,Job.userId,Job.workflowId,Sample.name,Sample.sampleSubtypeId,SampleMeta.genome.genomeString,FileGroup.description,FileGroup.fileTypeId,FileHandle.fileName,FileHandle.fileURI,FileHandle.md5hash");
		}

		try {
			Files.write(templateFile, lines, Charset.defaultCharset());
		} catch (IOException e) {
			throw new IOException("Unable to write to file " + path.getFileName());
		}
	}
	

	public static List<List<String>> importTemplateFileData(Path path) throws IOException{
		List<List<String>> data = new ArrayList<>();
		List<String> lines = new ArrayList<>();
		if (!Files.exists(path)){
			throw new IOException("File does not exist: " + path.getFileName());
		}
		try {
			lines = Files.readAllLines(path, Charset.defaultCharset());
		} catch (IOException e) {
			throw new IOException("Unable to read file " + path.getFileName());
		}
		int lineCount = 0;
		for (String line : lines){
			line = line.trim();
			if (line.startsWith("#") || line.startsWith("\"#"))
				continue;
			int elementCount = 0;
			if (line.isEmpty() || line.replaceAll(",","").isEmpty())
				continue;
			data.add(new ArrayList<String>());
			for (String element : line.split(",")){ // note: Trailing empty strings are not included in the resulting array.
				if (element.isEmpty()){
					if (lineCount <= 0)
						throw new IOException("Neither header line or first line of data can have empty elements");
					element = data.get(lineCount-1).get(elementCount); // value on previous line
				}
				else if (element.equalsIgnoreCase("N/A"))
					element = "";
				data.get(lineCount).add(element);
				elementCount++;
			}
			lineCount++;
		}
		return data;
	}


}
