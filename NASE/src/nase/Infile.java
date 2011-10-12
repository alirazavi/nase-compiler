package nase;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("unused")
public class Infile
{
	private PrintWriter echoFileWriter;

	public static String IN_FILE_EXTENSION = ".nase";
	
	private File inFile;
	
	private long lLineNr;
	private long lColNr;
	
	public Infile(String filename)
	{
		try
		{
			echoFileWriter = new PrintWriter(new FileWriter(filename + IN_FILE_EXTENSION));
			System.out.println("Infile()");
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void echoWriteLineNr(int lineNr)
	{
		echoFileWriter.printf("%d", new Object[] {new Integer(lineNr)});
	}
	
	
	private void echoWriteLine(String line)
	{
		
	}
	
	private void echoWriteChar(char c)
	{
		
	}

	public void closeEcho()
	{
		echoFileWriter.flush();
		echoFileWriter.close();
	}
}
