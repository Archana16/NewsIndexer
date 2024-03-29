/**
 * 
 */
package edu.buffalo.cse.irf14.document;

/**
 * @author nikhillo Generic wrapper exception class for parsing exceptions
 */
public class ParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4691717901217832517L;
	private String message = null;

	public ParserException(String message) {
		super(message);
		this.message = message;
	}

	public String toString() {
		return message;
	}

	public String getMessage() {
		return message;
	}
}