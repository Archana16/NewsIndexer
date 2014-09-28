package edu.buffalo.cse.irf14.analysis;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;


public class AccentFilter extends TokenFilter {

	public AccentFilter(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean increment() throws TokenizerException {
		// TODO Auto-generated method stub
		System.out.println("Accent filter function increment");
		Token token  = tStreamOld.next();
		String word  = token.getTermText().trim();
		String new_word;
		
		//http://stackoverflow.com/questions/1008802/converting-symbols-accent-letters-to-english-alphabet
		    
		 String nfdNormalizedString = Normalizer.normalize(word, Normalizer.Form.NFKD); 
		 Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		 new_word = pattern.matcher(nfdNormalizedString).replaceAll("");
		//new_word = Normalizer.normalize(word, Normalizer.Form.NFD);
		//System.out.println("old word was "+word+" new word is "+new_word);
		if(new_word.isEmpty())
			tStreamOld.remove();
		else
			token.setTermText(new_word);
		
		return tStreamOld.hasNext();
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return tStreamOld;
	}
}
