// CMPT 721 Assignment 3
// Authors: James Twigg and Wanda Boyer
//	    301229679       301242166

package cmpt721A3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AndIterator implements Iterator<Statement> {

	private static final int IN_ATOMS = 0;
	private static final int IN_EXISTS = 1;
	private static final int IN_ALLS = 2;
	
	private Iterator<AtomicStatement> atomItr;
	private Iterator<AllStatement> allItr;
	private Iterator<ExistsStatement> existItr;
	
	private int state;
	
	public AndIterator(HashSet<AtomicStatement> atoms, HashMap<AtomicStatement, AllStatement> alls, HashMap<AtomicStatement, ExistsStatement> exists)
	{
		// Start off in the set of atomic statements.
		state = 0;
		
		// Instantiate the three types of required iterators.
		atomItr = atoms.iterator();
		allItr = alls.values().iterator();
		existItr = exists.values().iterator();
	}
	
	// The hasNext() method returns true when there is a next element; the tricky bit is
	// that we have three different sets of objects that have to be iterated over in an AND
	// statement. When you reach the end of the list of atomic statements or EXISTS, you 
	// need to move over to the next set of statements and see if there is something in that
	// set. When you've reached the end of the ALLs, there are no more statements that are 
	// part of the overarching AND, hence return false. 
	@Override
	public boolean hasNext()
	{
		switch(state)
		{
			case IN_ATOMS:
			{
				if(atomItr.hasNext())
				{
					return true;
				}
				else
				{
					state = IN_EXISTS;
					return hasNext();
				}
			}
			case IN_EXISTS:
			{
				if(existItr.hasNext())
				{
					return true;
				}
				else
				{
					state = IN_ALLS;
					return hasNext();
				}
			}
			case IN_ALLS:
			{
				return allItr.hasNext();
			}
			default:
			{
				return false;
			}
		}
	}
	// The next() method first checks to see if there is a next element to fetch.
	@Override
	public Statement next()
	{
		switch(state)
		{
			case IN_ATOMS:
			{
				// Is there another atomic statement in the AND?
				if(hasNext())
				{
					return atomItr.next();
				}
				// If there wasn't a next atomic statement to return,
				// the hasNext() method changed the state for us to 
				// IN_EXISTS, so this next invocation of next() will
				// go to the next case in the switch statement.
				else
				{
					return next();
				}
			}
			case IN_EXISTS:
			{
				if(hasNext())
				{
					return existItr.next();
				}
				else
				{
					return next();
				}
			}
			case IN_ALLS:
			{
				if(hasNext())
				{
					return allItr.next();
				}
				else
				{
					throw new NoSuchElementException("End of Collection!");
				}
			}
			default:
			{
				throw new RuntimeException("Impossible state in AndIterator.");
			}
		}
	}
	
	// We wanted to make sure no one would try to invoke this method when it is not part of the system.
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("Remove is not supported in AndStatement.");

	}

}
