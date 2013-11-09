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
		state = 0;
		
		atomItr = atoms.iterator();
		allItr = alls.values().iterator();
		existItr = exists.values().iterator();
	}
	
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

	@Override
	public Statement next()
	{
		switch(state)
		{
			case IN_ATOMS:
			{
				if(hasNext())
				{
					return atomItr.next();
				}
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

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("Remove is not supported in AndStatement.");

	}

}
