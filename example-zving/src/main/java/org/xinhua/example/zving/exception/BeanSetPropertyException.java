package org.xinhua.example.zving.exception;


public class BeanSetPropertyException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BeanSetPropertyException(Exception e) {
		super(e);
	}
}
