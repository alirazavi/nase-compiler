/**
 * 
 */
package symboltable;

import java.util.Iterator;
import java.util.LinkedHashMap;

import syntaxtree.nodes.Node;
import files.FileManager;

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
		symTable.put(Symbols.NULL_SYMBOL , new Symbol("", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.NULL_SYMBOL ));
		symTable.put(Symbols.EOF , new Symbol(""+FileManager.EOF, true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.EOF ));
		symTable.put(Symbols.DELIMITER_SYMBOL , new Symbol(";", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.DELIMITER_SYMBOL ));
		symTable.put(Symbols.BEGIN_SYMBOL , new Symbol("BEGIN", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.BEGIN_SYMBOL ));
		symTable.put(Symbols.END_SYMBOL , new Symbol("END", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.END_SYMBOL ));
		symTable.put(Symbols.INT_TYPE_SYMBOL , new Symbol("INTEGER", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.INT_TYPE_SYMBOL ));
		symTable.put(Symbols.COMMA_SYMBOL , new Symbol(",", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.COMMA_SYMBOL ));
		symTable.put(Symbols.ASSIGN_SYMBOL , new Symbol(":=", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.ASSIGN_SYMBOL ));
		symTable.put(Symbols.MINUS_SYMBOL , new Symbol("-", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.MINUS_SYMBOL ));
		symTable.put(Symbols.PLUS_SYMBOL , new Symbol("+", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.PLUS_SYMBOL ));
		symTable.put(Symbols.TIMES_SYMBOL , new Symbol("*", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.TIMES_SYMBOL ));
		symTable.put(Symbols.DIVIDE_SYMBOL , new Symbol("/", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.DIVIDE_SYMBOL ));
		symTable.put(Symbols.MODULO_SYMBOL , new Symbol("%", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.MODULO_SYMBOL ));
		symTable.put(Symbols.OR_SYMBOL , new Symbol("OR", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.OR_SYMBOL ));
		symTable.put(Symbols.AND_SYMBOL , new Symbol("AND", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.AND_SYMBOL ));
		symTable.put(Symbols.OPEN_PARAENTHESIS_SYMBOL , new Symbol("(", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.OPEN_PARAENTHESIS_SYMBOL ));
		symTable.put(Symbols.CLOSE_PARENTHESIS_SYMBOL , new Symbol(")", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.CLOSE_PARENTHESIS_SYMBOL ));
		symTable.put(Symbols.INLINE_IF_SYMBOL , new Symbol("IIF", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.INLINE_IF_SYMBOL ));
		symTable.put(Symbols.INLINE_FI_SYMBOL , new Symbol("FII", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.INLINE_FI_SYMBOL ));
		symTable.put(Symbols.INLINE_THEN_SYMBOL , new Symbol("?", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.INLINE_THEN_SYMBOL ));
		symTable.put(Symbols.INLINE_ELSE_SYMBOL , new Symbol(":", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.INLINE_ELSE_SYMBOL ));
		symTable.put(Symbols.LT_SYMBOL , new Symbol("<", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.LT_SYMBOL ));
		symTable.put(Symbols.LE_SYMBOL , new Symbol("<=", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.LE_SYMBOL ));
		symTable.put(Symbols.EQ_SYMBOL , new Symbol("=", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.EQ_SYMBOL ));
		symTable.put(Symbols.GE_SYMBOL , new Symbol(">=", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.GE_SYMBOL ));
		symTable.put(Symbols.GT_SYMBOL , new Symbol(">", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.GT_SYMBOL ));
		symTable.put(Symbols.NE_SYMBOL , new Symbol("<>", true, 0, true, SymbolType.RESERVED_WORD, null, Symbols.NE_SYMBOL ));
		symTable.put(Symbols.READ_SYMBOL , new Symbol("READ", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.READ_SYMBOL ));
		symTable.put(Symbols.WRITE_SYMBOL , new Symbol("WRITE", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.WRITE_SYMBOL ));
		symTable.put(Symbols.BOOL_TYPE_SYMBOL , new Symbol("BOOL", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.BOOL_TYPE_SYMBOL ));
		symTable.put(Symbols.TRUE_SYMBOL , new Symbol("TRUE", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.TRUE_SYMBOL ));
		symTable.put(Symbols.FALSE_SYMBOL , new Symbol("FALSE", false, 0, true, SymbolType.RESERVED_WORD, null, Symbols.FALSE_SYMBOL ));
		for(int i = symTable.size(); i < 100; i++)
			symTable.put(i, new Symbol("", false, 0, true, SymbolType.RESERVED_WORD, null, i));
	}
	
	public int classifySymbol(String symbol){
		for(Symbol s : symTable.values()){
			if (s.getStringRepresentation().equals(symbol))
				return s.getSymbols();
		}
		return Symbols.NULL_SYMBOL ;
	}
	
	public int insertUserSymbol(String currentToken){
		int symbol = symTable.size();
		if(isDigitSequence(currentToken)){
			try{								
				symTable.put(symbol, new Symbol(currentToken, false, Integer.parseInt(currentToken), false, SymbolType.NUMBER, null, symbol));
			} catch (NumberFormatException ex) {
				return 0;				
			}
		} else {
			symTable.put(symbol, new Symbol(currentToken, false, 0, false, SymbolType.IDENTIFIER, null, symbol));
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
		Symbol s = symTable.get(sym);
		return s != null && s.getType() == SymbolType.IDENTIFIER;
	}
	
	public boolean isAnyNumericSymbol(int sym){
		Symbol s = symTable.get(sym);
		return s != null && s.getType() == SymbolType.NUMBER;
		
	}
	
	public Node getNodeLink(int sym){
		return symTable.get(sym).getNodeLink();
	}
	
	public void setNodeLink(int sym, Node node){
		symTable.get(sym).setNodeLink(node);
	}
}
