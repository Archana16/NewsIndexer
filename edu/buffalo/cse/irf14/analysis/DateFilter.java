package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.Arrays;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateFilter extends TokenFilter {

	public DateFilter(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean increment() throws TokenizerException {
		// TODO Auto-generated method stub

		System.out.println("date filter function increment");
		ArrayList<String> list = new ArrayList<String>(Arrays.asList("january",
				"jan", "febuary", "feb", "march", "april", "may", "june",
				"july", "august", "aug", "september", "sep", "october", "oct",
				"november", "nov", "december", "dec"));
		String cur = "", next1 = "",next2 ="", prev = "", year = "", month = "", date = "";
		/*Token t =  tStreamOld.next();
		String word = t.getTermText().trim();*/
		//System.out.println("word is "+word);

		while (tStreamOld.hasNext()) {
			cur = (tStreamOld.hasNext())?tStreamOld.next().getTermText():"";
			next1 = (tStreamOld.hasNext())?tStreamOld.next().getTermText():"";
			next2 = (tStreamOld.hasNext())?tStreamOld.getCurrent().getTermText():"";
			prev = (tStreamOld.hasNext())?tStreamOld.getTokenByIndex((tStreamOld.getIndex()-3)).getTermText():"";
			//System.out.println(cur + " " + next1 + " " + next2+" "+prev);
		if (list.contains(cur.toLowerCase())) {
				month = cur;
				if (prev.matches("^\\d{1,2}$"))
					date =prev;
				if (next1.matches("^\\d{1,2}$"))
					date = next1;
				if(next1.matches("^(\\d{2})(\\d{2})?$"))
					year = next1;
				if(next2.matches("^(\\d{2})(\\d{2})?$"))
					year = next2;
				System.out.println("date:" + month + " " + year + " " + date);
				try {
					Date newDate = new SimpleDateFormat("yyyy,MMMM,dd").parse(year
							+ "," + month + "," + date);
					String reportDate = new SimpleDateFormat("yyyyMMdd")
							.format(newDate);
					System.out.println(reportDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}

		/*
		 * String[] mock =
		 * ("Vidya Balan born 1 January 1978 is an Indian actress") .split(" ");
		 * for (int i = 0; i < mock.length; i++) { if
		 * (list.contains(mock[i].toLowerCase())) { month = mock[i]; if (mock[i
		 * - 1].matches("^\\d{1,2}$")) date = mock[i - 1]; else if (mock[i -
		 * 1].matches("^(\\d{2})(\\d{2})?$")) year = mock[i - 1];
		 * 
		 * if (mock[i + 1].matches("^\\d{1,2}$") && date.isEmpty()) date =
		 * mock[i + 1]; else if (mock[i + 1].matches("^(\\d{2})(\\d{2})?$") &&
		 * year.isEmpty()) year = mock[i + 1];
		 * 
		 * } System.out.println("date:" + month + " " + year + " " + date); try
		 * { Date newDate = new
		 * SimpleDateFormat("yyyy,MMMM,dd").parse(year+","+month+","+date);
		 * String reportDate = new SimpleDateFormat("yyyyMMdd").format(newDate);
		 * System.out.println(reportDate); } catch (Exception e) {
		 * e.printStackTrace(); } }
		 */
		return tStreamOld.hasNext();
	}

	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return tStreamOld;
	}
}
