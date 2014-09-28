/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import edu.buffalo.cse.irf14.Runner;
import edu.buffalo.cse.irf14.analysis.Analyzer;
import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.document.Parser;

/**
 * @author nikhillo
 * Class that emulates reading data back from a written index
 */
public class IndexReader {
	
	private TreeMap<Integer , Postings> map;
	private static TreeMap<String , Integer> termMap ;
	//private static TreeMap<String , Integer> docMap;
	//private static TreeMap<Integer,String> reverseDocMap ;
	
	/**
	 * Default constructor
	 * @param indexDir : The root directory from which the index is to be read.
	 * This will be exactly the same directory as passed on IndexWriter. In case 
	 * you make subdirectories etc., you will have to handle it accordingly.
	 * @param type The {@link IndexType} to read from
	 */
	public IndexReader(String indexDir, IndexType type) {
		//TODO
		FileInputStream file,fileInMain; 
		ObjectInputStream in,inMain; 
		String fileName;
		System.out.println("index dir i got is"+indexDir);
		//get term and doc dictionary
		try{
			file = new FileInputStream(indexDir+ File.separator+"TermMap");
			in = new ObjectInputStream(file);
			termMap = (TreeMap<String,Integer>)in.readObject();
			
			
			
			in.close();
		}catch(Exception e){
			System.out.println("exception reading dictionaries from map");
		}
		
		 switch (type) {
	         case TERM: fileName = "Term";break;
	         case AUTHOR: fileName = "Author";break;
	         case CATEGORY:fileName = "Category"; break;
	         case PLACE:fileName = "Place"; break;
	         default: fileName = "Term";break;
		 	}
		
		 try{
			fileInMain = new FileInputStream(indexDir+ File.separator+fileName);
			inMain = new ObjectInputStream(fileInMain);
			try{
				int i =0;
				map= (TreeMap<Integer,Postings>)inMain.readObject();
				for (Entry<Integer,Postings> entry : map.entrySet()) {
				 	System.out.println(i++ +"  key was "+entry.getKey());
				}	
			}catch(ClassNotFoundException e){
				System.out.println("out obje"+e);
			}
			inMain.close();
		}catch(Exception e){
			System.out.println("out obje"+e);
		}
		
		
		
		
	}
	
	/**
	 * Get total number of terms from the "key" dictionary associated with this 
	 * index. A postings list is always created against the "key" dictionary
	 * @return The total number of terms
	 */
	public int getTotalKeyTerms() {
		//TODO : YOU MUST IMPLEMENT THIS
		System.out.println("--------------------------total key terms="+map.size());
		return map.size();
	}
	
	/**
	 * Get total number of terms from the "value" dictionary associated with this 
	 * index. A postings list is always created with the "value" dictionary
	 * @return The total number of terms
	 */
	public int getTotalValueTerms() {
		//TODO: YOU MUST IMPLEMENT THIS
		System.out.println("-------total val terms ="+IndexWriter.getNoOfDocs());
		return IndexWriter.getNoOfDocs();
	}
	
	/**
	 * Method to get the postings for a given term. You can assume that
	 * the raw string that is used to query would be passed through the same
	 * Analyzer as the original field would have been.
	 * @param term : The "analyzed" term to get postings for
	 * @return A Map containing the corresponding fileid as the key and the 
	 * number of occurrences as values if the given term was found, null otherwise.
	 */
	public Map<String, Integer> getPostings(String term) {
		String query = getAnalyzedTerm(term);
		System.out.println("query is "+query);
		if(termMap.containsKey(query)){
			Postings p = map.get(termMap.get(term));
			Map<String,Integer> postingMap = p.getDocMap();
			return postingMap;
		}
		
		return null;
	}
	
	
	private static String getAnalyzedTerm(String string) {
		Tokenizer tknizer = new Tokenizer();
		AnalyzerFactory fact = AnalyzerFactory.getInstance();
		try {
			TokenStream stream = tknizer.consume(string);
			Analyzer analyzer = fact.getAnalyzerForField(FieldNames.CONTENT, stream);
			
			while (analyzer.increment()) {
				
			}
			
			stream.reset();
			return stream.next().toString();
		} catch (TokenizerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Method to get the top k terms from the index in terms of the total number
	 * of occurrences.
	 * @param k : The number of terms to fetch
	 * @return : An ordered list of results. Must be <=k fr valid k values
	 * null for invalid k values
	 */
	public List<String> getTopK(int k) {
		//TODO YOU MUST IMPLEMENT THIS
		return null;
	}
	
	/**
	 * Method to implement a simple boolean AND query on the given index
	 * @param terms The ordered set of terms to AND, similar to getPostings()
	 * the terms would be passed through the necessary Analyzer.
	 * @return A Map (if all terms are found) containing FileId as the key 
	 * and number of occurrences as the value, the number of occurrences 
	 * would be the sum of occurrences for each participating term. return null
	 * if the given term list returns no results
	 * BONUS ONLY
	 */
	public Map<String, Integer> query(String...terms) {
		//TODO : BONUS ONLY
		return null;
	}
}
