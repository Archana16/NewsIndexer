/**
 * 
 */
package edu.buffalo.cse.irf14.document;

import java.io.*;
import java.util.*;

/**
 * @author nikhillo Class that parses a given file into a Document
 */
public class Parser {
	/**
	 * Static method to parse the given file into the Document object
	 * 
	 * @param filename
	 *            : The fully qualified filename to be parsed
	 * @return The parsed and fully loaded Document object
	 * @throws ParserException
	 *             In case any error occurs during parsing
	 */

	private static Document d;
	private static ArrayList<String> list = new ArrayList<String>(
			Arrays.asList("january", "jan", "febuary", "feb", "march", "april",
					"may", "june", "july", "august", "aug", "september", "sep",
					"october", "oct", "november", "nov", "december", "dec"));

	public static boolean testAllUpperCase(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= 97 && c <= 122) {
				return false;
			}
		}
		// str.charAt(index)
		return true;
	}

	public static String checkDate(String str) {
		String[] words = str.split(" ");
		String date = "";
		for (int i = 0; i < words.length; i++) {
			if (list.contains(words[i].toLowerCase())) {
				date = words[i];
				if(i < words.length){
					if (words[i + 1].matches("^\\d{1,2},?$") ||words[i + 1].matches("^\\d{4},?$"))
						date += " " + words[i + 1];
					
				}
				if(i >0){
					if (words[i -1].matches("^\\d{1,2},?$") ||words[i -1].matches("^\\d{4},?$"))
						date = words[i -1]+" "+date;
				}
				if (date.contains(","))
					date = date.split(",")[0];
				return date;
			}
		}
		return null;
	}

	public static void contentParse(String content) {
		Scanner words = new Scanner(content);
		String Place = "";
		String Author = "";
		String Date = "";
		String AuthorOrg = "";
		if(Date.isEmpty())
			Date= checkDate(content);
		while (words.hasNext()) {
			String token = words.next();
			if (testAllUpperCase(token)) {
				if (token.contains("<AUTHOR>")) {
					token = words.next();
					while (true) {
						if (token.contains("</AUTHOR>")) {
							token = token.replace("</AUTHOR>", "");
							Author += " " + token;
							break;
						} else {
							Author += " " + token;
							token = words.hasNext() ? words.next() : "";
						}
					}
					String[] parts = Author.split(",");
					if (parts.length > 1) {
						Author = parts[0];
						AuthorOrg = parts[1];

					}

				} else{
					if(Place.isEmpty()){
						if (token.charAt(token.length() - 1) == ',') 
							Place = token;
						else{
							Place = token;
							String place1 ="";
							while (words.hasNext()) {
								place1 = words.next();
								if (place1.matches("^\\w+,$") && !Place.isEmpty())
									break;
								else
									Place += " " + place1;
								}
							}
						}
					}
				}
		}
		d.setField(FieldNames.PLACE, Place);
		d.setField(FieldNames.AUTHOR, Author);
		d.setField(FieldNames.AUTHORORG, AuthorOrg);
		d.setField(FieldNames.NEWSDATE, Date);
		words.close();
	}

	public static Document parse(String filename) throws ParserException {

		// System.out.println(filename);

		if (filename == null || filename == "") {
			throw new ParserException("Filename is empty");
		}
		if (!filename.matches("((/[a-zA-Z0-9_.-]+))+")) {
			throw new ParserException("Invalid input file " + filename);
		}
		// TODO YOU MUST IMPLEMENT THIS
		else {
			try {
				d = new Document();
				int index = filename.lastIndexOf("/");
				d.setField(FieldNames.FILEID, filename.substring(index + 1));
				d.setField(FieldNames.CATEGORY, filename.substring(filename
						.substring(0, index).lastIndexOf("/") + 1, index));
				File file = new File(filename);
				FileReader reader = new FileReader(file);
				BufferedReader in = new BufferedReader(reader);
				String string = "";
				String Content = "";
				String Title = "";
				while ((string = in.readLine()) != null) {
					// System.out.println(string);
					if (!string.isEmpty() && testAllUpperCase(string)) {
						if (Title.isEmpty())
							Title = string;
					} else {
						Content += string;
					}
				}
				d.setField(FieldNames.TITLE, Title);
				d.setField(FieldNames.CONTENT, Content);
				contentParse(Content);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return d;
		}
	}
	
	
	//made by pritika
	/*public static Document parse() throws ParserException {
		// TODO YOU MUST IMPLEMENT THIS
		
		//temp
		
		Document d= new Document();
		
		
		FieldNames f;
		String content =new String("The city $ San Francisco ~method() destructor is ~method() is in California. Some bodily fluids, such as saliva and tears, do not transmit HIV");
				@Microbiological Research Corp\n" + 
				"said it entered into a letter of can't could've couldn't intent for a B-45 A-Biti55  A-Bitibfdn 55BBB-AA proposed business\n" + 
				"combination with privately owned <DataGene Scientific\n" + 
				"Laboratories Inc>, and <Milex Corp> a newly formed company,\n" + 
				"through a stock swap.\n" );
		d.setField(FieldNames.FILEID,"1");
		d.setField(FieldNames.CATEGORY,"acq");
		d.setField(FieldNames.TITLE,"MICROBIO <MRC> PLANS ACQUISITION, FINANCING");
		//d.setField(FieldNames.AUTHOR,"1");
		//d.setField(FieldNames.AUTHORORG,"1");
		d.setField(FieldNames.PLACE,"  BOUNTIFUL, Utah,");
		d.setField(FieldNames.NEWSDATE,"March 2");
		d.setField(FieldNames.CONTENT,content);
		return d;
	}*/

}
