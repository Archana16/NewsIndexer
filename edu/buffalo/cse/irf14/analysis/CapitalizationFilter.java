package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CapitalizationFilter extends TokenFilter {

	public CapitalizationFilter(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean increment() throws TokenizerException {
		// TODO Auto-generated method stub
		Token token  = tStreamOld.next();
		String word  = token.getTermText().trim();
		String line = word;
		Pattern pattern ;
		Matcher matcher;
		String rex;

		//check for first word
		if(!tStreamOld.hasPrevious()){
			token.setTermText(word.toLowerCase());
			return tStreamOld.hasNext();
		}
		
		
		//check if word is caps and whole sentence isnt caps
		int wordn =0;
		if(isUpperCase(word)){
			while(tStreamOld.hasPrevious()) {
				String previous = tStreamOld.previous().getTermText().trim();
				if(isUpperCase(previous)){
					Pattern pattern1 = Pattern.compile("\\w[.]$");
					Matcher matcher1 = pattern1.matcher(previous);
					if(matcher1.find()){
						break;
					}else{
						line = previous+ " "+ line;
						wordn++;
					}
				}else{
					tStreamOld.resetPrevious();
					return tStreamOld.hasNext();
				}
			}
		
			tStreamOld.setTempIndex();
			while(tStreamOld.hasNext()){
				
				String next = tStreamOld.next().getTermText().trim();
				if(isUpperCase(next)){
					Pattern pattern1 = Pattern.compile("\\w[.]$");
					Matcher matcher1 = pattern1.matcher(next);
					if(matcher1.find()){
						line = line+" "+next;
						break;
					}else{
						
						line = line+" "+next;
						wordn++;
					}
				}else{
					tStreamOld.setIndexCurrent();
					return tStreamOld.hasNext();
				}
				
			}
			tStreamOld.getIndex();
		
			if(isUpperCase(line) && wordn !=0){
					token.setTermText(word.toLowerCase());
					tStreamOld.resetPrevious();
					Pattern pattern1_inner = Pattern.compile("\\w[.]$");
					Matcher matcher1_inner;
					while(tStreamOld.hasPrevious()){
						Token token_inner = tStreamOld.previous();
						String previous_inner_word = token_inner.getTermText().trim();
						matcher1_inner = pattern1_inner.matcher(previous_inner_word);
						if(matcher1_inner.find()){
							tStreamOld.resetPrevious();
							break;
							
						}else
							token_inner.setTermText(previous_inner_word.toLowerCase());
						
					}
					
					tStreamOld.setIndexCurrent();
					while(tStreamOld.hasNext() ){
						
						Token token_inner = tStreamOld.next();
						String next_word = token_inner.getTermText().trim();
						matcher1_inner = pattern1_inner.matcher(next_word);
						if(matcher1_inner.find()){
							token_inner.setTermText(next_word.toLowerCase());
							break;
						}	
						else
							token_inner.setTermText(next_word.toLowerCase());
						}
					tStreamOld.resetPrevious();
				}else if(isUpperCase(line) && wordn ==0){
					token.setTermText(word.toLowerCase());
					tStreamOld.resetPrevious();
				}
			
				
			
		}
		
		
		//check for camelcase
		String pat = "[A-Z]([A-Z0-9]*[a-z][a-z0-9]*[A-Z]|[a-z0-9]*[A-Z][A-Z0-9]*[a-z])[A-Za-z0-9]*";
		pattern = Pattern.compile(pat);
		matcher = pattern.matcher(word);
		while(matcher.find()){
			int wordNumber = 0;
			while(tStreamOld.hasPrevious()) {
				String previous = tStreamOld.previous().getTermText().trim();
				Pattern pattern1 = Pattern.compile("\\w[.]$");
				Matcher matcher1 = pattern1.matcher(previous);
				if(matcher1.find() && wordNumber ==0){
					token.setTermText(word.toLowerCase());
					break;
				}else{
					wordNumber++;
				}	
			}
			tStreamOld.resetPrevious();
		}
		
		//check for san fransico
		rex = "^[A-Z][a-z]+";
		pattern = Pattern.compile(rex);
		matcher = pattern.matcher(word);
		Pattern pattern1 = Pattern.compile("\\w[.]$");
		Matcher matcher1 = pattern1.matcher(word);
		Matcher matcher_p;
		
			
		while(matcher.find() && !matcher1.find()){
			ArrayList<Token> tokensToMerge = new ArrayList<Token>();
			int i =0;
			int wordNumber = 0;
			 if(tStreamOld.hasPrevious()){
					String prev = tStreamOld.previous().getTermText().trim(); // dont remove it 
					String prev1 = tStreamOld.previous().getTermText().trim(); // dont removeit 
					Matcher m_inn = pattern1.matcher(prev1);
					if(m_inn.find()){
						token.setTermText(word.toLowerCase());
					}
			
			 }
			while(tStreamOld.hasNext()) {
				Token tokenAdd = tStreamOld.next();
				String next = tokenAdd.getTermText().trim();
				matcher = pattern.matcher(next);
				matcher1 = pattern1.matcher(next);
				if(matcher.find()){
					tokensToMerge.add(tokenAdd);
					tStreamOld.remove();
					wordNumber++;
					if(matcher1.find())
						break;
				}else{
					break;
				}	
			}
			if(wordNumber >0){
				Token[] tokenArr = new Token[tokensToMerge.size()];
				tokenArr = tokensToMerge.toArray(tokenArr);
				token.merge(tokenArr);
			}
			
		}
		
		
		return tStreamOld.hasNext();
		
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return tStreamOld;
	}
	
	public Boolean isUpperCase(String word){
		for(char c1 : word.toCharArray()) {
			if(Character.isLetter(c1) && Character.isLowerCase(c1)) 
				return false;
		  }
		return true;
	}
	
	

}
