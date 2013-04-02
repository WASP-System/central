package edu.yu.einstein.wasp.util.illumina;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.yu.einstein.wasp.exception.illumina.IlluminaRunFolderParseException;

public class IlluminaRunFolderNameParser {
	
	private String runFolderName;
	
	private Date date;
	
	private String machineName;
	
	private Integer runNumber;
	
	private String flowcellName;
	
	private String flowcellPrefix;
	
	public IlluminaRunFolderNameParser(String runFolderName) throws IlluminaRunFolderParseException{
		setRunFolderName(runFolderName);
	}
	
	public void setRunFolderName(String runFolderName) throws IlluminaRunFolderParseException{
		this.runFolderName = runFolderName;
		Pattern p = Pattern.compile("^(\\d{6})_([^_].+?)_(\\d{4})_([AB])([a-zA-Z0-9]+)$");
		Matcher m = p.matcher(runFolderName);
		if (!m.matches())
			throw new IlluminaRunFolderParseException("runFolderName is not a valid run folder name");
		if (m.groupCount() != 5)
			throw new IlluminaRunFolderParseException("Expected 5 properties but got " + m.groupCount());
		
		DateFormat df = new SimpleDateFormat("yyMMdd");
		try {
			date = df.parse(m.group(1));
		} catch (ParseException e) {
			throw new IlluminaRunFolderParseException("Cannot parse date: " + e.getLocalizedMessage(), e);
		}
		machineName = m.group(2);
		try{
			runNumber = Integer.parseInt(m.group(3));
		} catch (NumberFormatException e){
			throw new IlluminaRunFolderParseException("Cannot parse runNumber to integer: " + e.getLocalizedMessage(), e);
		}
		
		flowcellPrefix = m.group(4);
		
		flowcellName = m.group(5);
	}
	
	public String getRunFolderName() {
		return runFolderName;
	}

	public Date getDate() {
		return date;
	}

	public String getMachineName() {
		return machineName;
	}

	public Integer getRunNumber() {
		return runNumber;
	}

	public String getFlowcellName() {
		return flowcellName;
	}
	
	public String getFlowcellPrefix() {
		return flowcellPrefix;
	}

	public static boolean isProperlyFormed(String runFolderName){
		Pattern p = Pattern.compile("^\\d{6}_[^_].+?_\\d{4}_[a-zA-Z0-9]+$");
		Matcher m = p.matcher(runFolderName);
		if (m.matches())
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		String value = "runFolderName=" + runFolderName + ", date=" + date.toString() + ", machineName=" + machineName;
		value += ", runNumber=" + runNumber.toString() + ", flowcellPrefix=" + flowcellPrefix + ", flowcellName=" + flowcellName;
		return value;
	}

}
