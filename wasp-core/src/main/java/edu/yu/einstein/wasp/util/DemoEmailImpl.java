package edu.yu.einstein.wasp.util;

public class DemoEmailImpl implements DemoEmail{
	
	private static final long serialVersionUID = -1595849857120418032L;
	
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
