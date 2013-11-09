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
	
	public boolean hasSizeOne()
	{
		return (atoms.size() + alls.size() + exists.size()) == 1;
				
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

}
