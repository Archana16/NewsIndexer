package edu.buffalo.cse.irf14.query;

import java.util.HashMap;

public class CalBM25 {

	public CalBM25() {
		// TODO Auto-generated constructor stub
	}
	private HashMap<String , Double> docScoreMap = new HashMap<String , Double>();
	public void calculateScoreForDoc(String doc,int noOfDocs,int freqInDoc,int docLength,double averageDocLength,int N){
		double val = (N-noOfDocs+0.5)/(noOfDocs+0.5);
		double idf = Math.log10(val);
		double k = 1.6;
		double b =0.75;
		double  score = idf *((freqInDoc*(k+1))/(freqInDoc+k*(1-b+(b*docLength)/averageDocLength))); 
		System.out.println("doc is "+doc+" score is "+score);
		insertInDocScoremap(doc,score);
	}
	
	public void insertInDocScoremap(String doc,double score){
		if(docScoreMap.containsKey(doc))
			docScoreMap.put(doc, score+docScoreMap.get(doc));
		else	
			docScoreMap.put(doc, score);
		
	}
	public HashMap<String , Double> getDocToScoreMap(){
		return docScoreMap;
	}


}
