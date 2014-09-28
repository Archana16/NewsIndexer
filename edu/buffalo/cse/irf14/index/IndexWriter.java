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

import edu.buffalo.cse.irf14.analysis.Analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import java.util.Map.Entry;
import java.util.ArrayList;

import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.analysis.TokenFilterType;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenFilterFactory;
import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;

import edu.buffalo.cse.irf14.document.ParserException;

import java.util.HashMap;

import edu.buffalo.cse.irf14.document.Parser;

/**
 * @author nikhillo Class responsible for writing indexes to disk
 */
public class IndexWriter implements java.io.Serializable {
	private static int termId = 1;
	int i =0;
	transient String indexDir;
	static int noOfDocs=0;
	private static TreeMap<String , Integer> termMap = new TreeMap<String, Integer>();
	private static TreeMap<Integer , Postings> TermIndex = new TreeMap<Integer , Postings>();
	private static TreeMap<Integer , Postings> AuthorIndex = new TreeMap<Integer , Postings>();
	private static TreeMap<Integer , Postings> CategoryIndex = new TreeMap<Integer , Postings>();
	private static TreeMap<Integer , Postings> PlaceIndex = new TreeMap<Integer , Postings>();
	
	/**
	 * Default constructor
	 * 
	 * @param indexDir
	 *            : The root directory to be sued for indexing
	 */
	
	public IndexWriter(String indexDir) {
		// TODO : YOU MUST IMPLEMENT THIS
		this.indexDir = indexDir;
	}
	
	public static int getNoOfDocs(){
		return noOfDocs;
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
	int id=0;
	public void addDocument(Document d) throws IndexerException {

		//with analyzer
		Tokenizer tknizer = new Tokenizer();
		AnalyzerFactory fact = AnalyzerFactory.getInstance();

		noOfDocs++;
			String docName = d.getField(FieldNames.FILEID)[0];
		for (FieldNames dir : FieldNames.values()) {
		
			try {
				TreeMap<Integer , Postings> CommonIndex ;
				if(dir.equals(FieldNames.AUTHOR)){
					CommonIndex = AuthorIndex;
				}else if(dir.equals(FieldNames.CATEGORY)){
					CommonIndex = CategoryIndex;
				}else if(dir.equals(FieldNames.PLACE)){
					CommonIndex = PlaceIndex;
				}else{
					CommonIndex = TermIndex;
				}
				
				//System.out.println("field for doc is "+d.getField(dir)[0]+" and dir is "+dir);
				if(dir == FieldNames.FILEID){
					//System.out.println("yes it is field id");
					continue;
					
				}
				//System.out.println("after continue  "+dir+" and val is "+d.getField(dir) );
				if(d.getField(dir) == null){
					//System.out.println("dude it is null for "+dir+" continuing");
					continue;
				}TokenStream stream = tknizer.consume(d.getField(dir)[0]);
				//System.out.println("stream is "+stream);
				if(stream.hasNext()){
					//System.out.println("yeh it has next");
				Analyzer analyzer = fact.getAnalyzerForField(dir, stream);
				while (analyzer.increment()) {
					
				}
				stream.reset();
				 while (stream.hasNext()){
						String term = stream.next().toString().trim();
						//System.out.println("word = "+term);
						if(!termMap.containsKey(term)){
							termMap.put(term,termId++);
							//System.out.println(id++ +" word = "+term);
						}else{
							//System.out.println("already had = "+term+" at= "+termMap.get(term));
						}	
						addDocumentToIndex(CommonIndex,termMap.get(term),d.getField(FieldNames.FILEID)[0]);
					}	
				}
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		//previous one
	/*	try {
			Tokenizer t = new Tokenizer();
			for (FieldNames dir : FieldNames.values()) {
					System.out.println(dir + " = " + d.getField(dir)[0]);
					TokenStream tstream = t.consume(d.getField(dir)[0]);
					TokenFilterFactory factory = TokenFilterFactory.getInstance();
					TokenFilter filter;

					
					  filter =factory.getFilterByType(TokenFilterType.DATE,tstream); 
					  tstream.reset(); 
					  while (tstream.hasNext()) {
					  filter.increment(); 
					  }
					  System.out.println("----------------after stopword------------");
					  tstream.reset(); 
					  while(tstream.hasNext())
						  System.out.println("next is "+tstream.next());
			}
		}catch (Exception e) {
				System.out.println("exception is " + e);

			}*/
				
	
	public int getTermNo(){
		return termId;
	}
	
	public void  addDocumentToIndex(TreeMap<Integer,Postings> IndType,int termId,String docId){
		//check if term_id exists
		if(IndType.containsKey(termId)){
			//increase term frequency 
			Postings  p = (Postings)IndType.get(termId);
			if(p.hasDoc(docId)){
				p.incrementExistingDoc(docId);
			}else{
				p.addDocument(docId);
			}
		}else{
			//create new key in final posting
			Postings p = new Postings();
			IndType.put(termId,p);
			p.addDocument(docId);
		}
		
	}
	
	
	
	public static TreeMap<String,Integer> getTermMap(){
		return termMap;
	} 
	
	
	
	public static TreeMap<Integer,Postings> getIndex(IndexType type){
		if(type.equals(IndexType.AUTHOR))
			return AuthorIndex;
		else if(type.equals(IndexType.CATEGORY))
			return CategoryIndex;
		else if(type.equals(IndexType.PLACE))
			return PlaceIndex;
		else
			return TermIndex;
		//return finalIndex;
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
		try{
			FileOutputStream fileOut = new FileOutputStream(new File(indexDir+ File.separator+"Author"));
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this.AuthorIndex);
			
			fileOut = new FileOutputStream(new File(indexDir+ File.separator+"Category"));
			out = new ObjectOutputStream(fileOut);
			out.writeObject(this.CategoryIndex);
			
			fileOut = new FileOutputStream(new File(indexDir+ File.separator+"Place"));
			out = new ObjectOutputStream(fileOut);
			out.writeObject(this.PlaceIndex);
			
			fileOut = new FileOutputStream(new File(indexDir+ File.separator+"Term"));
			out = new ObjectOutputStream(fileOut);
			out.writeObject(this.TermIndex);
			
			fileOut = new FileOutputStream(new File(indexDir+ File.separator+"TermMap"));
			out = new ObjectOutputStream(fileOut);
			out.writeObject(this.termMap);
			
			out.close();
			fileOut.close();
			
			//System.out.printf("Serialized data is saved ");
		}catch(IOException e){
			System.out.println("i have exception in close "+e);
		}
		
		
		
	}
}
