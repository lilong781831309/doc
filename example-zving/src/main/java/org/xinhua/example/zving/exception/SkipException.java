package org.xinhua.example.zving.exception;

public class SkipException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	protected Object skipData;

	public SkipException() {
	}
	
	public SkipException(Object skipData) {
		this.skipData = skipData;
	}

	public Object getSkipData() {
		return skipData;
	}

	public void setSkipData(Object skipData) {
		this.skipData = skipData;
	}
	
	@Override
	public synchronized Throwable fillInStackTrace() {
		/* Empty */
		return this;
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return null;/* Return Empty */
	}

	@Override
	public void setStackTrace(StackTraceElement[] stackTrace) {
		/* Empty */
	}

}
