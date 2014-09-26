/**
 * 
 */
package edu.buffalo.cse.irf14;

import java.io.File;

import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.document.Parser;
import edu.buffalo.cse.irf14.document.ParserException;
import edu.buffalo.cse.irf14.index.IndexWriter;
import edu.buffalo.cse.irf14.index.IndexerException;

/**
 * @author nikhillo
 *
 */
public class Runner {

	/**
	 * 
	 */
	public Runner() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/*String ipDir = args[0];
		String indexDir = args[1];
		//more? idk!
		
	/*	Document d = null;
		
		try {
			d = Parser.parse( "/home/archana/workspace-ir/IR/news_training/training/dfl/0007273");
		} catch (Exception e) {//ParserException
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		
		String ipDir = "/home/archana/workspace-ir/IR/news_training/training";
		String indexDir = "dfl";
		File ipDirectory = new File(ipDir);
		String[] catDirectories = ipDirectory.list();
		
		String[] files;
		File dir;
		Document d = null;
		IndexWriter writer = new IndexWriter();
		
		
	/*	try {
			for (String cat : catDirectories) {
				dir = new File(ipDir+ File.separator+ cat);
				files = dir.list();
				
				if (files == null)
					continue;
				
				for (String f : files) {
					try {
						d = Parser.parse(dir.getAbsolutePath() + File.separator +f);
						writer.addDocument(d);
					} catch (ParserException e) {//ParserException
						// TODO Auto-generated catch block
						e.getMessage();
					} 
					
				}
				
			}
			writer.close();
		} catch (IndexerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		*/
		
		
//	PRITIKA's code
		
		try {
			d = Parser.parse();
			System.out.println("hey i got the doc with name = "+d.getField(FieldNames.CONTENT)[0]);
			try{
				System.out.println("ok");
				writer.addDocument(d);
				
			}catch(IndexerException e){
				System.out.println("it is "+e);
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//IndexWriter writer = new IndexWriter(indexDir);
		
//		try {
//			for (String cat : catDirectories) {
//				dir = new File(ipDir+ File.separator+ cat);
//				files = dir.list();
//				
//				if (files == null)
//					continue;
//				
//				for (String f : files) {
//					try {
//						d = Parser.parse(dir.getAbsolutePath() + File.separator +f);
//						writer.addDocument(d);
//					} catch (ParserException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} 
//					
//				}
//				
//			}
//			
//			writer.close();
//		} catch (IndexerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
