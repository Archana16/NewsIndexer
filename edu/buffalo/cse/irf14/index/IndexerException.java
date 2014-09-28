/**
 * 
 */
package edu.buffalo.cse.irf14.index;

/**
 * @author nikhillo
 * Generic wrapper exception class for indexing exceptions
 */
public class IndexerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3012675871474097239L;
	private String message = null;

	public IndexerException(String message) {
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
