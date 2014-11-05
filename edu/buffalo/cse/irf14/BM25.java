package edu.buffalo.cse.irf14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;
import edu.buffalo.cse.irf14.index.IndexWriter;


class CalBM25{
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


public class BM25 {
	public static Postings p1 = new Postings();
	
	public static void main(String[] args) {
		String query = "{ [ Term:season OR Term:price ] AND Term:report }";
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
		Collection<String> Set = Collections.emptySet();
		str = str.replaceAll("[<,>]", "");
		String terms[] = str.split(":");
		IndexReader ir = p1.getReader(getType(terms[0]));
		Map<String, Integer> map = ir.getPostings(terms[1]);
		Set = map.keySet();
		CalBM25 bm = new CalBM25();
		int noOfDocs = Set.size();
		Iterator it = Set.iterator();
		
		for (Map.Entry<String, Integer> entry : map.entrySet()){
			String docName = entry.getKey();
			int freqInDoc = entry.getValue();
			int docLength = ir.getTotalFreqInDoc(docName);
			double averageDocLength= ir.getAverageDocLength();
			int N = ir.getTotalKeyTerms();
			bm.calculateScoreForDoc(docName,noOfDocs,freqInDoc,docLength,averageDocLength,N);

	}


		return Set;
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