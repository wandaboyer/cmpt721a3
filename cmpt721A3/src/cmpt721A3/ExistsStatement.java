// CMPT 721 Assignment 3
// Authors: James Twigg and Wanda Boyer
//	    301229679       301242166

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
		// If the quantity of the EXISTS is 0, then that means that
		// "all x such that at least 0 objects are r-related to by x",
		// which is vacuous regardless of your role r.
		return (quantity == 0);
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
				// If the role is not the same, then the two EXISTS
				// are incompatible.
				return false;
			}
			
			// The current EXISTS subsumes the other EXISTS only when 
			// it's quantity is less than or equal to the other's quantity,
			// making it a more general concept.
			return (this.quantity <= ex.getQuantity());
		}
		
		if(other instanceof AndStatement)
		{
			AndStatement and = (AndStatement)other;
			ex = and.getExists(this);
			if(ex == null)
			{
				// If there is no compatible EXISTS as a conjunct
				// within the AND, then the current EXISTS cannot
				// subsume the AND.
				return false;
			}
			// If there is a compatible EXISTS within the AND, then whether
			// the current EXISTS subsumes the AND depends on whether the 
			// quantity of this EXISTS is less than or equal to the other
			// EXISTS within the AND.
			return this.quantity <= ex.getQuantity();
		}
		
		// EXISTS is incompatible with Atomic statements and ALLs.
		return false;
	}

}
