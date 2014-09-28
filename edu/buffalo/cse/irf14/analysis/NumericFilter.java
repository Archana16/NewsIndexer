package edu.buffalo.cse.irf14.analysis;

public class NumericFilter extends TokenFilter {

	public NumericFilter(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean increment() throws TokenizerException {
		//System.out.println("Numeric filter function increment");
		while (tStreamOld.hasNext()) {
			String word = tStreamOld.next().getTermText();
			String temp = "";
			if(!word.isEmpty()){
				if (word.matches("^(-)?\\d{8}(-\\d{8})?$")) {
					//System.out.println("it is a date");
				} else if(word.matches("^\\d+-\\d+$")){
					//System.out.println("Should not edit");
				}else {
					temp = word.replaceAll("\\d", "");
					temp = temp.replaceAll("[.,]", "");
				}
				if (word.length() >1 && word.charAt(word.length() - 1) == ',')
					temp = temp + ",";
				if (word.length() >1 && word.charAt(word.length() - 1) == '.')
					temp = temp + ".";
				if (!word.equals(temp)) {
					if (temp.equals(""))
						tStreamOld.replaceTokens(6, temp);
					else
						tStreamOld.replaceTokens(4, temp);

				}	
			}
		}

		return tStreamOld.hasNext();
	}

	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return tStreamOld;
	}
}