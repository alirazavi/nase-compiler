package ptc.nase.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import ptc.nase.Console;
import ptc.nase.Nase;
import ptc.nase.SymbolTable;

@SuppressWarnings("unused")
public class Scanner implements IScanner
{
	
	private final char CHAR_CLASS_ILLEGAL = 0;
	private final char CHAR_CLASS_BLANK = 1;
	private final char CHAR_CLASS_UPPER = 2;
	private final char CHAR_CLASS_DIGIT = 3;
	private final char CHAR_CLASS_LOWER = 4;
	private final char CHAR_CLASS_SPECIAL = 5;
	
	
	private List<String> specialChars;
	
	private final boolean DEBUG_SCANNER = false;
	
	private Infile infile;
	private SymbolTable symbolTable;
	
	private int currentSymbol;
	
	public Scanner(String filename) throws FileNotFoundException
	{
		infile = new Infile(filename);
		symbolTable = new SymbolTable();
		
		Listing.init(filename);
		
		specialChars = symbolTable.getSpecialCharList();
	}
	
	public long getCurrentLine()
	{
		return infile.currentLineNr();
	}
	
	public long getCurrentColumn()
	{
		return infile.currentColumnNr();
	}
	
	public SymbolTable getSymbolTable()
	{
		return symbolTable;
	}
	
	private int charClass(char c)
	{
		if ( (c == Nase.IF_BLANK_CHAR) ||(c == Nase.IF_TAB_CHAR))
		{
			return CHAR_CLASS_BLANK;
		}
		if ( Character.isUpperCase(c))
		{
			return CHAR_CLASS_UPPER;
		}
		if ( Character.isDigit(c) )
		{
			return CHAR_CLASS_DIGIT;
		}
		if ( Character.isLowerCase(c) )
		{
			return CHAR_CLASS_LOWER;
		}
		if ( isSpecialChar(c) )
		{
			return CHAR_CLASS_SPECIAL;
		}
		
		return CHAR_CLASS_ILLEGAL;
	}
	
	private void debugOutText(String message)
	{
		if (DEBUG_SCANNER)
		{
			Console.display(message);
		}
	}
	
	private void debugOutTokenSymbol(String tokenString, int symbol)
	{
		if (DEBUG_SCANNER)
		{
			Console.display("Scanned symbol: " + tokenString + " has ID: " + symbol);
		}
	}
	
	private String getNextToken() throws IOException
	{
		String currentToken = "";
		int i = 0;
		
		while(true)
		{
			if (infile.currentChar() == Nase.IF_EOF_CHAR)
			{
				if (i == 0)
				{
					currentToken = "" + infile.currentChar();
				}
				break;
			}
			
			// skip leading blanks
			if (infile.currentChar() == Nase.ST_BLANK_CHAR)
			{
				infile.readNextChar();
				continue;
			}
			
			currentToken += infile.currentChar();
			infile.readNextChar();
			
			if ( 
					(charClass(currentToken.charAt(i)) != charClass(infile.currentChar())) &&
					!(
							charClass(currentToken.charAt(i)) == CHAR_CLASS_LOWER &&
							charClass(infile.currentChar()) == CHAR_CLASS_DIGIT
					 )
				)
			{
				break;
			}
			
			if ( isSpecialChar(currentToken.charAt(i)) && isSpecialChar(infile.currentChar()) )
			{
				if (isSpecialChar(currentToken.charAt(i), infile.currentChar()))
				{
					i++;
					currentToken += infile.currentChar();
					infile.readNextChar();
					break;
				}
				else
				{
					break;
				}
			}
			
			i++;
		}
	
		return currentToken;
	}
	
	
	private boolean isSpecialChar(char c)
	{
		if ( (c == Nase.IF_BLANK_CHAR) || (c == Nase.IF_TAB_CHAR) )
		{
			return false;
		}
		
		String cStr = Character.toString(c);
		
		for (int i = 0; i < specialChars.size(); i++)
		{
			String s = specialChars.get(i);
			
			if (specialChars.get(i).equals(cStr))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isSpecialChar(char lastChar, char thisChar)
	{
		if ( (lastChar == Nase.IF_BLANK_CHAR) || (lastChar == Nase.IF_TAB_CHAR) )
		{
			return false;
		}
		if ( (thisChar == Nase.IF_BLANK_CHAR) || (thisChar == Nase.IF_TAB_CHAR) )
		{
			return false;
		}
		
		String cStr = "" + lastChar + thisChar;
		
		for (int i = 0; i < specialChars.size(); i++)
		{
			String s = specialChars.get(i);
			
			if (specialChars.get(i).equals(cStr))
			{
				return true;
			}
		}		
		
		return false;
	}
	
	public boolean getNextSymbol() throws IOException
	{
		String currentToken = getNextToken();
		
		if (currentToken.length() == 0)
		{
			debugOutTokenSymbol(currentToken, SymbolTable.ST_NULL_SYMBOL);
			return false;
		}
		else
		{
			currentSymbol = symbolTable.classifySymbol(currentToken);
			
			if (currentSymbol == SymbolTable.ST_NULL_SYMBOL)
			{
				currentSymbol = symbolTable.insertUserSymbol(currentToken);
			}
			
			debugOutTokenSymbol(currentToken, currentSymbol);
			
			return true;
		}
	}
	
	/***
	* Yields current symbol of input stream
	**/
	public int getCurrentSymbol()
	{
		return currentSymbol;
	}
	
	public void dumpSymbolTable()
	{
		symbolTable.dumpSymbolTable();
	}
	
	
}
