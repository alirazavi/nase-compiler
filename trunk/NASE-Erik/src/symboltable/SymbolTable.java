/**
 * 
 */
package symboltable;

import java.util.HashMap;

import files.Infile;

/**
 * @author student
 *
 */
public class SymbolTable {
	
	private HashMap<String,Symbol> symTable;
	
	public SymbolTable(){
		symTable = new HashMap<String,Symbol>();
		symTable.put("", new Symbol("", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.NULL_SYMBOL.ordinal()));
		symTable.put(""+Infile.EOF, new Symbol(""+Infile.EOF, true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.EOF_SYMBOL.ordinal()));
		symTable.put(";", new Symbol(";", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.DELIMITER_SYMBOL.ordinal()));
		symTable.put("BEGIN", new Symbol("BEGIN", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.BEGIN_SYMBOL.ordinal()));
		symTable.put("END", new Symbol("END", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.END_SYMBOL.ordinal()));
		symTable.put("INTEGER", new Symbol("INTEGER", false, 0, true, SymbolType.RESERVED_WORD, 0,Symbols.INT_TYPE_SYMBOL.ordinal()));
		symTable.put(",", new Symbol(",", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.COMMA_SYMBOL.ordinal()));
		symTable.put(":=", new Symbol(":=", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.ASSIGN_SYMBOL.ordinal()));
		symTable.put("-", new Symbol("-", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.MINUS_SYMBOL.ordinal()));
		symTable.put("+", new Symbol("+", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.PLUS_SYMBOL.ordinal()));
		symTable.put("*", new Symbol("*", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.TIMES_SYMBOL.ordinal()));
		symTable.put("/", new Symbol("/", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.DIVIDE_SYMBOL.ordinal()));
		symTable.put("%", new Symbol("%", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.MODULO_SYMBOL.ordinal()));
		symTable.put("OR", new Symbol("OR", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.OR_SYMBOL.ordinal()));
		symTable.put("AND", new Symbol("AND", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.AND_SYMBOL.ordinal()));
		symTable.put("(", new Symbol("(", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.OPEN_PARAENTHESIS_SYMBOL.ordinal()));
		symTable.put(")", new Symbol(")", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.CLOSE_PARENTHESIS_SYMBOL.ordinal()));
		symTable.put("IIF", new Symbol("IIF", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.INLINE_IF_SYMBOL.ordinal()));
		symTable.put("FII", new Symbol("FII", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.INLINE_FI_SYMBOL.ordinal()));
		symTable.put("?", new Symbol("?", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.INLINE_THEN_SYMBOL.ordinal()));
		symTable.put(":", new Symbol(":", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.INLINE_ELSE_SYMBOL.ordinal()));
		symTable.put("<", new Symbol("<", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.LT_SYMBOL.ordinal()));
		symTable.put("<=", new Symbol("<=", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.LE_SYMBOL.ordinal()));
		symTable.put("=", new Symbol("=", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.EQ_SYMBOL.ordinal()));
		symTable.put(">=", new Symbol(">=", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.GE_SYMBOL.ordinal()));
		symTable.put(">", new Symbol(">", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.GT_SYMBOL.ordinal()));
		symTable.put("<>", new Symbol("<>", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.NE_SYMBOL.ordinal()));
		symTable.put("READ", new Symbol("READ", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.READ_SYMBOL.ordinal()));
		symTable.put("WRITE", new Symbol("WRITE", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.WRITE_SYMBOL.ordinal()));
	}
	
	public int classifySymbol(String symbol){
		Symbol sym = symTable.get(symbol);
		return (sym != null ? sym.getSymbols() : Symbols.NULL_SYMBOL.ordinal());
	}
	
	public int insertUserSymbol(String currentToken){
		int symbol = symTable.size()+1;
		if(isDigitSequence(currentToken)){
			try{								
				symTable.put(currentToken, new Symbol(currentToken, false, Integer.parseInt(currentToken), false, SymbolType.NUMBER, 0, symbol));
			} catch (NumberFormatException ex) {
				return 0;				
			}
		} else {
			symTable.put(currentToken, new Symbol(currentToken, false, 0, false, SymbolType.IDENTIFIER, 0, symbol));
		}
		return symbol;
	}

	
	private boolean isDigitSequence(String sequence){
		for(int i = 0; i < sequence.length(); i++){
			if(!Character.isDigit(sequence.charAt(i)))
				return false;
		}
		return true;
	}
}
