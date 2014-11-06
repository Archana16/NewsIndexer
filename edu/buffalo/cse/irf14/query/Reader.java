package edu.buffalo.cse.irf14.query;

import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;

public class Reader {
	private IndexReader AuthorIndex;
	private IndexReader PlaceIndex;
	private IndexReader CategoryIndex;
	private IndexReader TermIndex;
	private static String indexDir;

	public Reader(String indexDir) {
		// TODO Auto-generated constructor stub
		
		this.indexDir = indexDir;
		AuthorIndex = new IndexReader(indexDir, IndexType.AUTHOR);
		PlaceIndex = new IndexReader(indexDir, IndexType.PLACE);
		CategoryIndex = new IndexReader(indexDir, IndexType.CATEGORY);
		TermIndex = new IndexReader(indexDir, IndexType.TERM);
		
	}
	public IndexReader getReader(IndexType type) {
		switch (type) {
		case AUTHOR:
			return AuthorIndex;
		case PLACE:
			return PlaceIndex;
		case CATEGORY:
			return CategoryIndex;
		default:
			return TermIndex;
		}
	}

}
