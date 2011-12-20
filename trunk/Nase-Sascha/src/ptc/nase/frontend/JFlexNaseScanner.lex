package ptc.nase.frontend;

import ptc.nase.SymbolTable;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.PrintStream;
import ptc.nase.Nase;
@SuppressWarnings("unused")

%% // -------------------------------------------------------------------------------------

%class JFlexNaseScanner
%implements IScanner
%unicode
%line
%column
%type int
%ignorecase

%public
%final

%eofval{
		return SymbolTable.ST_EOF_SYMBOL;
%eofval}

%init{
		symbolTable = SymbolTable.getInstance();
%init}

%{

	public JFlexNaseScanner(String filename) throws FileNotFoundException
	{
		this(new FileReader(filename + Nase.IN_FILE_EXTENSION));
		
		// Open echo file listing
		echoFile = new PrintStream( new File(filename + Nase.ECHO_FILE_EXTENSION));
		
		echoWriteString("Compilation listing of <" + filename + Nase.IN_FILE_EXTENSION + ">\r\n");
		echoWriteLineNr(1);
		
		Listing.init(filename);
	}

	private SymbolTable symbolTable;
	private int currentSymbol;

	private PrintStream echoFile;

	public long getCurrentLine()
	{
		return yyline + 1;
	}
	
	public long getCurrentColumn()
	{
		return yycolumn + 1;
	}
	
	public SymbolTable getSymbolTable()
	{
		return symbolTable;
	}
	
	public boolean getNextSymbol() throws IOException
	{
		currentSymbol = yylex();
		
		echoWriteString(yytext());
			
		return true;
	}
	
	public void skipToDelimiter() throws IOException
	{
		boolean delimiterFound = false;
		
		while (!delimiterFound )
		{
			getNextSymbol();
			delimiterFound = ( (currentSymbol == SymbolTable.ST_DELIMITER_SYMBOL) || (currentSymbol == SymbolTable.ST_EOF_SYMBOL) );
		}
	}
	
	public int getCurrentSymbol()
	{
		return currentSymbol;
	}

	public void echoWriteLineNr(long lineNr)
	{
		echoFile.printf("\r\n%05d: ", lineNr);
	}
	
	public void echoWriteString(String line)
	{
		echoFile.printf("%s", line);
	}
	
	public void echoWriteChar(char c)
	{
		echoFile.printf("%c", c);
	}

%}


// -------------------------------------------------------------------------------------

DELIMITER_SYMBOL 			= ";"
BEGIN_SYMBOL				= "BEGIN"
END_SYMBOL					= "END"
INT_TYPE_SYMBOL				= "INTEGER"
COMMA_SYMBOL				= ","
ASSIGN_SYMBOL				= ":="
MINUS_SYMBOL				= "-"
PLUS_SYMBOL					= "+"
TIMES_SYMBOL				= "*"
DIVIDE_SYMBOL				= "/"
MODULO_SYMBOL				= "%"
OR_SYMBOL					= "OR"
AND_SYMBOL					= "AND"
OPEN_PARENTHESIS_SYMBOL		= "("
CLOSE_PARENTHESIS_SYMBOL	= ")"
INLINE_IF_SYMBOL			= "IIF"
INLINE_FI_SYMBOL			= "FII"
INLINE_THEN_SYMBOL			= "?"
INLINE_ELSE_SYMBOL			= ":"
LT_SYMBOL					= "<"
LE_SYMBOL					= "<="
EQ_SYMBOL					= "="
GE_SYMBOL					= ">="
GT_SYMBOL					= ">"
READ_SYMBOL					= "READ"
WRITE_SYMBOL				= "WRITE"

BOOL_TYPE_SYMBOL			= "BOOLEAN"
TRUE_SYMBOL					= "TRUE"
FALSE_SYMBOL				= "FALSE"
NOT_SYMBOL					= "NOT"

LINE_TERMINATOR 			= \r|\n|\r\n
WHITESPACE 					= [" ""\t"]+

IDENTIFIER					= [A-Za-z][A-Za-z0-9]*

INTEGER						= [1-9][0-9]*

