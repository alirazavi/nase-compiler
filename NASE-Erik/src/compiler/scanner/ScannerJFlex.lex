
package compiler.scanner;

import java_cup.runtime.*;
import java.io.IOException;

import files.FileManager;

import symboltable.SymbolTable;
import symboltable.Symbols;
import static symboltable.Symbols.*;

%%

%class ScannerJFlex 
%extends ScannerWrapper

%unicode
%line
%column

// %public
%final
// %abstract

%cupsym symboltable.Symbols
%cup
%cupdebug
%ignorecase

%init{
	// TODO: code that goes to constructor

	FileManager.getInstance().getEchofile().echoWriteLine("Used scanner: [generated] ScannerJFlex.java\n");
%init}

%{
	private Symbol sym(int type)
	{
		return sym(type, yytext());
	}

	private Symbol sym(int type, Object value)
	{
		return new Symbol(type, yyline, yycolumn, value);
	}

	private void error()
	throws IOException
	{
		throw new IOException("illegal text at line = "+yyline+", column = "+yycolumn+", text = '"+yytext()+"'");
	}
	
	private int currentSymbol;
	private boolean done = false;
	private StringBuffer buffer = new StringBuffer();
	
	public int getCurrentSymbol(){
		return currentSymbol;
	}
	
	public int getColumn(){
		return yycolumn + 1;
	}
	
	public int getLine(){
		return yyline + 1;
	}
	
	public boolean getNextSymbol(){
		Symbol s;
		String token;
		
		if(done){
			currentSymbol = EOF;
			return true;
		}
		
		try {
			s = next_token();
			currentSymbol = s.sym;
			if(s.sym == EOF){
				done = true;
				token = ""+FileManager.EOF;
			} else {
				token = s.value.toString();
			}
			currentSymbol = SymbolTable.getInstance().classifySymbol(token);
			if (currentSymbol <= Symbols.NULL_SYMBOL)
				currentSymbol = SymbolTable.getInstance().insertUserSymbol(token);
				
			scannerDebugOutTokenSymbol(token, currentSymbol);
		} catch (IOException e) {
			done = true;
			e.printStackTrace();
		}

		return true;
	}
	
	public void skipToDelimiter(){
		while(this.getNextSymbol()){
			if(currentSymbol == Symbols.DELIMITER_SYMBOL || currentSymbol == Symbols.EOF)
				break;
			scannerDebugOutText("Skipping...");
			scannerDebugOutTokenSymbol(yytext(), currentSymbol);
		}
	}
	
	public void skipBlockEndSymbol(){
		while(this.getNextSymbol()){
			if(currentSymbol == Symbols.EOF || currentSymbol == Symbols.END_SYMBOL)
				break;
				
			scannerDebugOutText("Skipping...");
			scannerDebugOutTokenSymbol(yytext(), currentSymbol);
		}
	}
	
	public int lookAheadOneSymbol(){
		return 0;
	}
%}

LINE_TERMINATOR = \r|\n|\r\n

DELEMITER_SYMBOL = ";"

COMMA_SYMBOL = ","
ASSIGN_SYMBOL = ":="
MINUS_SYMBOL = "-"
PLUS_SYMBOL = "+"
TIMES_SYMBOL = "*"
DIVIDE_SYMBOL = "/"
MODULO_SYMBOL = "%"



BEGIN_SYMBOL = "BEGIN"
END_SYMBOL = "END"
OR_SYMBOL = "OR"
AND_SYMBOL = "AND"
INLINE_IF_SYMBOL = "IIF"
INLINE_FI_SYMBOL = "FII"
WRITE_SYMBOL = "WRITE"
READ_SYMBOL = "READ"
TYPE_INT = "INTEGER"
TYPE_BOOL = "BOOLEAN"
TRUE_SYMBOL = "TRUE"
FALSE_SYMBOL = "FALSE"


