package edu.buffalo.cse.irf14.analysis;

import java.util.Arrays;
import java.util.ArrayList;

public class CategoryAnalyzer implements Analyzer {
	TokenStream tStreamOld;
	TokenFilterFactory factory = TokenFilterFactory.getInstance();
	TokenFilter filter = null;

	public CategoryAnalyzer(TokenStream stream) {
		tStreamOld = stream;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean increment() throws TokenizerException {
		ArrayList<TokenFilterType> term = new ArrayList<TokenFilterType>(
				Arrays.asList(TokenFilterType.CAPITALIZATION));
		for (TokenFilterType tf : term) {
			filter = factory.getFilterByType(tf, tStreamOld);
			while (filter.increment()) {

			}
			tStreamOld.reset();
		}
		
		return false;
	}

	@Override
	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return tStreamOld;
	}
}