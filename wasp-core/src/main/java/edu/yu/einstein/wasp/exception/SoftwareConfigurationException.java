package edu.yu.einstein.wasp.exception;

public class SoftwareConfigurationException extends WaspException {

	public SoftwareConfigurationException() {}

	public SoftwareConfigurationException(String message) {
		super(message);
	}

	public SoftwareConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
