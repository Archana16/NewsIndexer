package edu.buffalo.cse.irf14.analysis;

import java.util.Arrays;
import java.util.ArrayList;

public class ContentAnalyzer implements Analyzer {
	TokenStream tStreamOld;
	TokenFilterFactory factory = TokenFilterFactory.getInstance();
	TokenFilter filter = null;

	public ContentAnalyzer(TokenStream stream) {
		tStreamOld = stream;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean increment() throws TokenizerException {
		ArrayList<TokenFilterType> term = new ArrayList<TokenFilterType>(
				Arrays.asList(TokenFilterType.ACCENT,
						TokenFilterType.CAPITALIZATION,TokenFilterType.DATE,
						TokenFilterType.NUMERIC, TokenFilterType.SYMBOL,
						TokenFilterType.SPECIALCHARS, TokenFilterType.STOPWORD,
						TokenFilterType.STEMMER));
		for (TokenFilterType tf : term) {
			filter = factory.getFilterByType(tf, tStreamOld);
			while (filter.increment()) {

			}
			tStreamOld.reset();
		}
		System.out.println("---After all filters for Content---");
		return false;
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return tStreamOld;
	}
}