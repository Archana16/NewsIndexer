/**
 * 
 */
package edu.buffalo.cse.irf14;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.document.Parser;
import edu.buffalo.cse.irf14.document.ParserException;
import edu.buffalo.cse.irf14.index.IndexType;
import edu.buffalo.cse.irf14.index.IndexWriter;
import edu.buffalo.cse.irf14.index.IndexerException;
import edu.buffalo.cse.irf14.index.Postings;

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

		//String dir = System.getProperty("INDEX.DIR");
		//String ipDir = args[0];
		//String indexDir = args[1];
		//more? idk! 
		

		long startTime = System.currentTimeMillis(); // Get the start Time
	    long endTime = 0;
	    System.out.println( "starttime = "+startTime);
		
	   String ipDir = "/home/pritika/Downloads/news_training/training";
	    //String ipDir = "/home/archana/workspace-ir/IR/training/";
	String indexDir = "/home/pritika/Downloads/dfl";
		//String indexDir = "/home/archana/Downloads/dfl";
		IndexWriter writer = new IndexWriter(indexDir);
		Document d = null;
		//IndexWriter writer = new IndexWriter(indexDir);
		File ipDirectory = new File(ipDir);
		String[] catDirectories = ipDirectory.list();
		
		String[] files;
		File dir;
		
		

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
		
		
		
		//uncomment out this for one file
	/*	try{
			try {
				d = Parser.parse("/home/archana/workspace-ir/IR/training/money-supply/0004283");
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
		
		try{
			for (String cat : catDirectories) {
				dir = new File(ipDir+ File.separator+ cat);
				//System.out.println(i++ +" catDirectories is "+ipDir+ File.separator+ cat);
				files = dir.list();
				
				if (files == null)
					continue;
				
				for (String f : files) {
					
					try{
						try {
							
							d = Parser.parse(dir.getAbsolutePath() + File.separator +f);
							
							
							writer.addDocument(d);
							
							//System.out.println(d.getField(FieldNames.FILEID)[0]+" and "+d.getField(FieldNames.CATEGORY)[0]);
						}catch (Exception e) {//ParserException
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
				
			}
	
			
		
			endTime = System.currentTimeMillis(); //Get the end Time
			 System.out.println("total files ="+Parser.getNoOfFiles()+" time ="+(endTime-startTime));
			 writer.close();
			 
		}catch(IndexerException e){
			 
			}
		}
	}



