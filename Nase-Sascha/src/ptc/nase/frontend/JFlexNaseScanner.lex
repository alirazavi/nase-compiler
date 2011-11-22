package ptc.nase.frontend;

import ptc.nase.SymbolTable;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import ptc.nase.Nase;

%% // -------------------------------------------------------------------------------------

%class JFlexNaseScanner
%implements IScanner
%unicode
%line
%column
%type int

%public
%final

%eofval{
		return SymbolTable.ST_EOF_SYMBOL;
%eofval}

%init{
		symbolTable = new SymbolTable();
%init}

%{

	public JFlexNaseScanner(String filename) throws FileNotFoundException
	{
		this(new FileReader(filename + Nase.IN_FILE_EXTENSION));
		Listing.init(filename);
	}

	private SymbolTable symbolTable;
	private int currentSymbol;

	public long getCurrentLine()
	{
		return yycolumn + 1;
	}
	
	public long getCurrentColumn()
	{
		return yyline + 1;
	}
	
	public SymbolTable getSymbolTable()
	{
		return symbolTable;
	}
	
	public boolean getNextSymbol() throws IOException
	{
		currentSymbol = yylex();
			
		return true;
	}
	
	public void skipToDelimiter()
	{
		//TODO
		System.out.println("TODO: skipToDelimiter");
	}
	
	public int getCurrentSymbol()
	{
		return currentSymbol;
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

LINE_TERMINATOR 			= \r|\n|\r\n
WHITESPACE 					= [" ""\t"]+

IDENTIFIER					= [A-Za-z][A-Za-z0-9]*

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

{IDENTIFIER}				{

								int symbol = symbolTable.classifySymbol(yytext());
								if (symbol == SymbolTable.ST_NULL_SYMBOL)
								{
									symbol = symbolTable.insertUserSymbol(yytext());
								}
								
								return symbol;	
							}

{LINE_TERMINATOR}			{  }
{WHITESPACE}				{  }

.	{ }

