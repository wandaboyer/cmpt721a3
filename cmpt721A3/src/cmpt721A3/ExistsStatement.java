package cmpt721A3;

public class ExistsStatement extends Statement
{

	private int quantity;
	private AtomicStatement role;
	
	public ExistsStatement()
	{
		quantity = 0;
		role = AtomicStatement.UNIVERSAL_CONCEPT;
	}
	
	public ExistsStatement(int quantity, AtomicStatement role)
	{
		this.quantity = quantity;
		this.role = role;
	}

	public int getQuantity()
	{
		return quantity;
	}
	
	public AtomicStatement getRole()
	{
		return role;
	}
	
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	
	public void setRole(AtomicStatement description)
	{
		this.role = description;
	}
	
	@Override
	public boolean isVacuous()
	{
		return quantity == 0;
	}

	@Override
	public String toString()
	{
		return "[EXISTS " + quantity + " " + role + "]";
	}

	@Override
	public boolean subsumes(Statement other)
	{
		ExistsStatement ex;
		if(other instanceof ExistsStatement)
		{
			ex = (ExistsStatement) other;
			if( ! ex.role.equals(this.role))
			{
				return false;
			}
			
			return (this.quantity <= ex.getQuantity());
		}
		
		if(other instanceof AndStatement)
		{
			AndStatement and = (AndStatement)other;
			ex = and.getExists(this);
			if(ex == null)
			{
				return false;
			}
			return this.quantity <= ex.getQuantity();
		}
		
		return false;
	}

}
