/**

 * 
 */
package edu.buffalo.cse.irf14.analysis;

import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo This factory class is responsible for instantiating
 *         "chained" {@link Analyzer} instances
 */
public class AnalyzerFactory {
	/**
	 * Static method to return an instance of the factory class. Usually factory
	 * classes are defined as singletons, i.e. only one instance of the class
	 * exists at any instance. This is usually achieved by defining a private
	 * static instance that is initialized by the "private" constructor. On the
	 * method being called, you return the static instance. This allows you to
	 * reuse expensive objects that you may create during instantiation
	 * 
	 * @return An instance of the factory
	 */
	private static AnalyzerFactory instance;

	private AnalyzerFactory() {
		//System.out.println("AnalyzerFactory(): Initializing Instance");
	}

	public static AnalyzerFactory getInstance() {
		// TODO: YOU NEED TO IMPLEMENT THIS METHOD
		if (instance == null) {
			synchronized (AnalyzerFactory.class) {
				if (instance == null) {
					//System.out.println("getInstance(): First time getInstance was invoked!");
					instance = new AnalyzerFactory();
				}
			}
		}

		return instance;
	}

	/**
	 * Returns a fully constructed and chained {@link Analyzer} instance for a
	 * given {@link FieldNames} field Note again that the singleton factory
	 * instance allows you to reuse {@link TokenFilter} instances if need be
	 * 
	 * @param name
	 *            : The {@link FieldNames} for which the {@link Analyzer} is
	 *            requested
	 * @param TokenStream
	 *            : Stream for which the Analyzer is requested
	 * @return The built {@link Analyzer} instance for an indexable
	 *         {@link FieldNames} null otherwise
	 */

	public Analyzer getAnalyzerForField(FieldNames name, TokenStream stream) {
		// TODO : YOU NEED TO IMPLEMENT THIS METHOD
		Analyzer analyzer = null;
		switch (name) {
		case AUTHOR :
		case AUTHORORG :{
			analyzer = new AuthorAnalyzer(stream);
			break;
		}
		case PLACE: {
			analyzer = new PlaceAnalyzer(stream);
			break;
		}
		case CATEGORY: {
			analyzer = new CategoryAnalyzer(stream);
			break;
		}
		case NEWSDATE: {
			analyzer = new DateAnalyzer(stream);
			break;
		}
		default: {
			analyzer = new TermAnalyzer(stream);
			break;
		}
		}
		return analyzer;
	}
}
