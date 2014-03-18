package edu.yu.einstein.wasp.util;

public class DemoEmailImpl implements DemoEmail{
	
	private String demoEmail = "";

	@Override
	public String getDemoEmail() {
		return demoEmail;
	}

	@Override
	public void setDemoEmail(String demoEmail) {
		this.demoEmail = demoEmail;
	}

}
