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

	public static void contentParse(String content) {
		Scanner words = new Scanner(content);
		String Place = "";
		String Author = "";
		String Date = "";
		String AuthorOrg = "";
		ArrayList<String> list = new ArrayList<String>(Arrays.asList("january",
				"jan", "febuary", "feb", "march", "april", "may", "june",
				"july", "august", "aug", "september", "sep", "october", "oct",
				"november", "nov", "december", "dec"));
		
		while (words.hasNext()) {
			String token = words.next();
			if (testAllUpperCase(token)) {
				if (token.contains("<AUTHOR>")) {
					token = words.next();
					//System.out.println(token);
					while (true) {
						if (token.contains("</AUTHOR>")) {
							token = token.replace("</AUTHOR>", "");
							Author += " " + token;
							break;
						} else {
							Author += " " + token;
							token = words.next();
						}
					}
					String[] parts = Author.split(",");
					if(parts.length>1){
						Author = parts[0];
						AuthorOrg =parts[1];
						
					}
						
					
				} else if (Place.isEmpty()) {
					Place = token;
					String place1= words.next();
					while(!list.contains(place1.toLowerCase())){
						Place +=" "+place1;
						place1 = words.next();
					}
					if (list.contains(place1.toLowerCase()) && Date.isEmpty()) {
						Date = place1;
						String day = words.next();
						if (day.contains(","))
							day=day.split(",")[0];
						Date += " " + day;
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
		if(filename == null || filename == ""){
            throw new ParserException("Filename is empty");
        }
		if(!filename.matches("\\w.txt")){
            throw new ParserException("Invalid input file");
        }
		// TODO YOU MUST IMPLEMENT THIS

		try {
			d = new Document();
			int index = filename.lastIndexOf("/");
			d.setField(FieldNames.FILEID, filename.substring(index + 1));
			d.setField(FieldNames.CATEGORY, filename.substring(filename.substring(0, index).lastIndexOf("/") + 1, index));
			File file = new File(filename);
			FileReader reader = new FileReader(file);
			BufferedReader in = new BufferedReader(reader);
			String string = "";String Content = "";String Title = "";
			while ((string = in.readLine()) != null) {
				if (string.isEmpty())
					continue;
				if (testAllUpperCase(string)) {
					if (Title.isEmpty())
						Title = string;
				} else {
					Content += string;
				}
			}
			d.setField(FieldNames.TITLE, Title);
			d.setField(FieldNames.CONTENT, Content);
			contentParse(Content);
			System.out.println(d.getField(FieldNames.FILEID)[0]);
			System.out.println(d.getField(FieldNames.CATEGORY)[0]);
			System.out.println(d.getField(FieldNames.TITLE)[0]);
			System.out.println(d.getField(FieldNames.CONTENT)[0]);
			System.out.println(d.getField(FieldNames.AUTHOR)[0]);
			System.out.println(d.getField(FieldNames.AUTHORORG)[0]);
			System.out.println(d.getField(FieldNames.PLACE)[0]);
			System.out.println(d.getField(FieldNames.NEWSDATE)[0]);

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//made by pritika
	public static Document parse() throws ParserException {
		// TODO YOU MUST IMPLEMENT THIS
		
		//temp
		
		Document d= new Document();
		
		
		FieldNames f;
		String content =new String(" this. pritikaMehta is a test. The city San Francisco is in California. Some bodily fluids, such as saliva and tears, do not transmit HIV. It runs Apple's iOS mobile operating system,");
				/*@Microbiological Research Corp\n" + 
				"said it entered into a letter of can't could've couldn't intent for a B-45 A-Biti55  A-Bitibfdn 55BBB-AA proposed business\n" + 
				"combination with privately owned <DataGene Scientific\n" + 
				"Laboratories Inc>, and <Milex Corp> a newly formed company,\n" + 
				"through a stock swap.\n" );*/
	/*	d.setField(FieldNames.FILEID,"1");
		d.setField(FieldNames.CATEGORY,"acq");
		d.setField(FieldNames.TITLE,"MICROBIO <MRC> PLANS ACQUISITION, FINANCING");
		//d.setField(FieldNames.AUTHOR,"1");
		//d.setField(FieldNames.AUTHORORG,"1");
		d.setField(FieldNames.PLACE,"  BOUNTIFUL, Utah,");
		d.setField(FieldNames.NEWSDATE,"March 2");*/
		d.setField(FieldNames.CONTENT,content);


		return d;
		//return null;
	}

}
