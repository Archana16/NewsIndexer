package edu.buffalo.cse.irf14.analysis;

public class SpecialChars extends TokenFilter {

	public SpecialChars(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean increment() throws TokenizerException {
		// TODO Auto-generated method stub

		Token token  = tStreamOld.next();
		String word  = token.getTermText().trim();
		String new_word = word.replaceAll("\\b[,.!?]+(?= |$)", "");
		token.setTermText(new_word);
		if(new_word.equals(word))
			return false;
		else
			return true;
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return tStreamOld;
	}

}
