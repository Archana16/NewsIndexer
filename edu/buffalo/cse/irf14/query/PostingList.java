package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
		model = model;
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
	public void getList(){
		String terms[] = getQuery().split(" ");
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

public Collection<String> getPosting(String str) {
	Collection<String> Set = Collections.emptySet();
	str = str.replaceAll("[<,>]", "");
	System.out.println("terms->" + str);
	String terms[] = str.split(":");
	System.out.println("posting for " + terms[0] + "-" + terms[1]);
	Map<String, Integer> map = getIndexReader().getReader(getType(terms[0])).getPostings(
			terms[1]);
	Set = map.keySet();
	Iterator it = Set.iterator();
	System.out.println("posting list");
	while (it.hasNext()) {
		System.out.print("\t" + it.next() + " ");
	}
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
		}
	}else if(getModel() == ScoringModel.TFIDF){
		
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