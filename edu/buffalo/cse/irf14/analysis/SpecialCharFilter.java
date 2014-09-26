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
		Token token  = tStreamOld.next();
		String word  = token.getTermText().trim();
		
		String pat = "^([^A-Za-z0-9]+)$";
		Pattern pattern = Pattern.compile(pat);
		Matcher matcher = pattern.matcher(word);
		
		System.out.println("initially word was "+word);
		
		while(matcher.find()) {
			tStreamOld.remove();
			return tStreamOld.hasNext();
		}
		
		String new_word = word.replaceAll("^([^A-Za-z0-9-]+)|([^A-Za-z0-9]+)$", "");
		if(new_word.equals(word)){
			//System.out.println("dude they are equal new="+new_word+" old ="+word);
		}
		String oldWord = word;
		/*while(!new_word.equals(oldWord))
		{
			oldWord = new_word;
			new_word = new_word.replaceAll("^([^A-Za-z0-9])|([^A-Za-z0-9])$", "");
			System.out.println("new word is "+new_word);
		}*/	
		token.setTermText(new_word);
	
		return tStreamOld.hasNext();
	
		
		
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return tStreamOld;
	}

}
