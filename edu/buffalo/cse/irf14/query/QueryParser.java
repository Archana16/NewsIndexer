/**
 * 
 */
package edu.buffalo.cse.irf14.query;

import java.util.Stack;
import java.util.regex.PatternSyntaxException;


/**
 * @author nikhillo
 * Static parser that converts raw text to Query objects
 */
public class QueryParser {
	/**
	 * MEthod to parse the given user query into a Query object
	 * @param userQuery : The query to parse
	 * @param defaultOperator : The default operator to use, one amongst (AND|OR)
	 * @return Query object if successfully parsed, null otherwise
	 */
	public static Query parse(String userQuery, String defaultOperator) {
		//TODO: YOU MUST IMPLEMENT THIS METHOD
		String query = getQueryFromString(userQuery,"OR");
		Query q1 = new Query(query);
		return q1;
	}
	public static String getQueryFromString(String userQuery,String defaultOperator){
		String str = "";
		Stack<String> op_st = new Stack<String>();
		Stack<String> val_st = new Stack<String>();
		try {
			String[] splitArray = userQuery.trim().split("\\s+");
			
			
			int prev_has_quotes = 0;
			int prev_was_word = 0;
			int prev_got_left_brace=0;
			int prev_got_right_brace=0;
			int cat_has_started =0;
			int started_quote =0;
			int ended_quote =0;
			String temp_str="";
			String category ="";
			for(String word : splitArray){
				if(word.trim().equals("AND") || word.trim().equals("OR") || word.trim().equals("NOT")){
					prev_was_word = 0;
					op_st.push(word);
					if(prev_got_left_brace ==1){
						val_st.push(val_st.pop()+")");
						prev_got_right_brace=1;
						prev_got_left_brace =0;
					}	
				}else{
					//check for phrase query like "pritika mehta"
					if(word.matches("\"[A-Za-z0-9]+")){
						started_quote =1;
						System.out.println("word is "+word);
						if(!word.contains(":")){
							if(word.contains("{"))
								temp_str = "{Term:"+word.substring(word.indexOf("{")+1);
							else
								temp_str =" Term:"+word;
						}	
						else
							temp_str = word;
					}else if(word.matches("[A-Za-z0-9]+\"")){
						ended_quote =1;
						started_quote=0;
						temp_str = temp_str +" "+word;
						val_st.push(temp_str);
					}else if(started_quote == 1){
						temp_str = temp_str +" "+word;
					}else{
						//check for  terms like pritika mehta girl and convert it into (term:pritika OR term:mehta OR term:girl) 
						if(prev_was_word ==1 ){
							op_st.push(defaultOperator);
							if(prev_got_left_brace ==0){
								val_st.push(" ("+val_st.pop());
								prev_got_left_brace =1;
							}	
						}
						
						//check for terms like category:(pritika AND mehta) should not become category:(pritika or term:mehta)
						if(word.contains(":(")){
							cat_has_started =1;
							category = word.substring(0,word.indexOf(":"));
							System.out.println("categor is :"+category);
							word = word.replace(category+":(", "("+category+":");
							System.out.println("word is :"+word);
							val_st.push(word);
						}else{
							if(!word.contains(":") && cat_has_started ==0){
								if(word.contains("("))
									val_st.push("(Term:"+word.trim().substring(1));
								else
									val_st.push("Term:"+word);
							}else		
								val_st.push(category+":"+word);
						}
						if(word.contains(")") && cat_has_started ==1){
							cat_has_started =0;
							category ="";
						}	
						prev_was_word = 1;
					}
				}
			}
			if(prev_got_left_brace ==1 && prev_got_right_brace ==0)
				val_st.push(val_st.pop()+")");
			
			
		} catch (PatternSyntaxException e) {
		   System.out.println("exception in parse syntax expression "+e); 
		}
		
		
		return returnFinalQuery(val_st,op_st);
	}

	
	public static String returnFinalQuery(Stack<String> val_st,Stack<String> op_st){
		String  final_val="";
		while(!op_st.isEmpty()){
			String op = op_st.pop();
			if(op.equals("NOT")){
				//String first = val_st.pop();
				String right  = val_st.pop();
				String left  = val_st.pop();
				//System.out.println("right = "+right+"\n left ="+left);
				val_st.push(NOTop(left,right));
			}else if(op.equals("AND")){
				String right  = val_st.pop();
				String left  = val_st.pop();
				//System.out.println("right = "+right+"\n left ="+left);
				val_st.push(ANDop(left,right));
			}else{
				String right  = val_st.pop();
				String left  = val_st.pop();
				//System.out.println("right = "+right+"\n left ="+left);
				val_st.push(ORop(left,right));
			}
				
		}
		
		
		while(!val_st.isEmpty()){
			String po = val_st.pop();
			//System.out.println(" ols is :"+po);
			final_val = "{ "+po.replaceAll("\\(", "[ ").replaceAll("\\)", " ]")+" }";
			System.out.println("parsed query =  "+final_val);
		}
		return final_val;
		
	}
	
	public static String ANDop(String left,String right){
		return left+" AND "+right;
	}
	
	public static String ORop(String left,String right){
		return left+" OR "+right;
	}
	
	public static String NOTop(String left,String right){
		//if(right.matches("^[A_Za-z0-9]+\\)\\s"))
			String new_right;
			new_right = right.replaceFirst("\\)", ">\\)");
			if(new_right.equals(right)){
				//System.out.println("they are quals");
				 new_right = right.replaceFirst("\\s", "> ");
			}
			if(new_right.equals(right)){
				new_right = right+">";
			}
		return left+" AND <"+new_right;
	}
}

