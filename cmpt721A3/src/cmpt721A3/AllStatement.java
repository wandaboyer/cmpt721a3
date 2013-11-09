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

	@Override
	public boolean subsumes(Statement other)
	{
		if(other instanceof AllStatement)
		{
			AllStatement all = (AllStatement)other;
			if(! all.getRole().equals(role))
			{
				return false;
			}
			return this.description.subsumes(all.getDescription());
		}
		
		//TODO: other cases
		return false;
	}

}
