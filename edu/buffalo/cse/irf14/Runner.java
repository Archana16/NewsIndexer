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
		*/
		String ipDir = "/home/archana/workspace-ir/IR/training";
		//String indexDir = "dfl";
		String[] files;
		File dir;
		File ipDirectory = new File(ipDir);
		String[] catDirectories = ipDirectory.list();
		/*int i =0,j=0;
		for(String cat :catDirectories)
			{
			dir = new File(ipDir+ File.separator+ cat);
			files = dir.list();
			
			if (files == null)
				continue;
			System.out.println(ipDir+ File.separator+ cat);
			i++;
			dir = new File(ipDir+ File.separator+ cat);
			files = dir.list();
			for(String f :files)
			{
			System.out.println(f);
			j++;
			}
			System.out.println(j);
			}
		System.out.println(i);*/
		
		Document d = null;
		IndexWriter writer = new IndexWriter();
		
		//uncomment out this for one file
/*		try{
			try {
				d = Parser.parse("/home/archana/workspace-ir/IR/training/dfl/0007273");
				System.out.println(d.getField(FieldNames.FILEID)[0]);
				System.out.println(d.getField(FieldNames.CATEGORY)[0]);
				System.out.println(d.getField(FieldNames.PLACE)[0]);
				System.out.println(d.getField(FieldNames.AUTHOR)[0]);
				System.out.println(d.getField(FieldNames.AUTHORORG)[0]);
				System.out.println(d.getField(FieldNames.NEWSDATE)[0]);
				//writer.addDocument(d);
			} catch (Exception e) {//ParserException
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}*/
		
		//uncomment this for whole directory
		try {
			for (String cat : catDirectories) {
				dir = new File(ipDir+ File.separator+ cat);
				files = dir.list();
				
				if (files == null)
					continue;
				
				for (String f : files) {
					try {
						d = Parser.parse(dir.getAbsolutePath() + File.separator +f);
						System.out.println("back from"+dir.getAbsolutePath() + File.separator +f);
						//writer.addDocument(d);
					} catch (ParserException e) {//ParserException
						// TODO Auto-generated catch block
						e.getMessage();
					} 
					
				}
				
				
			}
			System.out.println("DONE!!!!");
			writer.close();
		} catch (IndexerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
//	PRITIKA's code
		
	/*	try {
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
		} */
		
	}

}
