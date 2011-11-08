package ptc.nase.frontend;

import java.io.*;

import java.io.IOException;

import ptc.nase.Nase;

public class Infile
{
	private PrintStream echoFile;
	private PushbackReader inFile;
	
	private long lineNr;
	private long colNr;
	
	private char currentChar;
	
	public Infile(String filename)
	{
		try 
		{
			// Open input channel
			FileInputStream fileInStream = new FileInputStream (filename + Nase.IN_FILE_EXTENSION);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInStream);
			
			inFile = new PushbackReader(inputStreamReader);
			
			// Open echo file listing
			echoFile = new PrintStream( new File(filename + Nase.ECHO_FILE_EXTENSION));
			
			echoWriteLine("Compilation listing of <" + filename + Nase.IN_FILE_EXTENSION + ">\r\n");
			echoWriteLine("zzzzz:          11111111112222222222333333333344444444445555555555666666666677777777778");
			echoWriteLine("eeeee: 12345678901234567890123456789012345678901234567890123456789012345678901234567890");
		
			colNr = 1;
			lineNr = 1;
			
			echoWriteLineNr(lineNr);
			
			readNextChar();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void readNextChar() throws IOException
	{
		currentChar = (char) inFile.read();
		colNr++;
		
		if ( currentChar == (char) 65535 ) // EOF
		{
			currentChar = Nase.IF_EOF_CHAR;
		}
		else
		{
			echoWriteChar(currentChar);
		}
		
		if (currentChar == Nase.IF_TAB_CHAR)
		{
			currentChar = Nase.IF_BLANK_CHAR;
			colNr += Nase.BA_I_TABSIZE;
		} 
		else if (currentChar == '\n')
		{
			currentChar = Nase.IF_BLANK_CHAR;
			colNr = 1;
			lineNr++;
			echoWriteLineNr(lineNr);
		}
		else if (currentChar == '\r')
		{
			currentChar = Nase.IF_BLANK_CHAR;
			colNr = 1;
			// readNextChar();
		}
		else if (currentChar == '$')
		{
			currentChar = (char) inFile.read();
			
			while (currentChar != '\n' && currentChar != '\r' && (currentChar != (char) 65535))
			{
				
				if (currentChar == Nase.IF_TAB_CHAR)
				{
					colNr += Nase.BA_I_TABSIZE;
				}
				else
				{
					colNr++;
				}
				
				echoWriteChar(currentChar);
				
				currentChar = (char) inFile.read();
			}
			
			if ( currentChar == (char) 65535 ) // EOF
			{
				currentChar = Nase.IF_EOF_CHAR;
				return;
			}
			else
			{
				echoWriteChar(currentChar);
				
				if (currentChar == '\n')
				{
					currentChar = Nase.IF_BLANK_CHAR;
					colNr = 1;
					lineNr++;
					
					echoWriteLineNr(lineNr);
					
				}
				else if (currentChar == '\r')
				{
					currentChar = Nase.IF_BLANK_CHAR;
					colNr = 1;
					
					// readNextChar(); // test unreadChar
				}
				
			}	
		}
	}
	
	public void unreadChar() throws IOException
	{
		if (currentChar != Nase.IF_EOF_CHAR)
		{
			inFile.unread(currentChar);
			colNr--;
		}
	}
	
	public void echoWriteLineNr(long lineNr)
	{
		echoFile.printf("%05d: ", lineNr);
	}
	
	public void echoWriteLine(String line)
	{
		echoFile.printf("%s\r\n", line);
	}
	
	public void echoWriteChar(char c)
	{
		echoFile.printf("%c", c);
	}
	
	public char currentChar()
	{
		return currentChar;
	}
	
	public char oneCharLockAheadChar() throws IOException
	{
		char nextChar;
		char savedChar;
		
		savedChar = currentChar;
		
		readNextChar();
		nextChar = currentChar;
		
		currentChar = savedChar;
		unreadChar();			
		
		return nextChar;
	}
	
	public long currentLineNr()
	{
		return lineNr;
	}
	
	public long currentColumnNr()
	{
		return colNr;
	}
}
