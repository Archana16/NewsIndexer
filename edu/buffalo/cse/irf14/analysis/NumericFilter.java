package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;

public class NumericFilter extends TokenFilter {

	public NumericFilter(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean increment() throws TokenizerException {
		//System.out.println("numeric filter function increment");
		while (tStreamOld.hasNext()) {
			String word = tStreamOld.next().getTermText();
			String temp = "";
			//System.out.println("before: " + word);
			if (word.matches("^(-)?\\d{8}(-\\d{8})?$")) {
				System.out.println("it is a date");
			} else {
				temp = word.replaceAll("\\d", "");
				temp = temp.replaceAll("[.,]", "");
			}
			//System.out.println("after: " + temp);
			if (word.charAt(word.length() - 1) == ',')
				temp = temp + ",";
			if (word.charAt(word.length() - 1) == '.')
				temp = temp + ".";
			if (!word.equals(temp)) {
				if (temp.equals(""))
					tStreamOld.replaceTokens(6, temp);
				else
					tStreamOld.replaceTokens(4, temp);

			}

		}

		return tStreamOld.hasNext();
	}

	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return tStreamOld;
	}
}