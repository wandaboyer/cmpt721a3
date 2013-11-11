// CMPT 721 Assignment 3
// Authors: James Twigg and Wanda Boyer
//	    301229679       301242166

package cmpt721A3;

public class AtomicStatement extends Statement
{
	public static final String UNIVERSAL_CONCEPT_NAME = "Thing";
	public static final AtomicStatement UNIVERSAL_CONCEPT = new AtomicStatement(UNIVERSAL_CONCEPT_NAME);
	public static final AtomicStatement EMPTY_CONCEPT = new AtomicStatement("");
	
	private String name;
	
	public AtomicStatement(String name)
	{
		this.name = name;
	}
	
	// Equality is based on the name being the same, and is case sensitive.
	public boolean equals(AtomicStatement other)
	{
		return name.equals(other.getName());
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public boolean isVacuous()
	{
		return name.equalsIgnoreCase(UNIVERSAL_CONCEPT_NAME);
	}

	@Override
	public String toString()
	{
		return name;
	}

	// An atomic statement
	@Override
	public boolean subsumes(Statement other)
	{
		if(this.equals(UNIVERSAL_CONCEPT))
		{
			// Thing subsumes all other atomic concepts.
			return true;
		}
		
		if(other instanceof AtomicStatement)
		{
			// An atomic statement subsumes another atomic
			// statement only when they are equal.
			return this.equals((AtomicStatement)other);
		}
		
		else if(other instanceof AndStatement)
		{
			AndStatement and = (AndStatement)other;
		// If there is an Atomic statement that matches within
		// an AND statement, then because the conjunction with
		// more conditions makes the AND more specicfic, the
		// Atomic statement will subsume the AND.
			return and.containsAtom(this);
		}
		// Atomic statements are incompatible with EXISTS and ALL statements.
		return false;
	}

}
