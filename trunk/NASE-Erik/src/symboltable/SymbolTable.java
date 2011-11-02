/**
 * 
 */
package symboltable;

import java.util.Iterator;
import java.util.LinkedHashMap;

import files.Infile;

/**
 * @author student
 *
 */
public class SymbolTable {
	
	private static SymbolTable instance = null;
	
	public static SymbolTable getInstance(){
		if(instance == null)
			instance = new SymbolTable();
		return instance;
	}
	
  private LinkedHashMap<Integer,Symbol> symTable;
	
	private SymbolTable(){
		symTable = new LinkedHashMap<Integer,Symbol>();
		symTable.put(Symbols.NULL_SYMBOL.ordinal(), new Symbol("", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.NULL_SYMBOL.ordinal()));
		symTable.put(Symbols.EOF_SYMBOL.ordinal(), new Symbol(""+Infile.EOF, true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.EOF_SYMBOL.ordinal()));
		symTable.put(Symbols.DELIMITER_SYMBOL.ordinal(), new Symbol(";", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.DELIMITER_SYMBOL.ordinal()));
		symTable.put(Symbols.BEGIN_SYMBOL.ordinal(), new Symbol("BEGIN", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.BEGIN_SYMBOL.ordinal()));
		symTable.put(Symbols.END_SYMBOL.ordinal(), new Symbol("END", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.END_SYMBOL.ordinal()));
		symTable.put(Symbols.INT_TYPE_SYMBOL.ordinal(), new Symbol("INTEGER", false, 0, true, SymbolType.RESERVED_WORD, 0,Symbols.INT_TYPE_SYMBOL.ordinal()));
		symTable.put(Symbols.COMMA_SYMBOL.ordinal(), new Symbol(",", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.COMMA_SYMBOL.ordinal()));
		symTable.put(Symbols.ASSIGN_SYMBOL.ordinal(), new Symbol(":=", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.ASSIGN_SYMBOL.ordinal()));
		symTable.put(Symbols.MINUS_SYMBOL.ordinal(), new Symbol("-", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.MINUS_SYMBOL.ordinal()));
		symTable.put(Symbols.PLUS_SYMBOL.ordinal(), new Symbol("+", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.PLUS_SYMBOL.ordinal()));
		symTable.put(Symbols.TIMES_SYMBOL.ordinal(), new Symbol("*", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.TIMES_SYMBOL.ordinal()));
		symTable.put(Symbols.DIVIDE_SYMBOL.ordinal(), new Symbol("/", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.DIVIDE_SYMBOL.ordinal()));
		symTable.put(Symbols.MODULO_SYMBOL.ordinal(), new Symbol("%", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.MODULO_SYMBOL.ordinal()));
		symTable.put(Symbols.OR_SYMBOL.ordinal(), new Symbol("OR", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.OR_SYMBOL.ordinal()));
		symTable.put(Symbols.AND_SYMBOL.ordinal(), new Symbol("AND", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.AND_SYMBOL.ordinal()));
		symTable.put(Symbols.OPEN_PARAENTHESIS_SYMBOL.ordinal(), new Symbol("(", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.OPEN_PARAENTHESIS_SYMBOL.ordinal()));
		symTable.put(Symbols.CLOSE_PARENTHESIS_SYMBOL.ordinal(), new Symbol(")", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.CLOSE_PARENTHESIS_SYMBOL.ordinal()));
		symTable.put(Symbols.INLINE_IF_SYMBOL.ordinal(), new Symbol("IIF", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.INLINE_IF_SYMBOL.ordinal()));
		symTable.put(Symbols.INLINE_FI_SYMBOL.ordinal(), new Symbol("FII", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.INLINE_FI_SYMBOL.ordinal()));
		symTable.put(Symbols.INLINE_THEN_SYMBOL.ordinal(), new Symbol("?", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.INLINE_THEN_SYMBOL.ordinal()));
		symTable.put(Symbols.INLINE_ELSE_SYMBOL.ordinal(), new Symbol(":", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.INLINE_ELSE_SYMBOL.ordinal()));
		symTable.put(Symbols.LT_SYMBOL.ordinal(), new Symbol("<", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.LT_SYMBOL.ordinal()));
		symTable.put(Symbols.LE_SYMBOL.ordinal(), new Symbol("<=", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.LE_SYMBOL.ordinal()));
		symTable.put(Symbols.EQ_SYMBOL.ordinal(), new Symbol("=", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.EQ_SYMBOL.ordinal()));
		symTable.put(Symbols.GE_SYMBOL.ordinal(), new Symbol(">=", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.GE_SYMBOL.ordinal()));
		symTable.put(Symbols.GT_SYMBOL.ordinal(), new Symbol(">", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.GT_SYMBOL.ordinal()));
		symTable.put(Symbols.NE_SYMBOL.ordinal(), new Symbol("<>", true, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.NE_SYMBOL.ordinal()));
		symTable.put(Symbols.READ_SYMBOL.ordinal(), new Symbol("READ", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.READ_SYMBOL.ordinal()));
		symTable.put(Symbols.WRITE_SYMBOL.ordinal(), new Symbol("WRITE", false, 0, true, SymbolType.RESERVED_WORD, 0, Symbols.WRITE_SYMBOL.ordinal()));
		for(int i = symTable.size(); i < 100; i++)
			symTable.put(i, new Symbol("", false, 0, true, SymbolType.RESERVED_WORD, 0, i));
	}
	
	public int classifySymbol(String symbol){
		for(Symbol s : symTable.values()){
			if (s.getStringRepresentation().equals(symbol))
				return s.getSymbols();
		}
		return Symbols.NULL_SYMBOL.ordinal();
	}
	
	public int insertUserSymbol(String currentToken){
		int symbol = symTable.size();
		if(isDigitSequence(currentToken)){
			try{								
				symTable.put(symbol, new Symbol(currentToken, false, Integer.parseInt(currentToken), false, SymbolType.NUMBER, 0, symbol));
			} catch (NumberFormatException ex) {
				return 0;				
			}
		} else {
			symTable.put(symbol, new Symbol(currentToken, false, 0, false, SymbolType.IDENTIFIER, 0, symbol));
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
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append('\n');
		buffer.append("Symboltabledump\n");
		Iterator<Symbol> it = symTable.values().iterator();
		while(it.hasNext()){
			buffer.append(String.format("%s\n", it.next()));
		}
		return buffer.toString();
	}
	
	public boolean isAnyIdentifierSymbol(int sym){
		return symTable.containsKey(sym);
	}
}
