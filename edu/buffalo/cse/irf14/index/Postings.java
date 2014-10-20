package edu.buffalo.cse.irf14.index;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public class Postings implements java.io.Serializable{
		
		private int docFrequency;
		private int termFrequency;
		private TreeMap<String , Integer> docToFreqMap;
		
		public Postings() {
			docFrequency = 0;
			termFrequency = 0;		
			docToFreqMap = new TreeMap<String, Integer>();
		}
		// TODO Auto-generated constructor stub

		public int getTermFrequency(){
			return termFrequency;
		}
		public int getDocFreq(){
			return docFrequency;
		}
		
		public void printDocmap(){
			//System.out.println("\tdoc frequency = "+docFrequency+" term frequency=" +termFrequency);
			Collection entrySet = docToFreqMap.entrySet();
			Iterator it = entrySet.iterator();
			 while(it.hasNext()){
		 		  // Postings p = (Postings) it.next();
		 	    	System.out.print("\t"+it.next()+" ");
		 	    	
		 	   }
			// System.out.println("\n--------------------------------------------------------------------------------------------------------------\n");
		}
		
		public TreeMap<String, Integer> getDocMap(){
			return docToFreqMap;
		}
		public boolean checkDocumentPresent(String docId){
			if(docToFreqMap.containsKey(docId))
				return true;
			else
				return false;
		}
	
		public void addDocument(String docId){
			
			docToFreqMap.put(docId,1) ;
			docFrequency++;
			termFrequency++;
				
		}
		
		public void incrementExistingDoc(String docId){
			docToFreqMap.put(docId,docToFreqMap.get(docId)+1);
			termFrequency++;
		}

		public boolean hasDoc(String docId){
			if(docToFreqMap.containsKey(docId))
				return true;
			else 
				return false;
		}
}
