/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The abstract class that you must extend when implementing your 
 * TokenFilter rule implementations.
 * Apart from the inherited Analyzer methods, we would use the 
 * inherited constructor (as defined here) to test your code.
 * @author nikhillo
 *
 */
public abstract class TokenFilter implements Analyzer {
	TokenStream tStreamOld; 
	
	/**
	 * Default constructor, creates an instance over the given
	 * 
	 * @param stream : The given TokenStream instance
	 */
	
	public TokenFilter(TokenStream stream) {
		//TODO : YOU MUST IMPLEMENT THIS METHOD
		tStreamOld = stream;
	
	}
}
