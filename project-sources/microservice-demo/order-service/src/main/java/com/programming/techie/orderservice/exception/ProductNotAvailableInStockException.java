package com.programming.techie.orderservice.exception;

public class ProductNotAvailableInStockException extends RuntimeException {

	private static final long serialVersionUID = -8915800366302668336L;

	public ProductNotAvailableInStockException() {
		super();
	}

	public ProductNotAvailableInStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ProductNotAvailableInStockException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProductNotAvailableInStockException(String message) {
		super(message);
	}

	public ProductNotAvailableInStockException(Throwable cause) {
		super(cause);
	}

}
