package edu.buffalo.cse.irf14.analysis;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolFilter extends TokenFilter {

	public SymbolFilter(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean increment() throws TokenizerException {
		String []compression= {"ain't","aren't","can't","could've","couldn't","couldn't've","didn't","doesn't","don't","hadn't","hadn't've","hasn't","haven't","he'd","he'd've","he'll","he's","how'd","how'll","how's","I'd","I'd've","I'll","I'm","I've","isn't","it'd","it'd've","it'll","it's","let's","ma'am","mightn't","mightn't've","might've","mustn't","must've","needn't","not've","o'clock","shan't","she'd","she'd've","she'll","she's","should've","shouldn't","shouldn't've","that's","there'd","there'd've","there're","there's","they'd","they'd've","they'll","they're","they've","wasn't","we'd","we'd've","we'll","we're","we've","weren't","what'll","what're","what's","what've","when's","where'd","where's","where've","who'd","who'll","who're","who's","who've","why'll","why're","why's","won't","would've","wouldn't","wouldn't've","y'all","y'all'd've","you'd","you'd've","you'll","you're","you've" } ;
		String []decompression= {"am not","are not","cannot","could have","could not","could not have","did not","does not","do not","had not","had not have","has not","have not","he would","he would have","he will","he has","how did","how will","how has","I would","I would have","I will","I am","I have","is not","it would","it would have","it will","it has","let us","madam","might not","might not have","might have","must not","must have","need not","not have","of the clock","shall not","she would","she would have","she will","she has","should have","should not","should not have","that has","there would","there would have","there are","there has","they would","they would have","they will","they are","they have","was not","we would","we would have","we will","we are","we have","were not","what will","what are","what has","what have","when has","where did","where has","where have","who would","who will","who are","who has","who have","why will","why are","why has","will not","would have","would not","would not have","you all","you all should have","you would","you would have","you shall","you are","you have"};

		
		if(tStreamOld.hasNext()){
			
			Token token  = tStreamOld.next();
			String word  = token.getTermText().trim();
			String new_word;
			
			if(word.equals("'em")){
				new_word = word.replace("'em","them");
				token.setTermText(new_word);
				return tStreamOld.hasNext();
			}
				
			//remove punctuation from the end
			new_word = word.replaceAll("\\b[,.!?]+(?= |$)", "");
			new_word = new_word.replaceAll("/\\s{2,}/g", "");
			
			
			//remove hyphen from two words with space
			String pat = "^([A-Za-z]+)([-])([A-Za-z]+)$";
			Pattern pattern = Pattern.compile(pat);
			Matcher matcher = pattern.matcher(new_word);
			
			while(matcher.find()) {
				new_word = matcher.group(1)+" "+matcher.group(3);
			}
	
		
			//token.setTermText(new_word);
			if(!new_word.equals(word)){
				token.setTermText(new_word);
			}
			
			int index = Arrays.asList(compression).indexOf(new_word);
			int index_lower = Arrays.asList(compression).indexOf(new_word.toLowerCase());
			
			if(index>0){
				new_word = decompression[index];
			}else if(index_lower >0){
				new_word = decompression[index_lower];
				new_word = new_word.substring(0, 1).toUpperCase() + new_word.substring(1);
			}
			token.setTermText(new_word);
				
			
			
			//remove apostophe from end
			new_word = new_word.replaceAll("'s$|'$|'", "");
			
			
			//remove hyphen
			new_word = new_word.replaceAll("[^\\w]^-+[^\\w]|-*$|^-*", "");
			
			if(new_word.isEmpty())
				tStreamOld.remove();
			else
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
