// CMPT 721 Assignment 3
// Authors: James Twigg and Wanda Boyer
//	    301229679       301242166

package cmpt721A3;

public class AllStatement extends Statement {

	private AtomicStatement role;
	private Statement description;
	
	public AllStatement()
	{
		role = AtomicStatement.UNIVERSAL_CONCEPT;
		description = AtomicStatement.UNIVERSAL_CONCEPT;
	}
	
	public AllStatement(AtomicStatement role, Statement description)
	{
		this.role = role;
		this.description = description;
	}

	public void setRole(AtomicStatement r)
	{
		role = r;
	}
	
	public void setDescription(Statement des)
	{
		description = des;
	}
	
	public Statement getDescription()
	{
		return description;
	}
	
	public AtomicStatement getRole()
	{
		return role;
	}
	
	@Override
	public boolean isVacuous()
	{
		return description.equals(AtomicStatement.UNIVERSAL_CONCEPT);
	}

	@Override
	public String toString()
	{
		return "[ALL :" + role + " " + description + "]";
	}
	
	// Determine if the current AllStatement subsumes another statement. The only
	// two valid cases are where the other statement is an ALL or an AND, which
	// must contain a compatible ALL statement.
	@Override
	public boolean subsumes(Statement other)
	{
		AllStatement all;
		if(other instanceof AllStatement)
		{
			// Two ALL statements are comparable only if their roles match.
			all = (AllStatement)other;
			if(! all.getRole().equals(role))
			{
				return false;
			}
			// Does the current ALL statement's description subsume the 
			// description of the other ALL statement?
			return this.description.subsumes(all.getDescription());
		}
		
		if(other instanceof AndStatement)
		{
			AndStatement and = (AndStatement)other;
			// Look for an ALL in the list of ALLs within the AND statement
			// with a compatible role.
			all = and.getAll(this);
			if(all == null)
			{
				// if there is no such ALL, then this ALL cannot subsume the AND.
				return false;
			}
			// The description of the object returned must be subsumed by this
			// ALLs description. 
			return this.description.subsumes(all.getDescription());
		}
		
		return false;
	}

}
