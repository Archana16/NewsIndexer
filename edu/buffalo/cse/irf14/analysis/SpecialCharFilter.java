package edu.buffalo.cse.irf14.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialCharFilter extends TokenFilter {

	public SpecialCharFilter(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean increment() throws TokenizerException {
		// TODO Auto-generated method stub
		//System.out.println("Special Character filter function increment");
		if(tStreamOld.hasNext()){
			Token token  = tStreamOld.next();
			String word  = token.getTermText().trim();
			
			String pat = "^([^A-Za-z0-9]+)$";
			Pattern pattern = Pattern.compile(pat);
			Matcher matcher = pattern.matcher(word);
			//System.out.println("initially word was "+word);
	
			while(matcher.find()) {
				tStreamOld.remove();
				return tStreamOld.hasNext();
			}
			
			String new_word = word.replaceAll("^([^A-Za-z0-9-]+)|([^A-Za-z0-9-]+)$", "");
			pat = "([^0-9][+-,])";
			pattern = Pattern.compile(pat);
			matcher = pattern.matcher(new_word);
			while(matcher.find()) {
				new_word = word.replaceAll("[+-,]", "");
				
			}
			
			pat = "([A-Za-z.]+)([^A-Za-z])([A-Za-z.]+)";
			pattern = Pattern.compile(pat);
			matcher = pattern.matcher(new_word);
			
			while(matcher.find()) {
				new_word = matcher.group(1)+matcher.group(3);
			}
			
			new_word = new_word.replaceAll("[\\^*&\\+]", "");
			token.setTermText(new_word);
		}
		return tStreamOld.hasNext();
	
		
		
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return tStreamOld;
	}

}