STRING_LITERAL = [A-Za-z][A-Za-z0-9]*
INTEGER_LITERAL = [0-9]+

COMMENT = "$".*{LINE_TERMINATOR}

WHITESPACE = [" ""\t"]+
%%

{DELEMITER_SYMBOL}		{	buffer.append(yytext()); return sym(DELIMITER_SYMBOL, ";"); }

{COMMA_SYMBOL}			{	buffer.append(yytext()); return sym(COMMA_SYMBOL, ",");}
{ASSIGN_SYMBOL}			{	buffer.append(yytext()); return sym(ASSIGN_SYMBOL, ":=");}
{MINUS_SYMBOL}			{	buffer.append(yytext()); return sym(MINUS_SYMBOL, "-");}
{PLUS_SYMBOL}			{	buffer.append(yytext()); return sym(PLUS_SYMBOL, "+");}
{TIMES_SYMBOL}			{	buffer.append(yytext()); return sym(TIMES_SYMBOL, "*");}
{DIVIDE_SYMBOL}			{	buffer.append(yytext()); return sym(DIVIDE_SYMBOL, "/");}
{MODULO_SYMBOL}			{	buffer.append(yytext()); return sym(MODULO_SYMBOL, "%");}

{BEGIN_SYMBOL}			{	buffer.append(yytext()); return sym(BEGIN_SYMBOL, "BEGIN"); }
{END_SYMBOL}			{	buffer.append(yytext()); return sym(END_SYMBOL, "END"); }
{READ_SYMBOL}			{	buffer.append(yytext()); return sym(READ_SYMBOL, "READ");}
{WRITE_SYMBOL}			{	buffer.append(yytext()); return sym(WRITE_SYMBOL, "WRITE");}
{OR_SYMBOL}				{	buffer.append(yytext()); return sym(OR_SYMBOL, "OR");}
{AND_SYMBOL}			{	buffer.append(yytext()); return sym(AND_SYMBOL, "AND");}
{INLINE_IF_SYMBOL}		{	buffer.append(yytext()); return sym(INLINE_IF_SYMBOL, "IIF");}
{INLINE_FI_SYMBOL}		{	buffer.append(yytext()); return sym(INLINE_FI_SYMBOL, "FII");}
{TYPE_INT}				{	buffer.append(yytext()); return sym(INT_TYPE_SYMBOL, "INTEGER");}
{TYPE_BOOL}				{	buffer.append(yytext()); return sym(BOOL_TYPE_SYMBOL, "BOOL");}
{TRUE_SYMBOL}			{	buffer.append(yytext()); return sym(TRUE_SYMBOL, "TRUE");}
{FALSE_SYMBOL}			{	buffer.append(yytext()); return sym(FALSE_SYMBOL, "FALSE");}
	
{WHITESPACE}			{	/* ignored */ 
							buffer.append(yytext()); }
{LINE_TERMINATOR}		{	/* ignored */  
							FileManager.getInstance().getEchofile().echoWriteLineNr(yyline+1);
							FileManager.getInstance().getEchofile().echoWriteLine(buffer.toString());
							buffer = new StringBuffer();
						}
{COMMENT}				{	/* ignored */
							FileManager.getInstance().getEchofile().echoWriteLineNr(yyline+1); 
							FileManager.getInstance().getEchofile().echoWriteLine(yytext().substring(0, yytext().length()-1));
							buffer = new StringBuffer();}
{STRING_LITERAL}		{ 	buffer.append(yytext());
							return sym(STRING_LITERAL, yytext());
						}
{INTEGER_LITERAL}		{ 	buffer.append(yytext()); return sym(INTEGER_LITERAL, new Integer(yytext()));}

<<EOF>>					{	FileManager.getInstance().getEchofile().echoWriteLineNr(yyline+1);
							FileManager.getInstance().getEchofile().echoWriteLine(buffer.toString()+"\n");
							return sym(EOF, null);
						}
.						{error();}

