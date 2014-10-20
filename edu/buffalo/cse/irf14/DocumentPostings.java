package edu.buffalo.cse.irf14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.HashMap;
import java.lang.Math;

import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;

class Postings {

	private IndexReader AuthorIndex;
	private IndexReader PlaceIndex;
	private IndexReader CategoryIndex;
	private IndexReader TermIndex;
	private static String indexDir;

	public Postings() {
		System.out.printf("creating all readers");
		indexDir = "/home/pritika/Downloads/dfl";
		AuthorIndex = new IndexReader(indexDir, IndexType.AUTHOR);
		PlaceIndex = new IndexReader(indexDir, IndexType.PLACE);
		CategoryIndex = new IndexReader(indexDir, IndexType.CATEGORY);
		TermIndex = new IndexReader(indexDir, IndexType.TERM);
		System.out.printf("exiting");
	}

	public IndexReader getReader(IndexType type) {
		switch (type) {
		case AUTHOR:
			return AuthorIndex;
		case PLACE:
			return PlaceIndex;
		case CATEGORY:
			return CategoryIndex;
		default:
			return TermIndex;
		}
	}
}

public class DocumentPostings {
	public static Postings p1 = new Postings();
	public static HashMap<String, Map<String, Integer>> TermDoc = new  HashMap<String, Map<String, Integer>>();


	public static void main(String[] args) {
		String query = "{ [ Term:season OR Term:price ] AND <Term:report> }";
		String terms[] = query.split(" ");
		// Stack for terms
		Stack<Collection<String>> values = new Stack<Collection<String>>();
		// Stack for Operators
		Stack<String> operation = new Stack<String>();
		ArrayList<String> operatorList = new ArrayList<String>(Arrays.asList(
				"OR", "AND", "NOT"));
		for (int i = 0; i < terms.length; i++) {
			System.out.print(terms[i] + "\n");
		}
		for (int i = 0; i < terms.length; i++) {
			System.out.print("\n----start---"+terms[i] + "\n");
			if (terms[i].equals("{") || terms[i].equals("[")) {
				System.out.print("opening bracket"+terms[i]+"\n");
				operation.push(terms[i]);
			} else if (terms[i].equals("}")) {
				System.out.print("closing bracket"+terms[i]+"\n");
				while (!operation.peek().equals("{")){
					values.push(applyOp(operation.pop(), values.pop(),values.pop()));
				}
				operation.pop();
			} else if (terms[i].equals("]")) {
				System.out.print("closing bracket"+terms[i]+"\n");
				while (!operation.peek().equals("[")){
					values.push(applyOp(operation.pop(), values.pop(),values.pop()));
				}
				operation.pop();
			}else if (!operatorList.contains(terms[i].toUpperCase())) {
				System.out.print("an operand, so push into values stack\n");
				
				values.push(getPosting(terms[i]));
			} else if (operatorList.contains(terms[i].toUpperCase())) {
				System.out.print("operator, push onto operator stack\n");
				operation.push(terms[i]);
			}
			System.out.print("\n----finish---"+terms[i] +" index"+i+"\n");
		}
		  while (!operation.empty())
	            values.push(applyOp(operation.pop(), values.pop(), values.pop()));
		values.pop();
		getSquareRootOfEufactor();
		calculateTheFinalVal();
		for (Map.Entry<String,Double> entry :docScore.entrySet()){
			System.out.println(" val is  "+entry.getKey()+" ="+entry.getValue());
		}	
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

	public static Collection<String> getPosting(String str) {
		Collection Set = Collections.emptySet();
		str = str.replaceAll("[<,>]", "");
		System.out.println("terms->" + str);
		String terms[] = str.split(":");
		System.out.println("posting for " + terms[0] + "-" + terms[1]);
		Map<String, Integer> map = p1.getReader(getType(terms[0])).getPostings(terms[1]); 
		p1.getReader(getType(terms[0])).populateTfIdf(terms[1]);
		Set = map.entrySet();
		Iterator it = Set.iterator();
		System.out.println("posting list");
		while (it.hasNext()) {
			System.out.print("\t" + it.next() + " ");
		}
		int sum =0;
		for (String key : map.keySet()) {
		    System.out.println(key + ":" + map.get(key));
		}
		
		for (Map.Entry<String, Integer> entry:map.entrySet()){
			String docName = entry.getKey();
			int freqInDoc = entry.getValue();
			setEucaladianFactorForDoc(docName,freqInDoc);
		}
		TermDoc.put(str, map);
		return Set;
	}
	
	
	
	private static HashMap<String,Double> EuFactor = new HashMap<String,Double>();
	private static void setEucaladianFactorForDoc(String docName,double freqInDoc){
		if(EuFactor.containsKey(docName)){
			EuFactor.put(docName, EuFactor.get(docName)+(freqInDoc*freqInDoc));
		}else
			EuFactor.put(docName, freqInDoc);
	}
	
	private static void getSquareRootOfEufactor(){
		for (Map.Entry<String, Double> entry:EuFactor.entrySet()){
			EuFactor.put(entry.getKey(), Math.sqrt(entry.getValue()));
		}
	}
	
	private static HashMap <String,Double> docScore= new HashMap<String,Double>();
	
	public static void calculateTheFinalVal(){
		for (Map.Entry<String,Map<String,Integer>> entry :TermDoc.entrySet()){
			String terms[] = entry.getKey().split(":");
			double idf = p1.getReader(getType(terms[0])).getIdfForTerm(terms[1]);
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
		for (String key : TermDoc.keySet()) {
		    System.out.println(key + ":" + TermDoc.get(key));
		    for(String term: terms){
		    	
		    }
		}
		/*sum+=Math.pow(Integer.parseInt(map.get(key).toString()),2);
		Double Ed = Math.sqrt(sum);*/
	}

	public static Collection<String> applyOp(String op, Collection<String> s1,
			Collection<String> s2) {
		Iterator it1,it2,it3;
		it1 = s1.iterator();
		int count =0;
		System.out.println("\nfirst set\n");
		while (it1.hasNext()) {
			count++;
			System.out.print("\t" + it1.next() + " ");
		}
		System.out.print("\ncount ="+count+"\n");
		count =0;
		it2 = s2.iterator();
		System.out.println("\nsecond set\n");
		while (it2.hasNext()) {
			count++;
			System.out.print("\t" + it2.next() + " ");
		}
		System.out.print("\ncount ="+count+"\n");
		count =0;
		if (op.equals("OR")) {
			Set <String> union = new HashSet<String>(s1);
			union.addAll(s2);
		} else if (op.equals("AND")) {
			s1.retainAll(s2);
		} else if (op.equals("NOT")) {
			s1.removeAll(s2);
		} else {
			System.out.println("Invalid operator");
			return null;
		}
		it3 = s1.iterator();
		System.out.println("\nresult set\n");
		while (it3.hasNext()) {
			count++;
			System.out.print("\t" + it3.next() + " ");
		}
		System.out.print("\ncount ="+count+"\n");
		count =0;
		return s1;
	}
}