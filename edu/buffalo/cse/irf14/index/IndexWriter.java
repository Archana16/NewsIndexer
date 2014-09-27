/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenFilterFactory;
import edu.buffalo.cse.irf14.analysis.TokenFilterType;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo Class responsible for writing indexes to disk
 */
public class IndexWriter {
	/**
	 * Default constructor
	 * 
	 * @param indexDir
	 *            : The root directory to be sued for indexing
	 */
	public IndexWriter(String indexDir) {
		// TODO : YOU MUST IMPLEMENT THIS
	}

	public IndexWriter() {
		// TODO : YOU MUST IMPLEMENT THIS
	}

	/**
	 * Method to add the given Document to the index This method should take
	 * care of reading the filed values, passing them through corresponding
	 * analyzers and then indexing the results for each indexable field within
	 * the document.
	 * 
	 * @param d
	 *            : The Document to be added
	 * @throws IndexerException
	 *             : In case any error occurs
	 */
	public void addDocument(Document d) throws IndexerException {

		try {
			Tokenizer t = new Tokenizer();
			AnalyzerFactory AnalyzerInstance = AnalyzerFactory.getInstance();

			for (FieldNames dir : FieldNames.values()) {

					System.out.println(dir + " = " + d.getField(dir)[0]);
					TokenStream tstream = t.consume(d.getField(dir)[0]);
					//Analyzer obj = (Analyzer)AnalyzerInstance.getAnalyzerForField(dir, tstream);
					TokenFilterFactory factory = TokenFilterFactory
							.getInstance();
					TokenFilter filter;

					/*
					 * filter =
					 * factory.getFilterByType(TokenFilterType.STOPWORD,
					 * tstream); tstream.reset(); while (tstream.hasNext()) {
					 * filter.increment(); }
					 * 
					 * 
					 * System.out.println(
					 * "------------------------------after stopword-------------------------"
					 * ); tstream.reset(); while(tstream.hasNext())
					 * System.out.println("next is "+tstream.next());
					 * 
					 * 
					 * 
					 * filter = factory.getFilterByType(TokenFilterType.SYMBOL,
					 * tstream); tstream.reset(); while (tstream.hasNext()) {
					 * filter.increment(); } System.out.println(
					 * "------------------------------after symbol-------------------------"
					 * ); tstream.reset(); while(tstream.hasNext())
					 * System.out.println("next is "+tstream.next());
					 * 
					 * 
					 * tstream.reset();
					 * 
					 * filter = factory.getFilterByType(TokenFilterType.STEMMER,
					 * tstream); tstream.reset(); while (tstream.hasNext()) {
					 * filter.increment(); }
					 * 
					 * System.out.println(
					 * "------------------------------after stemmer-------------------------"
					 * ); tstream.reset(); while(tstream.hasNext())
					 * System.out.println("next is "+tstream.next());
					 * 
					 * 
					 * tstream.reset();
					 * 
					 * filter = factory.getFilterByType(TokenFilterType.ACCENT ,
					 * tstream); tstream.reset(); while (tstream.hasNext()) {
					 * filter.increment(); }
					 * 
					 * System.out.println(
					 * "------------------------------after accent-------------------------"
					 * );
					 * 
					 * tstream.reset(); while(tstream.hasNext())
					 * System.out.println("next is "+tstream.next());
					 * 
					 * tstream.reset();
					 */
					filter = factory.getFilterByType(TokenFilterType.DATE,
							tstream);
					tstream.reset();
					while (tstream.hasNext()) {
						filter.increment();
					}

					System.out
							.println("------------------------------after datefilter-------------------------");
					/*tstream.reset();
					while (tstream.hasNext())
						System.out.println("next is " + tstream.next());
					
				   filter = factory.getFilterByType(TokenFilterType.CAPITALIZATION, tstream);
					tstream.reset();
					while (tstream.hasNext()) {
						filter.increment();
						
					}
					
					
					System.out.println("------------------------------after capitalization-------------------------");
					tstream.reset();
					while(tstream.hasNext())
						System.out.println("next is "+tstream.next());
					*/
					filter = factory.getFilterByType(TokenFilterType.NUMERIC,
							tstream);
					tstream.reset();
					while (tstream.hasNext()) {
						filter.increment();
					}

					System.out.println("------------------------------after numeric filter-------------------------");
					while(tstream.hasNext())
						System.out.println("next is "+tstream.next());
			}

		} catch (Exception e) {
			System.out.println("exception is " + e);

		}

	}

	/**
	 * Method that indicates that all open resources must be closed and cleaned
	 * and that the entire indexing operation has been completed.
	 * 
	 * @throws IndexerException
	 *             : In case any error occurs
	 */
	public void close() throws IndexerException {
		// TODO
	}
}
