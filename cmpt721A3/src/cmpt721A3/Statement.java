// CMPT 721 Assignment 3
// Authors: James Twigg and Wanda Boyer
//	    301229679       301242166

package cmpt721A3;

public abstract class Statement
{
	public abstract boolean isVacuous();
	public abstract String toString();
	
	public abstract boolean subsumes(Statement other);
}