COMMENT = "$".*{LINE_TERMINATOR}

%% // -------------------------------------------------------------------------------------


{DELIMITER_SYMBOL}			{ return SymbolTable.ST_DELIMITER_SYMBOL; }
{BEGIN_SYMBOL}				{ return SymbolTable.ST_BEGIN_SYMBOL; }
{END_SYMBOL}				{ return SymbolTable.ST_END_SYMBOL; }
{INT_TYPE_SYMBOL}			{ return SymbolTable.ST_INT_TYPE_SYMBOL; }
{COMMA_SYMBOL}				{ return SymbolTable.ST_COMMA_SYMBOL; }
{ASSIGN_SYMBOL}				{ return SymbolTable.ST_ASSIGN_SYMBOL; }
{MINUS_SYMBOL}				{ return SymbolTable.ST_MINUS_SYMBOL; }
{PLUS_SYMBOL}				{ return SymbolTable.ST_PLUS_SYMBOL; }
{TIMES_SYMBOL}				{ return SymbolTable.ST_TIMES_SYMBOL; }
{DIVIDE_SYMBOL}				{ return SymbolTable.ST_DIVIDE_SYMBOL; }
{MODULO_SYMBOL}				{ return SymbolTable.ST_MODULO_SYMBOL; }
{OR_SYMBOL}					{ return SymbolTable.ST_OR_SYMBOL; }
{AND_SYMBOL}				{ return SymbolTable.ST_AND_SYMBOL; }
{OPEN_PARENTHESIS_SYMBOL}	{ return SymbolTable.ST_OPEN_PARENTHESIS_SYMBOL; }
{CLOSE_PARENTHESIS_SYMBOL}	{ return SymbolTable.ST_CLOSE_PARENTHESIS_SYMBOL; }
{INLINE_IF_SYMBOL}			{ return SymbolTable.ST_INLINE_IF_SYMBOL; }
{INLINE_FI_SYMBOL}			{ return SymbolTable.ST_INLINE_FI_SYMBOL; }
{INLINE_THEN_SYMBOL}		{ return SymbolTable.ST_INLINE_THEN_SYMBOL; }
{INLINE_ELSE_SYMBOL}		{ return SymbolTable.ST_INLINE_ELSE_SYMBOL; }
{LT_SYMBOL}					{ return SymbolTable.ST_LT_SYMBOL; }
{LE_SYMBOL}					{ return SymbolTable.ST_LE_SYMBOL; }
{EQ_SYMBOL}					{ return SymbolTable.ST_EQ_SYMBOL; }
{GE_SYMBOL}					{ return SymbolTable.ST_GE_SYMBOL; }
{GT_SYMBOL}					{ return SymbolTable.ST_GT_SYMBOL; }
{READ_SYMBOL}				{ return SymbolTable.ST_READ_SYMBOL; }
{WRITE_SYMBOL}				{ return SymbolTable.ST_WRITE_SYMBOL; }


{BOOL_TYPE_SYMBOL}			{ return SymbolTable.ST_BOOL_TYPE_SYMBOL; }
{TRUE_SYMBOL}				{ return SymbolTable.ST_TRUE_SYMBOL; }
{FALSE_SYMBOL}				{ return SymbolTable.ST_FALSE_SYMBOL; }
{NOT_SYMBOL}				{ return SymbolTable.ST_NOT_SYMBOL; }

{IDENTIFIER}				{

								int symbol = symbolTable.classifySymbol(yytext());
								if (symbol == SymbolTable.ST_NULL_SYMBOL)
								{
									symbol = symbolTable.insertUserSymbol(yytext());
								}
								
								return symbol;	
							}

{INTEGER}					{

								int symbol = symbolTable.classifySymbol(yytext());
								if (symbol == SymbolTable.ST_NULL_SYMBOL)
								{
									symbol = symbolTable.insertUserSymbol(yytext());
								}
								
								return symbol;	
							}

{LINE_TERMINATOR}			{ echoWriteLineNr(yyline + 2); }
{WHITESPACE}				{ echoWriteString(yytext()); }

{COMMENT}					{  }

. {}
