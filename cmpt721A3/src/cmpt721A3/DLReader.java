package cmpt721A3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class DLReader
{
	private static final String KWD_EXISTS = "EXISTS";
	private static final String KWD_AND = "AND";
	
	private HashMap<String, AtomicStatement> atoms;
	private BufferedReader bfr;
	
	public DLReader(String fileName)
	{
		atoms = new HashMap<String, AtomicStatement>();
		
		try
		{
			bfr = new BufferedReader(new FileReader(fileName));
		}
		catch (FileNotFoundException fnfEx)
		{
			fnfEx.printStackTrace();
			throw new RuntimeException("Input file " + fileName + " was not found.", fnfEx);
		}
	}
	
	public void close()
	{
		try
		{
			bfr.close();
		}
		catch (IOException e)
		{
			System.err.print("Failed to close the input file.");
			e.printStackTrace();
		}
	}
	
	private AtomicStatement getAtomicStatement(String name)
	{
		AtomicStatement atom = atoms.get(name);
		if(atom == null)
		{
			atom = new AtomicStatement(name);
			atoms.put(name, atom);
		}
		return atom;
	}
	
	private AllStatement parseAllStatement(String str)
	{
		int roleStart = str.indexOf(":")+1;
		String[] parts = str.substring(roleStart).split("[\\s\\[]", 2);
		if(parts.length < 2)
		{
			throw new RuntimeException("Parse Error: Could not find the end of the role in all statement:\n" + str);
		}
		
		AtomicStatement role = getAtomicStatement(parts[0].trim());
		String descStr = str.substring(roleStart + parts[0].length()).trim();
		Statement description = parseStatement(descStr);
		
		AllStatement all = new AllStatement(role, description);
		return all;
	}
	
	/**
	 * Parses a String containing a DL sentence into a Statement object 
	 * @param str A String of the form "[AND.*]"
	 * @return If the statement has exactly 1 conjunct, the conjunct alone is
	 * returned, and may not be an AndStatement. Otherwise an AndStatement is
	 * returned, which may contain 0 conjuncts. 
	 */
	private Statement parseAndStatement(String str)
	{
		LinkedList<String> conjuncts = new LinkedList<String>();
		int start = -1;
		int depth = 0;
		//String is of the form "[AND.*", so skip these 4 characters
		for(int i = KWD_AND.length()+1; i < str.length(); ++i)
		{
			char c = str.charAt(i);
			if(" \t][".indexOf(c) != -1 && depth == 0 && start != -1)
			{
				//the end of an atom
				conjuncts.add(str.substring(start, i));
				start = -1;
			}
			else if(c == '[')
			{
				//The start of a sub-statement
				if(depth == 0)
				{
					if(start != -1)
					{
						conjuncts.add(str.substring(start, i));
					}
					start = i;
				}
				++depth;
			}
			else if(c == ']')
			{
				//The end of a sub-statement
				--depth;
				if(depth == 0)
				{
					conjuncts.add(str.substring(start, i+1));
					start = -1;
				}
			}
			else if(" \t:".indexOf(c) == -1 && depth == 0 && start == -1)
			{
				//Any other character is the beginning of an atom, so mark start
				start = i;
			}
		}
		if(conjuncts.size() == 1)
		{
			return parseStatement(conjuncts.getFirst());
		}
		AndStatement and = new AndStatement();
		for(String conj : conjuncts)
		{
			and.addConjunct(parseStatement(conj));
		}
		
		//It is possible that after adding all the conjuncts, that only one remains
		//This could be the result of removing duplicate or vacuous conjuncts
		if(and.hasSizeOne())
		{
			return and.firstConjunct();
		}
		
		return and;
	}

	/**
	 * Parses a DL statement into a Statement object
	 * @param str The statement, must be of the form "[EXISTS.*]" 
	 * @return The statement
	 */
	private ExistsStatement parseExistsStatement(String str)
	{
		//String is of the form "[EXISTS.*"
		String[] parts = str.substring(KWD_EXISTS.length()+1).trim().split("\\s", 2);
		if(parts.length < 2)
		{
			throw new RuntimeException("Parse Error: Could not find the end of the quantity in Exists statement:\n" + str);
		}
		
		int quantity = 0;
		try
		{
			quantity = Integer.parseInt(parts[0]);
		}
		catch(NumberFormatException nfEx)
		{
			throw new RuntimeException("Parse Error: failed to interpret quantity as integer: " + parts[0], nfEx);
		}
		
		AtomicStatement role = parseAtomicStatement(parts[1]);
		
		return new ExistsStatement(quantity, role);
	}
	
	private AtomicStatement parseAtomicStatement(String str)
	{
		String[] parts = str.split("[\\s:\\]\\[]", 2);
		return getAtomicStatement(parts[0].trim());
	}
	
	private Statement parseStatement(String str)
	{
		Statement stmt;
		if(str.startsWith("[ALL"))
		{
			stmt = parseAllStatement(str);
		}
		else if(str.startsWith("[EXISTS"))
		{
			stmt = parseExistsStatement(str);
		}
		else if(str.startsWith("[AND"))
		{
			stmt = parseAndStatement(str);
		}
		else
		{
			stmt = parseAtomicStatement(str);
		}
		
		return stmt;
	}
	

	/**
	 * Returns a representation of the next sentence in the input file.
	 * @return A Statement object
	 * @throws A RuntimeException if there is any error reading the file,
	 *  or incorrect formatting is detected.
	 */
	public Statement readNextStatement()
	{
		//BasicTree<String> tree = new BasicTree<String>(null);
		
		boolean balanced = false;
		try
		{
			String stmt = "";
			String line = null;
			while(!balanced)
			{
				line = bfr.readLine();
				if(line == null)
				{
					throw new RuntimeException("The end of the file was reached before the end of the statement.");
				}
				line = line.trim();
				int bracketDepth = 0;
				
				for(int i = 0; i < line.length(); ++i)
				{
					switch(line.charAt(i))
					{
						case '[':
						{
							++bracketDepth;
							break;
						}
						case ']':
						{
							--bracketDepth;
							if(bracketDepth < 0 || (bracketDepth == 0 && i != line.length()-1))
							{
								throw new RuntimeException("Unbalanced Parentheses detected at position " + i + " of the input string:\n" + line);
							}
							break;
						}
						default:
						{
							//do nothing
							break;
						}
					}
				}
				
				stmt += line;
				balanced = (bracketDepth == 0);
			}
			
			Statement st = parseStatement(stmt);
			return st.isVacuous() ? AtomicStatement.EMPTY_CONCEPT : st;
		}
		catch (IOException ioEx)
		{
			ioEx.printStackTrace();
			throw new RuntimeException("Error reading input file, abort.", ioEx);
		}
	}
}