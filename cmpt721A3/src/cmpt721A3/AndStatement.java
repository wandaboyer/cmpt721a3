// CMPT 721 Assignment 3
// Authors: James Twigg and Wanda Boyer
//	    301229679       301242166

package cmpt721A3;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class AndStatement extends Statement implements Iterable<Statement>
{
	private HashSet<AtomicStatement> atoms;
	private HashMap<AtomicStatement, AllStatement> alls;
	private HashMap<AtomicStatement, ExistsStatement> exists;
	
	public AndStatement()
	{
		atoms = new HashSet<AtomicStatement>();
		alls = new HashMap<AtomicStatement, AllStatement>();
		exists = new HashMap<AtomicStatement, ExistsStatement>();
	}
	
	// Add the Atomic statement to the HashSet of atoms. 
	private void addAtomicConjunct(AtomicStatement atom)
	{
		atoms.add(atom);
	}
	
	private void addAllConjunct(AllStatement all)
	{
		AllStatement current = alls.get(all.getRole());
		if(current == null)
		{	
			// If there is no ALL in the AND with the same role,
			// add this ALL to the alls HashMap with role as key.
			alls.put(all.getRole(), all);
		}
		else
		{
			
			AndStatement and = new AndStatement();
			and.addConjunct(current.getDescription());
			and.addConjunct(all.getDescription());
			current.setDescription(and);
		}
	}
	
	private void addExistsConjunct(ExistsStatement ex)
	{
		ExistsStatement current = exists.get(ex.getRole());
		if(current == null)
		{
			// If no EXISTS exists in the AND's HashMap exists,
			// then add the new EXISTS, keyed on it's role.
			exists.put(ex.getRole(), ex);
		}
		else if(current.getQuantity() < ex.getQuantity())
		{
			// If the existing EXISTS has a larger quantity value,
			// then change it to be the more general (smaller) value.
			current.setQuantity(ex.getQuantity());
		}
	}
	
	// For collapsing nested ANDs
	private void addAndConjunct(AndStatement and)
	{
		// Iterate through the set of Atomic statements and add them
		// to the outer AND statement's atoms HashSet.
		for(AtomicStatement atom : and.atoms)
		{
			addAtomicConjunct(atom);
		}
		
		// Iterate through the collection of ALL statements and invoke
		// the addAllConjunct method to make sure they're added appropriately.
		Collection<AllStatement> otherAlls = and.alls.values();
		for(AllStatement all : otherAlls)
		{
			addAllConjunct(all);
		}

		// Iterate through the collection of EXISTS statements and invoke
		// the addExistsConjunct method to make sure they're added appropriately.		
		Collection<ExistsStatement> otherExs = and.exists.values();
		for(ExistsStatement ex : otherExs)
		{
			addExistsConjunct(ex);
		}
	}
	
	// Receive a Statement object, and according to what subclass it is an instance of,
	// invoke the correct method to add it to the current AND statement.
	public void addConjunct(Statement st)
	{
		if(st.isVacuous())
		{
			return;
		}
		
		if(st instanceof AtomicStatement)
		{
			addAtomicConjunct((AtomicStatement) st);
		}
		else if(st instanceof AndStatement)
		{
			addAndConjunct((AndStatement) st);
		}
		else if(st instanceof ExistsStatement)
		{
			addExistsConjunct((ExistsStatement) st);
		}
		else if(st instanceof AllStatement)
		{
			addAllConjunct((AllStatement) st);
		}
		else
		{
			RuntimeException rtEx = new RuntimeException("Unsupported Statement type in AndStatement.addConjunct");
			rtEx.printStackTrace();
			throw rtEx;
		}
	}
	
	
	public Statement firstConjunct()
	{
		// If the set of Atomic statements is nonempty, then the
		// first conjunct will be the from this set.
		if(! atoms.isEmpty())
		{
			return atoms.iterator().next();
		}
		
		// If the set of Atomic Statements was empty, check to see
		// if the set of EXISTS is nonempty; if so, then the first
		// conjunct will be from this set.
		if(! exists.isEmpty())
		{
			return exists.values().iterator().next();
			
		}

		// Otherwise, if both the set of Atomic Statements and EXISTS
		// are empty, then return from the list of ALLs (because of 
		// how AND statements are parsed, we are guaranteed that this is
		// nonempty).
		return alls.values().iterator().next();
	}
	
	public int size()
	{
		return atoms.size() + alls.size() + exists.size();
	}
	
	public boolean hasSizeOne()
	{
		int size = atoms.size();
		if(size > 1) return false;
		size += alls.size();
		if(size > 1) return false;
		return (size + exists.size()) == 1;
	}
	
	public ExistsStatement getExists(ExistsStatement ex)
	{
		return exists.get(ex.getRole());
	}
	
	public AllStatement getAll(AllStatement all)
	{
		return alls.get(all.getRole());
	}
	
	public boolean containsAtom(AtomicStatement atom)
	{
		return atoms.contains(atom);
	}
	
	// The empty conjunction is vacuously true.	
	@Override
	public boolean isVacuous()
	{
		return(atoms.isEmpty() && alls.isEmpty() && exists.isEmpty());
	}

	@Override
	public String toString()
	{
		String val = "[AND ";
		for(AtomicStatement atom : atoms)
		{
			val += atom + " ";
		}

		Collection<AllStatement> allVals = alls.values();
		for(AllStatement all : allVals)
		{
			val += all + " ";
		}
		
		Collection<ExistsStatement> existVals = exists.values();
		for(ExistsStatement ex : existVals)
		{
			val += ex + " ";
		}
		
		val += "]";
		
		return val;
	}

	@Override
	public Iterator<Statement> iterator()
	{
		return new AndIterator(atoms, alls, exists);
	}

	@Override
	public boolean subsumes(Statement other)
	{
		ExistsStatement otherEx;
		AllStatement otherAll;
		if(other instanceof AndStatement)
		{
			AndStatement and = (AndStatement)other;
			if(this.size() > and.size())
			{
				/* If this statement has more conjuncts than the other, then either
				 * this statement is a subset of the other, or their difference is
				 * not the empty set.*/
				return false;
			}
			
			for(Statement currentStatement : this)
			{
				if(currentStatement instanceof AtomicStatement)
				{
					if(! and.containsAtom((AtomicStatement)currentStatement))
					{
						return false;
					}
				}
				else if(currentStatement instanceof ExistsStatement)
				{
					otherEx = and.getExists((ExistsStatement) currentStatement);
					if(otherEx == null || ! currentStatement.subsumes(otherEx))
					{
						return false;
					}
				}
				else//must be an AllStatement
				{
					otherAll = and.getAll((AllStatement)currentStatement);
					if(otherAll == null || ! currentStatement.subsumes(otherAll))
					{
						return false;
					}
				}
			}
			
			return true;
		}
		
		//If the other statement is not an AND, this cannot subsume it because a normalized AND
		//has at least 2 conjuncts, so this must be more specific, or incomparable, to any non-AND
		//statement.
		return false;
	}

}
