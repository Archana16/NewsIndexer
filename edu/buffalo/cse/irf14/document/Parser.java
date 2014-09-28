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
	
	private static int docMapIndex=1;
	private static TreeMap<String , Integer> docMap = new TreeMap<String, Integer>();
	
	/**
	 * Static method to parse the given file into the Document object
	 * 
	 * @param filename
	 *            : The fully qualified filename to be parsed
	 * @return The parsed and fully loaded Document object
	 * @throws ParserException
	 *             In case any error occurs during parsing
	 */

	public static TreeMap<String,Integer> getDocMap(){
		return docMap;
	} 
	
	public static void setDocMap(String doc_name){
			docMap.put(doc_name,docMapIndex++);
	}
	public static int getNoOfFiles(){
		return docMapIndex;
	}
	
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
		//System.out.println("in content parse");
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
			if(token.isEmpty())
				continue;
			//System.out.println("in while rue "+token);
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
							token = words.hasNext()?words.next():"";
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
					
				//	System.out.println("in else if with place="+place1);
					/*while(!list.contains(place1.toLowerCase())){
						Place +=" "+place1;
						place1 = words.hasNext()?words.next():"";
						if(place1.isEmpty())
							break;
						System.out.println("in inner while place = "+place1);
						
					}*/
					if (list.contains(place1.toLowerCase()) && Date.isEmpty()) {
						Date = place1;
						String day = words.hasNext()?words.next():"";
						if (day!="" && day.contains(","))
							day=day.split(",")[0];
						Date += " " + day;
					}
							
				}
			}
		
		}
		//System.out.println("out of while");
		d.setField(FieldNames.PLACE, Place);
		d.setField(FieldNames.AUTHOR, Author);
		d.setField(FieldNames.AUTHORORG, AuthorOrg);
		d.setField(FieldNames.NEWSDATE, Date);
		//System.out.println("\t\tit is dne");
		words.close();
	}

	public static Document parse(String filename) throws ParserException {
		
		//System.out.println(filename);
		
		if(filename == null || filename == ""){
            throw new ParserException("Filename is empty");
        }
		if(!filename.matches("((/[a-zA-Z0-9_.-]+))+")){
            throw new ParserException("Invalid input file "+filename);
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
				//System.out.println("i am in while line = "+string);
			/*	if (string.isEmpty()){
					System.out.println("continue");
					continue;
				}*/	
				if (testAllUpperCase(string)) {
					if (Title.isEmpty())
						Title = string;
				} else {
					Content += string;
				}
			}
			
			//System.out.println("content is "+Content);
			d.setField(FieldNames.TITLE, Title);
			d.setField(FieldNames.CONTENT, Content);
			contentParse(Content);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String docName = d.getField(FieldNames.CATEGORY)[0]+"_"+d.getField(FieldNames.FILEID)[0];
		setDocMap(docName);
		return d;
	}
	
	
	//made by pritika
	public static Document parse() throws ParserException {
		// TODO YOU MUST IMPLEMENT THIS
		
		//temp
		
		Document d= new Document();
		
		
		FieldNames f;
		String content =new String("TRADERS SAY USDA MAY LOWER ORANGE CROP ESTIMATE\n" + 
				"\n" + 
				"    New York, March 9 - The U.S. Department of Agriculture will\n" + 
				"probably decrease its estimate of the 1986/87 Florida orange\n" + 
				"crop today to as low as 123 mln boxes from 129 mln boxes,\n" + 
				"analysts and industry sources said.\n" + 
				"    The Department is scheduled to release the new estimate at\n" + 
				"1500 hrs EST (2100 gmt) today.\n" + 
				"    Analysts said the market is anticipating a downward\n" + 
				"revision and much of the bullish impact has been discounted.\n" + 
				"    The estimate, which the USDA has left unchanged since\n" + 
				"October, should be affected this time by recent evidence of a\n" + 
				"shortfall in the early and midseason crop now that those\n" + 
				"harvests are complete. Analysts said based on earlier USDA\n" + 
				"projections, the harvests should have been five to seven mln\n" + 
				"boxes larger than they were.\n" + 
				"    \"They are going to cut their estimate,\" said Bob Tate, an\n" + 
				"FCOJ broker with Dean Witter Reynolds in Miami. \"The only\n" + 
				"question is whether they will admit the whole thing in this\n" + 
				"estimate.\"\n" + 
				"    Tate said it is possible the USDA will lower its estimate\n" + 
				"by a lesser amount, perhaps three mln boxes, and continue to\n" + 
				"drop the estimate in subsequent reports as the crop picture\n" + 
				"clarifies. The late season harvest, consisting mostly of\n" + 
				"Valencia oranges, has not yet started, he noted.\n" + 
				"    \"They'll temper it,\" said Judy Weissman, FCOJ analyst with\n" + 
				"Shearson Lehman Bros. \"The main drop will probably come in\n" + 
				"July.\"\n" + 
				"    She expects today's estimate will be 126 mln boxes..");
		d.setField(FieldNames.FILEID,"1");
		d.setField(FieldNames.CATEGORY,"acq");
		d.setField(FieldNames.TITLE,"MICROBIO <MRC> PLANS ACQUISITION, FINANCING");
		d.setField(FieldNames.AUTHOR,"1");
		d.setField(FieldNames.AUTHORORG,"1");
		d.setField(FieldNames.PLACE,"  BOUNTIFUL, Utah,");
		d.setField(FieldNames.NEWSDATE,"March 2");
		d.setField(FieldNames.CONTENT,content);
		return d;
	}

}
