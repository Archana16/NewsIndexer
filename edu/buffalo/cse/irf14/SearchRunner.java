package edu.buffalo.cse.irf14;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

import edu.buffalo.cse.irf14.query.QueryParser;
import edu.buffalo.cse.irf14.query.Query;
import edu.buffalo.cse.irf14.query.Reader;
import edu.buffalo.cse.irf14.query.PostingList;

import java.util.Collection;
import java.util.Collections;
/**
 * Main class to run the searcher.
 * As before implement all TODO methods unless marked for bonus
 * @author nikhillo
 *
 */
public class SearchRunner {
	public enum ScoringModel {TFIDF,OKAPI};
	
	/**
	 * Default (and only public) constuctor
	 * @param indexDir : The directory where the index resides
	 * @param corpusDir : Directory where the (flattened) corpus resides
	 * @param mode : Mode, one of Q or E
	 * @param stream: Stream to write output to
	 */
	PrintStream output;
	String indexDir;
	ScoringModel mod;
	 TreeMap <Double, String> FinalScore = new TreeMap<Double, String>();
	public SearchRunner(String indexDir, String corpusDir, 
			char mode, PrintStream stream) {
			output = stream;
			this.indexDir = indexDir;
			askUser(mode);
			int i =0;
			
			for (Map.Entry<Double, String> entry:FinalScore.entrySet()){
				
				String doc = entry.getValue();
				stream.println(doc);
				i++;
				if(i==10)
					break;
			}
			if(i==0)
				System.out.println("No document found");
			
		//TODO: IMPLEMENT THIS METHOD
	}
	
	public String getIndexDir(){
		return indexDir;
	}
	public void askUser(char mode){

		Scanner in = new Scanner(System.in);
		String query,mod;
		if(mode =='Q'){
			 
			 System.out.println("Enter a string");
		     query = in.nextLine();
		     System.out.println("Enter scoring model");
		     mod = in.nextLine();
		     while(mod.isEmpty()){
		    	 System.out.println("you need to enter a scoring model");
		    	 System.out.println("Enter scoring model");
			     mod = in.nextLine();
		     }
		     if(mod.equalsIgnoreCase("OKAPI"))
		    	 query(query,ScoringModel.OKAPI);
		     else
		    	 query(query,ScoringModel.TFIDF);
		}else{
			 File fileName;
			 System.out.println("Enter file name\n");
			 fileName = new File(in.nextLine());
			 System.out.println("Enter scoring model");
			   setModel(in.nextLine());
			   query(fileName);
			    
		}	
	}
	/**
	 * Method to execute given query in the Q mode
	 * @param userQuery : Query to be parsed and executed
	 * @param model : Scoring Model to use for ranking results
	 */
	public void query(String userQuery, ScoringModel model) {
		//TODO: IMPLEMENT THIS METHOD
		 QueryParser parser = new QueryParser();
	     Query Q = parser.parse(userQuery, "OR");
	   
	     Reader Readers = new Reader(getIndexDir());
	     PostingList DocumentList = new PostingList(Q.toString(), Readers, model);
	     Collection <String> FinalSet = DocumentList.getList();
	     HashMap<String, Double> scoreMap = new  HashMap<String, Double>();
	     if(model.equals(ScoringModel.TFIDF)){
	    	 scoreMap = DocumentList.getDocScore();
	     }
	     else if(model.equals(ScoringModel.OKAPI)){
	    	 scoreMap = DocumentList.getDocScoreMap(); 
	     }
	     
	    
	     
	     Iterator it1;
	 	it1 = FinalSet.iterator();
	 	while (it1.hasNext()) {
	 		String docName = (String)it1.next();
	 		Double score = scoreMap.get(docName);
	 		FinalScore.put(score, docName);
	 	}
	    		 
	 	
	}
	
	
	public void setModel(String mod){
		if(mod.equals("TFIDF")){
			this.mod = ScoringModel.TFIDF;
		}else if(mod.equals("OKAPI")){
			this.mod = ScoringModel.OKAPI;
		}
	}
	public ScoringModel getModel(){
		return mod;
	}
	
	/**
	 * Method to execute queries in E mode
	 * @param queryFile : The file from which queries are to be read and executed
	 */
	public void query(File queryFile) {
		//TODO: IMPLEMENT THIS METHOD
		try{
			String str = ""; 
			QueryParser parser = new QueryParser();
			FileReader reader = new FileReader(queryFile);
			BufferedReader in = new BufferedReader(reader);
			while ((str = in.readLine()) != null) {
				Query Q = parser.parse(str, "OR");
				query(Q.toString(),getModel());
				
			}
		}catch(Exception e){
			
		}
		
	}
	
	/**
	 * General cleanup method
	 */
	public void close() {
		//TODO : IMPLEMENT THIS METHOD
	}
	
	/**
	 * Method to indicate if wildcard queries are supported
	 * @return true if supported, false otherwise
	 */
	public static boolean wildcardSupported() {
		//TODO: CHANGE THIS TO TRUE ONLY IF WILDCARD BONUS ATTEMPTED
		return false;
	}
	
	/**
	 * Method to get substituted query terms for a given term with wildcards
	 * @return A Map containing the original query term as key and list of
	 * possible expansions as values if exist, null otherwise
	 */
	public Map<String, List<String>> getQueryTerms() {
		//TODO:IMPLEMENT THIS METHOD IFF WILDCARD BONUS ATTEMPTED
		return null;
		
	}
	
	/**
	 * Method to indicate if speel correct queries are supported
	 * @return true if supported, false otherwise
	 */
	public static boolean spellCorrectSupported() {
		//TODO: CHANGE THIS TO TRUE ONLY IF SPELLCHECK BONUS ATTEMPTED
		return false;
	}
	
	/**
	 * Method to get ordered "full query" substitutions for a given misspelt query
	 * @return : Ordered list of full corrections (null if none present) for the given query
	 */
	public List<String> getCorrections() {
		//TODO: IMPLEMENT THIS METHOD IFF SPELLCHECK EXECUTED
		return null;
	}
}
