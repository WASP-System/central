package edu.yu.einstein.wasp.controller.util;

public class ExtStepInfoModel extends ExtModel{
	
	private static final long serialVersionUID = 6934767107669057926L;

	private String info;
	
	private String script;
	
	private String stdout;
	
	private String stderr;
	
	private String clusterReport;
	
	
	public ExtStepInfoModel() {
		super();
		this.info = "No basic step execution information is available ";
		this.script = "No execution script is available";
		this.stdout = "No stdout data is available";
		this.stderr = "No stderr data is available";
		this.clusterReport = "No cluster report is available";
	}

	public ExtStepInfoModel(String info, String script, String stdout, String stderr, String clusterReport) {
		super();
		this.info = info;
		this.script = script;
		this.stdout = stdout;
		this.stderr = stderr;
		this.clusterReport = clusterReport;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getStdout() {
		return stdout;
	}

	public void setStdout(String stdout) {
		this.stdout = stdout;
	}

	public String getStderr() {
		return stderr;
	}

	public void setStderr(String stderr) {
		this.stderr = stderr;
	}

	public String getClusterReport() {
		return clusterReport;
	}

	public void setClusterReport(String clusterReport) {
		this.clusterReport = clusterReport;
	}

}
