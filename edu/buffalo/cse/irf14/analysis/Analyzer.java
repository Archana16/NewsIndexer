/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.HashMap;

import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo
 * This interface defines all {@link TokenStream} processing
 * It has two purposes:
 * 	- Either be implemented as a single {@link TokenFilter} instance
 *  - Or implemented as a TokenFilter chain that handles {@link FieldNames} specific processing
 */
public interface Analyzer {
	//public static HashMap<String, ArrayList<TokenFilterType>> map = new HashMap<String, ArrayList<TokenFilterType>>();
	
	/**
	 * Method to indicate that the implementing class must complete 
	 * all its processing on the current {@link Token} and proceed to next
	 * @return true if a Token exists in the stream that is unprocessed, 
	 * false if no more unprocessed tokens exist 
	 * @throws TokenizerException : If any exception occurs during the operation
	 */
	public abstract boolean increment() throws TokenizerException;
	
	/**
	 * Return the underlying {@link TokenStream} instance
	 * @return The underlying stream
	 */
	public abstract TokenStream getStream();
	
	//public abstract HashMap<String, ArrayList<TokenFilterType>> getMap();
}
