package edu.yu.einstein.wasp.exception;

public class PluginException extends RuntimeException {

	private static final long serialVersionUID = -5930567252661250215L;

	public PluginException() {
		// TODO Auto-generated constructor stub
	}

	public PluginException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PluginException(Throwable exception) {
		super(exception);
		// TODO Auto-generated constructor stub
	}

	public PluginException(String message, Throwable exception) {
		super(message, exception);
		// TODO Auto-generated constructor stub
	}

}
