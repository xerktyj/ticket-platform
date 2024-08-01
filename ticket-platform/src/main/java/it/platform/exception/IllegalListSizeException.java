package it.platform.exception;

public class IllegalListSizeException extends RuntimeException{
	
	public IllegalListSizeException() {
		super("la lista è vuota");
	}
	
	public IllegalListSizeException(String message) {
		super(message);
	}

}
