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
	
	private void addAtomicConjunct(AtomicStatement atom)
	{
		atoms.add(atom);
	}
	
	private void addAllConjunct(AllStatement all)
	{
		AllStatement current = alls.get(all.getRole());
		if(current == null)
		{
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
			exists.put(ex.getRole(), ex);
		}
		else if(current.getQuantity() < ex.getQuantity())
		{
			current.setQuantity(ex.getQuantity());
		}
	}
	
	private void addAndConjunct(AndStatement and)
	{
		for(AtomicStatement atom : and.atoms)
		{
			addAtomicConjunct(atom);
		}
		
		Collection<AllStatement> otherAlls = and.alls.values();
		for(AllStatement all : otherAlls)
		{
			addAllConjunct(all);
		}
		
		Collection<ExistsStatement> otherExs = and.exists.values();
		for(ExistsStatement ex : otherExs)
		{
			addExistsConjunct(ex);
		}
	}
	
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
		if(! atoms.isEmpty())
		{
			return atoms.iterator().next();
			//Iterator<AtomicStatement> itr = atoms.iterator();
			//return itr.hasNext() ? itr.next() : null;
		}
		
		if(! alls.isEmpty())
		{
			return alls.values().iterator().next();
		}
		
		return exists.values().iterator().next();
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
