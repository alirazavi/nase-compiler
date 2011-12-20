/**
 * 
 */
package compiler.scanner;

import java.util.StringTokenizer;

import symboltable.SymbolTable;
import symboltable.Symbols;

import compiler.NaseCompiler;

import files.FileManager;
import files.Infile;

/**
 * @author student
 * 
 */
public class Scanner extends ScannerWrapper{

	private final int CHAR_CLASS_ILLEGAL = 0;
	private final int CHAR_CLASS_BLANK = 1;
	private final int CHAR_CLASS_UPPER = 2;
	private final int CHAR_CLASS_DIGIT = 3;
	private final int CHAR_CLASS_LOWER = 4;
	private final int CHAR_CLASS_SPECIAL = 5;
	

	private String specialChars = " "+FileManager.EOF;
	private int symbolnr;
	
	private boolean exit;
	
	public Scanner() {
		exit = false;
		
		FileManager.getInstance().getEchofile().echoWriteLine("Used scanner: Scanner.java\n");
		
		FileManager.getInstance().getEchofile().echoWriteLineNr(1);
		FileManager.getInstance().getInfile().readNextChar();
	}

	public int getCurrentSymbol(){
		return symbolnr;
	}
	
	public void skipBlockEndSymbol(){
		String token;
		int symbol = getCurrentSymbol();
		
		if(symbol == Symbols.NULL_SYMBOL  ||
			 symbol == Symbols.EOF   ||
			 symbol == Symbols.DELIMITER_SYMBOL )
			return;
				
		while((token = getNextToken()).length() > 0){
			symbol = SymbolTable.getInstance().classifySymbol(token);
			if(symbol == Symbols.NULL_SYMBOL ){
				scannerDebugOutTokenSymbol(token, symbol);
				continue;
			} else if(symbol == Symbols.EOF || symbol == Symbols.END_SYMBOL){
				scannerDebugOutTokenSymbol(token, symbol);
				return;
			}
			scannerDebugOutText("Skipping...");
			scannerDebugOutTokenSymbol(token, symbol);
		}
	}
	
	public void skipToDelimiter(){
		String token;
		int symbol = getCurrentSymbol();
		
		if(symbol == Symbols.NULL_SYMBOL  ||
			 symbol == Symbols.EOF   ||
			 symbol == Symbols.DELIMITER_SYMBOL )
			return;
				
		while((token = getNextToken()).length() > 0){
			symbol = SymbolTable.getInstance().classifySymbol(token);
			if(symbol == Symbols.NULL_SYMBOL ){
				scannerDebugOutTokenSymbol(token, symbol);
				continue;
			} else if(symbol == Symbols.EOF || symbol == Symbols.DELIMITER_SYMBOL){
				scannerDebugOutTokenSymbol(token, symbol);
				return;
			}
			scannerDebugOutText("Skipping...");
			scannerDebugOutTokenSymbol(token, symbol);
		}
	}
	
	public boolean getNextSymbol(){
		if(exit){
			symbolnr = Symbols.EOF;
			return false;
		}
		String symbol = getNextToken();
		if(symbol.length() == 0){
			scannerDebugOutTokenSymbol(symbol, Symbols.NULL_SYMBOL);
			return false;
		} else {
			symbolnr = SymbolTable.getInstance().classifySymbol(symbol);
			if (symbolnr <= Symbols.NULL_SYMBOL)
				symbolnr = SymbolTable.getInstance().insertUserSymbol(symbol);
			scannerDebugOutTokenSymbol(symbol, symbolnr);
			return true;
		}
	}
	
	public int lookAheadOneSymbol() {

		int symbolNr;
		String token;

		token = getNextToken();

		if( 0 == token.length() ) {
			scannerDebugOutTokenSymbol(token, Symbols.NULL_SYMBOL );
			return Symbols.NULL_SYMBOL;
		} else {
			symbolNr = SymbolTable.getInstance().classifySymbol(token);
			if( symbolNr <= Symbols.NULL_SYMBOL ) {
				symbolNr = SymbolTable.getInstance().insertUserSymbol(token);
			}
			scannerDebugOutTokenSymbol(token, symbolNr);
			return symbolNr;
		}
	}

	
	private String getNextToken() {
		int i = 0;
		String currentToken = "";
		
		while (true) {
			switch (FileManager.getInstance().getInfile().getCurrentChar()) {
				case FileManager.EOF:
					if (i == 0)
						currentToken = new StringBuffer(currentToken).insert(0, FileManager.getInstance().getInfile().getCurrentChar()).toString();
						currentToken = (exit ? "" : currentToken);
						exit = true;
					return currentToken;
				case Infile.BLANK:
					FileManager.getInstance().getInfile().readNextChar();
					continue;
			}
			
			currentToken = new StringBuffer(currentToken).insert(i, FileManager.getInstance().getInfile().getCurrentChar()).toString();
			FileManager.getInstance().getInfile().readNextChar();
			
			if( (charClass(currentToken.charAt(i)) != charClass(FileManager.getInstance().getInfile().getCurrentChar())) &&
					!(charClass(currentToken.charAt(i)) == CHAR_CLASS_LOWER && (charClass(FileManager.getInstance().getInfile().getCurrentChar()) == CHAR_CLASS_DIGIT)))
				break;
			
			if(isSpecialChar(currentToken.charAt(i)) && isSpecialChar(FileManager.getInstance().getInfile().getCurrentChar())){
				if(isSpecialCharDoublet(currentToken.charAt(i), FileManager.getInstance().getInfile().getCurrentChar())){
					i++;
					currentToken = new StringBuffer(currentToken).insert(i, FileManager.getInstance().getInfile().getCurrentChar()).toString();
					FileManager.getInstance().getInfile().readNextChar();
					break;
				} else
					break;
			}
			i++;	 
		}
		
		return currentToken;
		
	}

	private boolean isSpecialChar(char character) {
		if (character == Infile.BLANK || character == Infile.TAB)
			return false;

		return specialChars.contains(String.valueOf(character));
	}

	
	private boolean isSpecialCharDoublet(char lastChar, char currentChar){
		if (lastChar == Infile.BLANK || lastChar == Infile.TAB)
			return false;
		
		if (currentChar == Infile.BLANK || currentChar == Infile.TAB)
			return false;
		
		String currentToken = new StringBuffer().append(lastChar).append(currentChar).toString();
		StringTokenizer tokenizer = new StringTokenizer(specialChars, NaseCompiler.TOKEN_LIST_SEPERATOR);
		while(tokenizer.hasMoreTokens()){
			if(currentToken.equals(tokenizer.nextToken()))
				return true;
		}
		return false;
	}
	
	private int charClass(char character) {
		if (character == Infile.BLANK || character == Infile.TAB)
			return CHAR_CLASS_BLANK;
		else if (Character.isUpperCase(character))
			return CHAR_CLASS_UPPER;
		else if (Character.isDigit(character))
			return CHAR_CLASS_DIGIT;
		else if (Character.isLowerCase(character))
			return CHAR_CLASS_LOWER;
		else if (isSpecialChar(character))
			return CHAR_CLASS_SPECIAL;
		else
			return CHAR_CLASS_ILLEGAL;
	}

	@Override
	public int getLine() {
		return FileManager.getInstance().getInfile().getLine();
	}

	@Override
	public int getColumn() {
		return FileManager.getInstance().getInfile().getColumn();
	}
	
	
}
