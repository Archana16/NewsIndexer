package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import edu.buffalo.cse.irf14.query.CalBM25;
import edu.buffalo.cse.irf14.SearchRunner.ScoringModel;
import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;

public class PostingList {

	private String query;
	private Reader p1;
	private ScoringModel model;
	public PostingList(String q1, Reader r1,ScoringModel model) {
		// TODO Auto-generated constructor stub
		query = q1;
		p1= r1;
		this.model = model;
	}
	public String getQuery(){
		return query;
	}
	public Reader getIndexReader(){
		return p1;
	}
	public ScoringModel getModel(){
		return model;
	}
	
	public static HashMap<String, Map<String, Integer>> TermDoc = new  HashMap<String, Map<String, Integer>>();
	private HashMap<String , Double> docScoreMap = new HashMap<String , Double>();
	public Collection<String> getList(){
		String str = getQuery().replaceAll("AND\\s<", "NOT ");
		str = str.replaceAll("[>]", "");
		String terms[] = str.split(" ");
		// Stack for terms
		Stack<Collection<String>> values = new Stack<Collection<String>>();
		// Stack for Operators
		Stack<String> operation = new Stack<String>();
		ArrayList<String> operatorList = new ArrayList<String>(Arrays.asList(
				"OR", "AND", "NOT"));
		
		for (int i = 0; i < terms.length; i++) {
			if (terms[i].equals("{") || terms[i].equals("[")) {
				//System.out.print("opening bracket"+terms[i]+"\n");
				operation.push(terms[i]);
			} else if (terms[i].equals("}")) {
				//System.out.print("closing bracket"+terms[i]+"\n");
				while (!operation.peek().equals("{")){
					values.push(applyOp(operation.pop(), values.pop(),values.pop()));
				}
				operation.pop();
			} else if (terms[i].equals("]")) {
				//System.out.print("closing bracket"+terms[i]+"\n");
				while (!operation.peek().equals("[")){
					values.push(applyOp(operation.pop(), values.pop(),values.pop()));
				}
				operation.pop();
			}else if (!operatorList.contains(terms[i].toUpperCase())) {
				//System.out.print("an operand, so push into values stack\n");
				if(getPosting(terms[i])!= null)
					values.push(getPosting(terms[i]));
			} else if (operatorList.contains(terms[i].toUpperCase())) {
				//System.out.print("operator, push onto operator stack\n");
				operation.push(terms[i]);
			}
			//System.out.print("\n----finish---"+terms[i] +" index"+i+"\n");
		}
		  while (!operation.empty())
	            values.push(applyOp(operation.pop(), values.pop(), values.pop()));
		  
		  if(getModel() == ScoringModel.TFIDF){
			  //System.out.println("i am in tfidf mode!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
				getSquareRootOfEufactor();
				calculateTheFinalVal();
			} 
		return values.pop();

		
		
	}


public static IndexType getType(String type) {
	if (type.equals("Term"))
		return IndexType.TERM;
	else if (type.equals("Author"))
		return IndexType.AUTHOR;
	else if (type.equals("Category"))
		return IndexType.CATEGORY;
	else if (type.equals("Place"))
		return IndexType.PLACE;
	else
		return null;

}

public Collection<String> getPosting(String str) {
	Collection<String> Set = Collections.emptySet();
	
	if(!str.isEmpty()){
		
		String terms[] = str.split(":");
		//System.out.println("posting for " + terms[0] + "-" + terms[1]);
		getIndexReader().getReader(getType(terms[0])).populateTfIdf(terms[1]);
		Map<String, Integer> map = getIndexReader().getReader(getType(terms[0])).getPostings(
				terms[1]);
		if(map != null){
			Set = map.keySet();
			Iterator it = Set.iterator();
			
			if(getModel() == ScoringModel.OKAPI){
				IndexReader ir = getIndexReader().getReader(getType(terms[0]));
				CalBM25 bm = new CalBM25();
				int noOfDocs = Set.size();
				for (Map.Entry<String, Integer> entry : map.entrySet()){
					String docName = entry.getKey();
					int freqInDoc = entry.getValue();
					int docLength = ir.getTotalFreqInDoc(docName);
					double averageDocLength= ir.getAverageDocLength();
					int N = ir.getTotalKeyTerms();
					bm.calculateScoreForDoc(docName,noOfDocs,freqInDoc,docLength,averageDocLength,N);
					docScoreMap = bm.getDocToScoreMap();
				}
			}else if(getModel() == ScoringModel.TFIDF){
				
				for (Map.Entry<String, Integer> entry:map.entrySet()){
					String docName = entry.getKey();
					int freqInDoc = entry.getValue();
					setEucaladianFactorForDoc(docName,freqInDoc);
				}
				TermDoc.put(str, map);
			}
		}
	}	
	return Set;
}


public HashMap<String, Double> getDocScore(){
	return docScore;
}
public HashMap<String, Double> getDocScoreMap(){
	return docScoreMap;
}
private static HashMap<String,Double> EuFactor = new HashMap<String,Double>();

private static void setEucaladianFactorForDoc(String docName,double freqInDoc){
	//System.out.println("setting------------------");
	if(EuFactor.containsKey(docName)){
		EuFactor.put(docName, EuFactor.get(docName)+(freqInDoc*freqInDoc));
	}else
		EuFactor.put(docName, freqInDoc);
}

private static void getSquareRootOfEufactor(){
	//System.out.println("getting square root ------------------");
	for (Map.Entry<String, Double> entry:EuFactor.entrySet()){
		EuFactor.put(entry.getKey(), Math.sqrt(entry.getValue()));
	}
}

private HashMap <String,Double> docScore= new HashMap<String,Double>();

public  void calculateTheFinalVal(){
	//System.out.println("calculating final score------------------");
	for (Map.Entry<String,Map<String,Integer>> entry :TermDoc.entrySet()){
		String terms[] = entry.getKey().split(":");
		double idf = getIndexReader().getReader(getType(terms[0])).getIdfForTerm(terms[1]);
		Map<String,Integer> docMap = entry.getValue();
		for (Map.Entry<String, Integer> entry1: docMap.entrySet()){
			String docId = entry1.getKey();
			double getEuFactorForDoc = EuFactor.get(docId);
			double tfIdf = (entry1.getValue()/getEuFactorForDoc)*entry1.getValue()*idf;
			if(docScore.containsKey(docId))
				docScore.put(docId,docScore.get(docId)+tfIdf);
			else	
				docScore.put(docId,tfIdf);
			
		}
		
	}
}

public static void normalizeDoc(String[] terms){
	/*for (String key : TermDoc.keySet()) {
	    System.out.println(key + ":" + TermDoc.get(key));
	    for(String term: terms){
	    	
	    }
	}*/
	/*sum+=Math.pow(Integer.parseInt(map.get(key).toString()),2);
	Double Ed = Math.sqrt(sum);*/
}

public static Collection<String> applyOp(String op, Collection<String> s1,
		Collection<String> s2) {
	Iterator it1,it2,it3;
	it1 = s1.iterator();
	int count =0;
	it2 = s2.iterator();
	//System.out.println("\nsecond set\n");
	
	count =0;
	if (op.equals("OR")) {
		Set <String> union = new HashSet<String>(s1);
		union.addAll(s2);
	} else if (op.equals("AND")) {
		s1.retainAll(s2);
	} else if (op.equals("NOT")) {
		if(it2.hasNext() && it1.hasNext()){
			s2.removeAll(s1);
			s1 = s2;
		}else if(it2.hasNext())
			s1 = s2;
		
	} else {
		System.out.println("Invalid operator");
		return null;
	}
	it3 = s1.iterator();
	count =0;
	return s1;
}
}