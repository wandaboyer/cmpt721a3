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

}
