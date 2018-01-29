package com.itv.kata.exceptions;

public class InvalidKataRequestException extends RuntimeException {
    /**
	 * Serial Version Id.
	 */
	private static final long serialVersionUID = 8851586315983274422L;
	
    private final String message;

    /**
     * @param message
     */
    public InvalidKataRequestException(String message) {
        this.message = message;
    }

    /**
     * @return
     */
    @Override
    public String getMessage() {
        return message;
    }
}

