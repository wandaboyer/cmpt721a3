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
		
		Subsumption sub = new Subsumption();
		
		
		
		//TODO: Subsumption.
		System.out.println(stmt1+" "+ sub.computeSubsumption(stmt1, stmt2) +" subsumed by "+ stmt2);
		
		reader.close();
	}

	private String computeSubsumption(Statement stmt1, Statement stmt2) {
		// TODO Auto-generated method stub
		return "";
	}
}