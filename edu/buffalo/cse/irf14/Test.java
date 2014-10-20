package edu.buffalo.cse.irf14;





import java.util.Stack;
import java.util.regex.PatternSyntaxException;

import edu.buffalo.cse.irf14.SearchRunner.ScoringModel;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Check c = new Check();
		String[] q_arr = {"hello","hello dear","\"pritika mehta\"","orange AND yellow","(black OR blue) AND bruises","Author:rushdie NOT jihad","Category:War AND Author:Dutt AND Place:Baghdad AND prisoners detainees rebels","(Love NOT War) AND Category:(movies NOT crime)"  };
		String []output={"{ Term:hello }","{ Term:hello OR Term:world }\n" ,"{ Term:”hello world” }","{ Term:orange AND Term:yellow }","{ [ Term:black OR Term:blue ] AND Term:bruises }","{ Author:rushdie AND <Term:jihad> }","{ Category:War AND Author:Dutt AND Place:Baghdad AND [ Term:prisoners OR Term:detainees OR Term:rebels ] }", "{ [ Term:Love AND <Term:War> ] AND [ Category:movies AND <Category:crime> ] }"};
		int i=0;
		//for(String raw :q_arr){
			System.out.println(q_arr[7]);
			c.getQueryFromString(q_arr[7],"OR");
			System.out.println(output[7]);
		//}	
	}

}

class Check{
	
	public void query(String userQuery, ScoringModel model) {
		//TODO: IMPLEMENT THIS METHOD
		String query = getQueryFromString(userQuery,"OR");
	}
	
	String getQueryFromString(String userQuery, String defaultOperator){
		String str = "";
		Stack<String> op_st = new Stack<String>();
		Stack<String> val_st = new Stack<String>();
		try {
			String[] splitArray = userQuery.split("\\s+");
			
			
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
						if(!word.contains(":"))
							temp_str =" Term:"+word;
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

	
	public String returnFinalQuery(Stack<String> val_st,Stack<String> op_st){
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
			System.out.println("----------------- "+final_val);
		}
		return final_val;
		
	}
	
	public String ANDop(String left,String right){
		return left+" AND "+right;
	}
	
	public String ORop(String left,String right){
		return left+" OR "+right;
	}
	
	public String NOTop(String left,String right){
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