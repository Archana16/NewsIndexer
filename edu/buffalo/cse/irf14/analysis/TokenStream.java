/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author nikhillo Class that represents a stream of Tokens. All
 *         {@link Analyzer} and {@link TokenFilter} instances operate on this to
 *         implement their behavior
 */
public class TokenStream implements Iterator<Token> {

	private ArrayList<Token> mylist; // ArrayList<Vector> will be set here
	private int index;
	private int tempIndex;
	private int lastIndex;
	private int previous;
	private int previousLoop;
	private int current;

	public TokenStream() {

	}

	public TokenStream(ArrayList<Token> mylist) {
		this.mylist = mylist;
		index = tempIndex = lastIndex=0;
		previous = -2;
		previousLoop = -2;
		current = -1;
	}
	
	

	/**
	 * Method that checks if there is any Token left in the stream with regards
	 * to the current pointer. DOES NOT ADVANCE THE POINTER
	 * 
	 * @return true if at least one Token exists, false otherwise
	 */
	@Override
	public boolean hasNext() {
		// TODO YOU MUST IMPLEMENT THIS
		if (mylist.size() > 1 && index < mylist.size() && index >= 0)
			return true;
		else if (mylist.size() == 1 && index == 0)
			return true;
		else
			return false;
	}

	public boolean hasPrevious() {
		// TODO YOU MUST IMPLEMENT THIS
		if (previousLoop >= 0 && mylist.size() >= 0
				&& previousLoop < mylist.size())
			return true;
		else
			return false;
	}

	public Token previous() {
		if (previousLoop > 0 && mylist.size() >= 0)
			return mylist.get(previousLoop--);
		else if (previousLoop == 0) {
			previousLoop--;
			return mylist.get(0);
		} else
			return null;
	}

	public void resetPrevious() {
		//System.out.println("previous was " + previous + " and now it is "
			//	+ previousLoop);
		previousLoop = previous;

	}

	/**
	 * Method to return the next Token in the stream. If a previous hasNext()
	 * call returned true, this method must return a non-null Token. If for any
	 * reason, it is called at the end of the stream, when all tokens have
	 * already been iterated, return null
	 */
	@Override
	public Token next() {
		// TODO YOU MUST IMPLEMENT THIS
		if (index < mylist.size()) {
			previous++;
			current++;
			previousLoop = previous;
			return mylist.get(index++);
		} else {
			if (previous < mylist.size() - 1)
				previous++;
			current++;
			lastIndex= index;
			index = -1;
			previousLoop = previous;
			return null;
		}
	}

	public void setTempIndex() {
		//System.out.println("earlier temp index was " + tempIndex + " index ="
			//	+ index);
		tempIndex = index;

	}

	public void setIndexCurrent() {
		//System.out.println(" index was " + index + " temp index =" + tempIndex);
		index = tempIndex;
		previous = index - 2;
	}

	/**
	 * Method to remove the current Token from the stream. Note that "current"
	 * token refers to the Token just returned by the next method. Must thus be
	 * NO-OP when at the beginning of the stream or at the end
	 */
	@Override
	public void remove() {
		// TODO YOU MUST IMPLEMENT THIS
		current = -9;
		try{
		if (index != -1) {
			if (index > 0 && index != mylist.size()) {
				//if(!mylist.get(index-1).getTermText().isEmpty())
				//System.out.println("removed = "+mylist.get(index-1));
				mylist.remove(index - 1);
				index = index - 1;
			} else if (index == mylist.size() && mylist.size() != 0) {
				//if(!mylist.get(index-1).getTermText().isEmpty())
					//System.out.println("removed = "+mylist.get(index-1));
				mylist.remove(--index);
				
			}
		}
		}catch(Exception e){
			System.out.println("exception is from remove "+e);
		}
	}

	/**
	 * Method to reset the stream to bring the iterator back to the beginning of
	 * the stream. Unless the stream has no tokens, hasNext() after calling
	 * reset() must always return true.
	 */
	public void reset() {
		// TODO : YOU MUST IMPLEMENT THIS
		if (mylist.size() > 0)
			index = 0;
		else
			index = -1;
	}

	/**
	 * Method to append the given TokenStream to the end of the current stream
	 * The append must always occur at the end irrespective of where the
	 * iterator currently stands. After appending, the iterator position must be
	 * unchanged Of course this means if the iterator was at the end of the
	 * stream and a new stream was appended, the iterator hasn't moved but that
	 * is no longer the end of the stream.
	 * 
	 * @param stream
	 *            : The stream to be appended
	 */
	public void append(TokenStream stream) {
		if(stream != null){
			stream.reset();
			while(stream.hasNext()){
				Token t = new Token();
				t.setTermText(stream.next().getTermText());
				mylist.add(t);
				if(index== -1)
					index = lastIndex;
				
				
			}
			// TODO : YOU MUST IMPLEMENT THIS
			
			}
	}

	/**
	 * Method to get the current Token from the stream without iteration. The
	 * only difference between this method and {@link TokenStream#next()} is
	 * that the latter moves the stream forward, this one does not. Calling this
	 * method multiple times would not alter the return value of
	 * {@link TokenStream#hasNext()}
	 * 
	 * @return The current {@link Token} if one exists, null if end of stream
	 *         has been reached or the current Token was removed
	 */
	public Token getCurrent() {
		// TODO: YOU MUST IMPLEMENT THIS
		if (index > 0 && index <= mylist.size() && current != -9) {
			return mylist.get(index - 1);
		} else
			return null;

	}

	// method to return the current index
	public int getIndex() {
		return index;
	}

	public boolean hasTokenbyIndex(int index) {
		if (index < 0 || index >= mylist.size() )
			return false;
		else
			return true;
	}

	public Token getTokenByIndex(int index) {
		return mylist.get(index);
	}

	public Token getPrevious() {
		if (previous >= 0 && current <= mylist.size()) {
			return mylist.get(previous);
		} else
			return null;

	}
	public void replaceTokens(int type, String ele){
		Token t = new Token();
		t.setTermText(ele);
		switch(type){
		case 1: {
			//first month
			if(index >2 && index <mylist.size()){
				mylist.remove(--index);
				mylist.remove(--index);
				if(index>0 && index < mylist.size())
				mylist.set( index, t );
			}
			break;
		}
		case 2 :{
			//second month
			if(index >1 && index < mylist.size()){
				index++;
				mylist.remove(--index);
				mylist.remove(--index);
				if(index>0 && index < mylist.size())
				mylist.set( index, t );
			}
			break;
			
		}
		case 3 :{
			//AD-BC
			if(index >2 && index <mylist.size()){
				--index;
				mylist.remove(--index);
				if(index>0 && index < mylist.size())
				mylist.set( index, t );
			}
			break;
		}
		case 4 :{
			//only year and combined time and combined AD //numeric filter
			if(index >1 && index <mylist.size()){
				--index;
				//mylist.remove(--index);
				if(index>0 && index < mylist.size())
				mylist.set( index, t );
			}
			break;
		}
		case 5 :{
			//time
			if(index >1 && index <mylist.size()){
				mylist.remove(--index);
				if(index>0 && index < mylist.size())
				mylist.set( index, t );
			}
			break;
		}
		case 6:{
			//numeric filter
			if(index >1 && index <mylist.size()){
				--index;
				 mylist.remove(index);
				//++index;
			}
			break;
		}
		
	}
		
	}
	}

