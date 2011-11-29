package ptc.nase.frontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import ptc.nase.Nase;

public class Listing 
{
	private static PrintStream listFile = null;
	
	public static void init(String filename) throws FileNotFoundException
	{
		listFile = new PrintStream( new File(filename + Nase.LIST_FILE_EXTENSION));
	}
	
	public static void write(char c) throws IOException
	{
		if (listFile != null)
			listFile.printf("%c", c);
	}
	
	public static void writeLine(String line) throws IOException
	{
		System.out.printf("%s\r\n", line);
		if (listFile != null)
			listFile.printf("%s\r\n", line);
	}
	
	public static void writeInternalError(String line) throws IOException
	{
		System.out.printf("[INTERNAL ERROR]\t%s\r\n", line);
		if (listFile != null)
			listFile.printf("[INTERNAL ERROR]\t%s\r\n", line);
	}
	
	public static void writeGeneralContextError(long line, long column, String message) throws IOException
	{
		Listing.writeLine("CONTEXT ERROR near line " + line + ", column " + column + ": " + message);
	}
	
}
