package org.xinhua.example.zving.exception;


public class BeanGetPropertyException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BeanGetPropertyException(Exception e) {
		super(e);
	}
}
