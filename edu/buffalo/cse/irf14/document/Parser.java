/**
 * 
 */
package edu.buffalo.cse.irf14.document;

/**
 * @author nikhillo
 * Class that parses a given file into a Document
 */
public class Parser {
	/**
	 * Static method to parse the given file into the Document object
	 * @param filename : The fully qualified filename to be parsed
	 * @return The parsed and fully loaded Document object
	 * @throws ParserException In case any error occurs during parsing
	 */
	public static Document parse(String filename) throws ParserException {
		// TODO YOU MUST IMPLEMENT THIS
		return null;
	}
	
	
	//made by pritika
	public static Document parse() throws ParserException {
		// TODO YOU MUST IMPLEMENT THIS
		
		//temp
		
		Document d= new Document();
		
		
		FieldNames f;
		String content =new String("nа̀ра ('steam/vapour') and nара̀ ('cent/penny, money");
				/*@Microbiological Research Corp\n" + 
				"said it entered into a letter of can't could've couldn't intent for a B-45 A-Biti55  A-Bitibfdn 55BBB-AA proposed business\n" + 
				"combination with privately owned <DataGene Scientific\n" + 
				"Laboratories Inc>, and <Milex Corp> a newly formed company,\n" + 
				"through a stock swap.\n" );*/
		d.setField(FieldNames.FILEID,"1");
		d.setField(FieldNames.CATEGORY,"acq");
		d.setField(FieldNames.TITLE,"MICROBIO <MRC> PLANS ACQUISITION, FINANCING");
		//d.setField(FieldNames.AUTHOR,"1");
		//d.setField(FieldNames.AUTHORORG,"1");
		d.setField(FieldNames.PLACE,"  BOUNTIFUL, Utah,");
		d.setField(FieldNames.NEWSDATE,"March 2");
		d.setField(FieldNames.CONTENT,content);


		return d;
		//return null;
	}

}
