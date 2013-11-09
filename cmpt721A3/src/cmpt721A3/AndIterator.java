package cmpt721A3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class AndIterator implements Iterator<Statement> {

	private HashMap<AtomicStatement, AllStatement> alls;
	//hash set for atoms
	//hash map for exists
	//iterators for each of the hash things
	//some kind of state variables for which hash things we've already iterated
	
	public AndIterator(HashSet<AtomicStatement> atoms, HashMap<AtomicStatement, AllStatement> alls, HashMap<AtomicStatement, ExistsStatement> exists)
	{
		
	}
	
	@Override
	public boolean hasNext()
	{
		//have we reached the end of the current set?
		//if so, go to next
		//if current iterator.hasNext return true;
		//otherwise advance to next iterator and if the last is done return false
		return false;
	}

	@Override
	public Statement next() {
		//return currentiterator.next
		return null;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("Remove is not supported in AndStatement.");

	}

}
