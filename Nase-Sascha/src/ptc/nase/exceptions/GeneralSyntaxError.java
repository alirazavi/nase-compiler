package ptc.nase.exceptions;

public class GeneralSyntaxError extends Exception 
{
	private long line;
	private long column;
	
	public GeneralSyntaxError(long sLine, long sColumn, String message)
	{
		super(message);
		line = sLine;
		column = sColumn;
	}
}
