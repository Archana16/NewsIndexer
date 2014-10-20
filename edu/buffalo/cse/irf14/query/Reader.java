package edu.buffalo.cse.irf14.query;

import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;

public class Reader {
	private IndexReader AuthorIndex;
	private IndexReader PlaceIndex;
	private IndexReader CategoryIndex;
	private IndexReader TermIndex;
	private static String indexDir;

	public Reader() {
		// TODO Auto-generated constructor stub
		System.out.printf("creating all readers");
		indexDir = "/home/pritika/Downloads/dfl";
		AuthorIndex = new IndexReader(indexDir, IndexType.AUTHOR);
		PlaceIndex = new IndexReader(indexDir, IndexType.PLACE);
		CategoryIndex = new IndexReader(indexDir, IndexType.CATEGORY);
		TermIndex = new IndexReader(indexDir, IndexType.TERM);
		System.out.printf("exiting");
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
