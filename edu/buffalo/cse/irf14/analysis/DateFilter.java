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

		//System.out.println("date filter function increment");
		ArrayList<String> list = new ArrayList<String>(Arrays.asList("january",
				"jan", "febuary", "feb", "march", "april", "may", "june",
				"july", "august", "aug", "september", "sep", "october", "oct",
				"november", "nov", "december", "dec"));

		while (tStreamOld.hasNext()) {
			boolean flag = false;
			String cur = "", next1 = "", next2 = "", prev = "", year = "1900", month = "january", date = "01", hour = "00", minute = "00", second = "00", time = "", type = "";
			cur = (tStreamOld.hasNext()) ? tStreamOld.next().getTermText() : "";

			next1 = (tStreamOld.hasTokenbyIndex(tStreamOld.getIndex())) ? tStreamOld
					.getTokenByIndex((tStreamOld.getIndex())).getTermText()
					: "";
			next2 = (tStreamOld.hasTokenbyIndex(tStreamOld.getIndex() + 1)) ? tStreamOld
					.getTokenByIndex((tStreamOld.getIndex() + 1)).getTermText()
					: "";
			prev = (tStreamOld.hasTokenbyIndex(tStreamOld.getIndex() - 2)) ? tStreamOld
					.getTokenByIndex((tStreamOld.getIndex() - 2)).getTermText()
					: "";
			/*
			 * System.out.println("before: " + cur + " " + next1 + " " + next2 +
			 * " " + prev);
			 */
			if (list.contains(cur.toLowerCase())) {
				month = cur;
				if (prev.matches("^\\d{1,2},?$")) {
					date = prev.replace(",", "");
					flag = true;
				}
				if (next1.matches("^\\d{1,2},?$"))
					date = next1.replace(",", "");
				if (next1.matches("^\\d{4}[,.]?$")) {
					year = next1.replace(",", "");
					year = year.replace(".", "");
				}
				if (next2.matches("^\\d{4}[.,]?$")) {
					year = next2.replace(",", "");
					year = year.replace(".", "");
				}
				// System.out.println("after date:" + month + " " + year + " "+
				// date);
				/* System.out.println(convertDate(year, month, date)); */

				if (flag) {
					if (!next2.isEmpty() && (next1.charAt(next1.length() - 1) == ',' || next1
							.charAt(next1.length() - 1) == '.')) {
						if (year == "1900")
							tStreamOld.replaceTokens(
									5,
									convertDate(year, month, date)
											+ next1.charAt(next1.length() - 1));
						else
							tStreamOld.replaceTokens(
									1,
									convertDate(year, month, date)
											+ next1.charAt(next1.length() - 1));
					} else {
						if (year == "1900")
							tStreamOld.replaceTokens(5,
									convertDate(year, month, date));
						else
							tStreamOld.replaceTokens(1,
									convertDate(year, month, date));

					}
				}

				else {
					if (!next2.isEmpty() && (next2.charAt(next2.length() - 1) == ',' || next2
							.charAt(next2.length() - 1) == '.')) {
						if (year == "1900")
							tStreamOld.replaceTokens(
									5,
									convertDate(year, month, date)
											+ next1.charAt(next1.length() - 1));
						else
							tStreamOld.replaceTokens(
									2,
									convertDate(year, month, date)
											+ next1.charAt(next1.length() - 1));
					} else {
						if (year == "1900"){
							tStreamOld.replaceTokens(5,
									convertDate(year, month, date));
						}else
							tStreamOld.replaceTokens(2,
									convertDate(year, month, date));

					}
				}

			} else if (cur.matches("^\\d{4}(-\\d{2})?[,.]?$")) {
				String years[] = cur.split("-");
				if (years.length > 1) {
					years[0] = years[0].replace(",", "");
					years[1] = years[0].substring(0, 2).concat(years[1]);
					years[1] = years[1].substring(0, years[1].length() - 1);
					if ((cur.charAt(cur.length() - 1) == ',' || cur.charAt(cur
							.length() - 1) == '.') && cur.length() > 1)
						tStreamOld.replaceTokens(4,
								convertDate(years[0], month, date) + "-"
										+ convertDate(years[1], month, date)
										+ cur.charAt(cur.length() - 1));
					else
						tStreamOld.replaceTokens(4,
								convertDate(years[0], month, date) + "-"
										+ convertDate(years[1], month, date));

					/*
					 * System.out.println(convertDate(years[0], month, date) +
					 * "-" + convertDate(years[1], month, date));
					 */
				} else {
					year = cur.replace(",", "");
					year = year.replace(".", "");
					if ((cur.charAt(cur.length() - 1) == '.' || cur.charAt(cur
							.length() - 1) == ',') && cur.length() > 1)
						tStreamOld.replaceTokens(
								4,
								convertDate(year, month, date)
										+ cur.charAt(cur.length() - 1));
					else
						tStreamOld.replaceTokens(4,
								convertDate(year, month, date));
					// tStreamOld.replaceTokens(4, convertDate(year, month,
					// date));
					/* System.out.println(convertDate(year, month, date)); */
				}

			} else if (cur.matches("^(\\d{1,2}:\\d{2})(:\\d{2})?$")) {
				time = cur;
				String part[] = time.split(":");
				String sec = (part.length == 2) ? "00" : part[2];
				if (next1.charAt(next1.length() - 1) == '.'
						|| next1.charAt(next1.length() - 1) == ',')
					tStreamOld.replaceTokens(
							5,
							convertTime(part[0], part[1], sec, next1)
									+ next1.charAt(next1.length() - 1));
				else
					tStreamOld.replaceTokens(5,
							convertTime(part[0], part[1], sec, next1));

				// System.out.println(convertTime(part[0], part[1], sec,
				// next1));
			} else if (cur
					.matches("^(\\d{1,2}:\\d{2})(:\\d{2})?([APap][mM])?([.,])?$")
					&& time.isEmpty()) {
				if (cur.toUpperCase().contains("AM")) {
					time = cur.toUpperCase().replace("AM", "");
					time = time.replace(".", "");
					type = "AM";
				}
				if (cur.toUpperCase().contains("PM")) {
					time = cur.toUpperCase().replace("PM", "");
					time = time.replace(".", "");
					type = "PM";
				}
				String part[] = time.split(":");
				String sec = (part.length == 2) ? "00" : part[2];
				if (cur.charAt(cur.length() - 1) == '.'
						|| cur.charAt(cur.length() - 1) == ',')
					tStreamOld.replaceTokens(
							4,
							convertTime(part[0], part[1], sec, type)
									+ cur.charAt(cur.length() - 1));
				else
					tStreamOld.replaceTokens(4,
							convertTime(part[0], part[1], sec, type));
				// System.out.println(convertTime(part[0], part[1], sec, type));

			} else if (cur.equals("AD") || cur.equals("BC")) {
				year = prev;
				if (cur.equals("BC"))
					tStreamOld.replaceTokens(3,
							convertBC_AD(year, month, date, "BC"));
				// System.out.println(convertBC_AD(year, month, date, "BC"));
				else
					tStreamOld.replaceTokens(3,
							convertBC_AD(year, month, date, "AD"));
				// System.out.println(convertBC_AD(year, month, date, "AD"));
				/*
				 * System.out.println("after: " + cur + " " + next1 + " " +
				 * next2 + " " + prev);
				 */
			} else if (cur.matches("^\\d{1,4}((BC)|(AD))[.,]$")) {
				if (cur.charAt(cur.length() - 1) == '.'
						|| cur.charAt(cur.length() - 1) == ',') {
					year = cur.substring(0, cur.length() - 3);
					tStreamOld.replaceTokens(
							4,
							convertBC_AD(
									year,
									month,
									date,
									cur.substring(cur.length() - 3,
											cur.length() - 1))
									+ cur.charAt(cur.length() - 1));
				} else {
					year = cur.substring(0, cur.length() - 2);
					convertBC_AD(year, month, date,
							cur.substring(cur.length() - 3, cur.length() - 1));
				}

				/*
				 * System.out.println(convertBC_AD(year, month, date,
				 * cur.substring(cur.length() - 2)));
				 */
			}

		}
		return tStreamOld.hasNext();
	}

	public String convertDate(String y, String m, String d) {
		String reportDate = "";
		try {
			Date newDate = new SimpleDateFormat("yyyy,MMMM,dd").parse(y + ","
					+ m + "," + d);
			reportDate = new SimpleDateFormat("yyyyMMdd").format(newDate);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportDate;
	}

	public String convertBC_AD(String y, String m, String d, String type) {
		String reportDate = "";
		try {
			Date newDate = new SimpleDateFormat("yyyy,MMMM,dd").parse(y + ","
					+ m + "," + d);
			reportDate = new SimpleDateFormat("yyyyMMdd").format(newDate);
			if (type.equals("BC")) {
				reportDate = "-" + reportDate;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportDate;
	}

	// NOT REQUIRED
	/*
	 * public String prependZero(String str){ String s= "0000"+str; // max four
	 * zeros return s.substring(s.length()-4); // keep the rightmost 4 chars }
	 */
	public String convertTime(String h, String m, String s, String type) {
		String reportTime = "";
		try {
			Date newTime = new SimpleDateFormat("hh:mm:ss aa").parse(h + ":"
					+ m + ":" + s + " " + type);
			reportTime = new SimpleDateFormat("HH:mm:ss").format(newTime);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportTime;
	}

	public TokenStream getStream() {
		// TODO Auto-generated method stub
		return tStreamOld;
	}
}
