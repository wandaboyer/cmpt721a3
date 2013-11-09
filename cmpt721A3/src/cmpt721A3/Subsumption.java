package cmpt721A3;

public class Subsumption
{
	
	public static void main(String[] args)
	{
		if(args.length < 1)
		{
			throw new RuntimeException("Not enough arguments. Must provide an input file name.");
		}
		
		DLReader reader = new DLReader(args[0]);
		
		Statement stmt1 = reader.readNextStatement();
		
		System.out.println("The first statement (normalized) is:");
		System.out.println(stmt1);
		
		Statement stmt2 = reader.readNextStatement();
		System.out.println("The second statement (normalized) is:");
		System.out.println(stmt2);
		System.out.println();
		
		boolean firstSubsSecond = stmt1.subsumes(stmt2);
		boolean secondSubsFirst = stmt2.subsumes(stmt1);

		if(firstSubsSecond && secondSubsFirst)
		{
			System.out.println("Statement 1 and Statement 2 are equivalent.");
		}
		else if(firstSubsSecond)
		{
			System.out.println("Statement 1 subsumes Statement 2.");
		}
		else if(secondSubsFirst)
		{
			System.out.println("Statement 2 subsumes Statement 1.");
		}
		else
		{
			System.out.println("Statement 1 and Statement 2 are incompatible.");
		}
		reader.close();
	}
}