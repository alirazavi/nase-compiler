package ptc.nase.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import ptc.nase.Nase;

public class CodeFile
{
	private static PrintStream codeFile = null;
	
	public static void init(String filename) throws FileNotFoundException
	{
		codeFile = new PrintStream( new File(filename + Nase.MMIX_FILE_EXTENSION));
	}
	
	public static void writeCode(String code) throws IOException
	{
		if (codeFile != null)
			codeFile.printf("%s", code);
	}
}
