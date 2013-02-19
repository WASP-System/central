package edu.yu.einstein.wasp.exception;

public class SoftwareConfigurationException extends WaspException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6357633357261358513L;

	public SoftwareConfigurationException() {}

	public SoftwareConfigurationException(String message) {
		super(message);
	}

	public SoftwareConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
