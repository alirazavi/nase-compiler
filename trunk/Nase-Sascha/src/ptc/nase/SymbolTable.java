package ptc.nase;

import java.util.List;
import java.util.ArrayList;

import ptc.nase.syntaxtree.nodes.SyntaxtreeNode;

public class SymbolTable 
{
	/*
	 * Special chars
	 */
	public final char ST_BLANK_CHAR = ' ';
	public final char ST_TAB_CHAR = 0x09;
	
	/*
	 * Symbols
	 */
	public static final int ST_NULL_SYMBOL = 0;
	public static final int ST_EOF_SYMBOL = 1;
	public static final int ST_DELIMITER_SYMBOL = 2;
	public static final int ST_BEGIN_SYMBOL = 3;
	public static final int ST_END_SYMBOL = 4;
	public static final int ST_INT_TYPE_SYMBOL = 5;
	public static final int ST_COMMA_SYMBOL = 6;
	public static final int ST_ASSIGN_SYMBOL = 7;
	public static final int ST_MINUS_SYMBOL = 8;
	public static final int ST_PLUS_SYMBOL = 9;
	public static final int ST_TIMES_SYMBOL = 10;
	public static final int ST_DIVIDE_SYMBOL = 11;
	public static final int ST_MODULO_SYMBOL = 12;
	public static final int ST_OR_SYMBOL = 13;
	public static final int ST_AND_SYMBOL = 14;
	public static final int ST_OPEN_PARENTHESIS_SYMBOL = 15;
	public static final int ST_CLOSE_PARENTHESIS_SYMBOL = 16;
	public static final int ST_INLINE_IF_SYMBOL = 17;
	public static final int ST_INLINE_FI_SYMBOL = 18;
	public static final int ST_INLINE_THEN_SYMBOL = 19;
	public static final int ST_INLINE_ELSE_SYMBOL = 20;
	public static final int ST_LT_SYMBOL = 21;
	public static final int ST_LE_SYMBOL = 22;
	public static final int ST_EQ_SYMBOL = 23;
	public static final int ST_GE_SYMBOL = 24;
	public static final int ST_GT_SYMBOL = 25;
	public static final int ST_NE_SYMBOL = 26;
	public static final int ST_READ_SYMBOL = 27;
	public static final int ST_WRITE_SYMBOL = 28;
	
	private static final int ST_FIXED_SYMBOLS = 100;
	private static final int ST_USER_SYMBOLS = 1000;
	private static final int ST_SYMBOLTABLE_SIZE = ST_FIXED_SYMBOLS + ST_USER_SYMBOLS;
	
	private int numberOfSymbolsInTable;
	
	private SymbolTableEntry symbolTable[];
	
	private SyntaxtreeNode nullNode;
	
	public SymbolTable()
	{
		symbolTable = new SymbolTableEntry[ST_SYMBOLTABLE_SIZE];
		nullNode = new SyntaxtreeNode();
		
		for (int i = 0; i <= ST_FIXED_SYMBOLS; i++)
		{
			symbolTable[i] = new SymbolTableEntry();
			symbolTable[i].isFixEntry = true;
			symbolTable[i].isBreackingCharSeq = false;
			symbolTable[i].type = SymbolType.reservedWordSymbol;
			symbolTable[i].nodeLink = nullNode;
			symbolTable[i].sRepresentation = "";
		}
		
		symbolTable[ST_NULL_SYMBOL].sRepresentation = "";
		symbolTable[ST_EOF_SYMBOL].sRepresentation = "" + Nase.IF_EOF_CHAR;
		setBreackingCharSeq( ST_EOF_SYMBOL );
		symbolTable[ST_DELIMITER_SYMBOL].sRepresentation = ";";
		setBreackingCharSeq( ST_DELIMITER_SYMBOL );
		symbolTable[ST_BEGIN_SYMBOL].sRepresentation = "BEGIN";
		symbolTable[ST_END_SYMBOL].sRepresentation = "END";
		symbolTable[ST_INT_TYPE_SYMBOL].sRepresentation = "INTEGER";
		symbolTable[ST_COMMA_SYMBOL].sRepresentation = ",";
		setBreackingCharSeq( ST_COMMA_SYMBOL );
		symbolTable[ST_ASSIGN_SYMBOL].sRepresentation = ":=";
		setBreackingCharSeq( ST_ASSIGN_SYMBOL );
		symbolTable[ST_MINUS_SYMBOL].sRepresentation = "-";
		setBreackingCharSeq( ST_MINUS_SYMBOL );
		symbolTable[ST_PLUS_SYMBOL].sRepresentation = "+";
		setBreackingCharSeq( ST_PLUS_SYMBOL );
		symbolTable[ST_TIMES_SYMBOL].sRepresentation = "*";
		setBreackingCharSeq( ST_TIMES_SYMBOL );
		symbolTable[ST_DIVIDE_SYMBOL].sRepresentation = "/";
		setBreackingCharSeq( ST_DIVIDE_SYMBOL );
		symbolTable[ST_MODULO_SYMBOL].sRepresentation = "%";
		setBreackingCharSeq( ST_MODULO_SYMBOL );
		symbolTable[ST_OR_SYMBOL].sRepresentation = "OR";
		symbolTable[ST_AND_SYMBOL].sRepresentation = "AND";
		symbolTable[ST_OPEN_PARENTHESIS_SYMBOL].sRepresentation = "(";
		setBreackingCharSeq( ST_OPEN_PARENTHESIS_SYMBOL );
		symbolTable[ST_CLOSE_PARENTHESIS_SYMBOL].sRepresentation = ")";
		setBreackingCharSeq( ST_CLOSE_PARENTHESIS_SYMBOL );
		symbolTable[ST_INLINE_IF_SYMBOL].sRepresentation = "IIF";
		symbolTable[ST_INLINE_FI_SYMBOL].sRepresentation = "FII";
		symbolTable[ST_INLINE_THEN_SYMBOL].sRepresentation = "?";
		setBreackingCharSeq( ST_INLINE_THEN_SYMBOL );
		symbolTable[ST_INLINE_ELSE_SYMBOL].sRepresentation = ":";
		setBreackingCharSeq( ST_INLINE_ELSE_SYMBOL );
		symbolTable[ST_LT_SYMBOL].sRepresentation = "<";
		setBreackingCharSeq( ST_LT_SYMBOL );
		symbolTable[ST_LE_SYMBOL].sRepresentation = "<=";
		setBreackingCharSeq( ST_LE_SYMBOL );
		symbolTable[ST_EQ_SYMBOL].sRepresentation = "=";
		setBreackingCharSeq( ST_EQ_SYMBOL );
		symbolTable[ST_GE_SYMBOL].sRepresentation = ">=";
		setBreackingCharSeq( ST_GE_SYMBOL );
		symbolTable[ST_GT_SYMBOL].sRepresentation = ">";
		setBreackingCharSeq( ST_GT_SYMBOL );
		symbolTable[ST_NE_SYMBOL].sRepresentation = "<>";
		setBreackingCharSeq( ST_NE_SYMBOL );
		symbolTable[ST_READ_SYMBOL].sRepresentation = "READ";
		symbolTable[ST_WRITE_SYMBOL].sRepresentation = "WRITE";
		
		numberOfSymbolsInTable = ST_FIXED_SYMBOLS;
	}
	
	private void setBreackingCharSeq(int i)
	{
		symbolTable[i].isBreackingCharSeq = true;
	}
	
	public void dumpSymbolTable()
	{
		Console.display("\r\n\r\nSymboltabledump\r\n");
		
		for (int i = 0; i <= numberOfSymbolsInTable; i++)
		{
			Console.display(i + "\t");
			
			if (symbolTable[i].isFixEntry) 
				Console.display("Fixed\t");
			else
				Console.display("User\t");
			
			if (symbolTable[i].isBreackingCharSeq) 
				Console.display("IsBreakSeq\t");
			else
				Console.display("\t\t");
			
			
			
			switch (symbolTable[i].type)
			{
				case reservedWordSymbol:
					Console.display("ResWord\t");
					break;
				case identifierSymbol:
					Console.display("Identifier\t");
					break;
				case numberSymbol:
					Console.display("ResWord\t");
					break;
				
			}
			
			Console.display("NodeLink: " + symbolTable[i].nodeLink);
			Console.display(" -- " + symbolTable[i].sRepresentation);
			Console.display("\r\n");
		}
	}
	
	public List<String> getSpecialCharList()
	{
		List<String> specialCharList = new ArrayList<String>(ST_FIXED_SYMBOLS+1);
		
		for (int i = 0; i <= ST_FIXED_SYMBOLS; i++)
		{
			if (symbolTable[i].isBreackingCharSeq)
			{
				specialCharList.add(symbolTable[i].sRepresentation);
			}
		}
		
		return specialCharList;
	}
	
	public int insertUserSymbol(String currentToken)
	{
		int newSymbolTableEntry = numberOfSymbolsInTable + 1;
			
		if (newSymbolTableEntry >= ST_SYMBOLTABLE_SIZE)
		{
			return Console.errorStop("Symboltabelle voll!");
		}
		
		symbolTable[newSymbolTableEntry] = new SymbolTableEntry();
		
		symbolTable[newSymbolTableEntry].isFixEntry = false;
		symbolTable[newSymbolTableEntry].isBreackingCharSeq = false;
		symbolTable[newSymbolTableEntry].nodeLink = nullNode;	
		symbolTable[newSymbolTableEntry].yielding = Integer.MAX_VALUE;
		
		if (isDigitSequence(currentToken))
		{
			symbolTable[newSymbolTableEntry].type = SymbolType.numberSymbol;
			symbolTable[newSymbolTableEntry].yielding = Integer.parseInt(currentToken);
		}
		else
		{
			symbolTable[newSymbolTableEntry].type = SymbolType.identifierSymbol;
		}
		
		symbolTable[newSymbolTableEntry].sRepresentation = currentToken;
		
		numberOfSymbolsInTable = newSymbolTableEntry;
		
		return newSymbolTableEntry;
	}
	
	private boolean isDigitSequence(String token)
	{
		for (int i = 0; i < token.length(); i++)
		{
			if ( !Character.isDigit(token.charAt(i)) )
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void setSymbolNodeLink(int symbol, SyntaxtreeNode node)
	{
		if (symbol <= numberOfSymbolsInTable)
		{
			symbolTable[symbol].nodeLink = node;
		}
	}
	
	public SyntaxtreeNode getSymbolNodeLink(int symbol)
	{
		if (symbol <= numberOfSymbolsInTable)
		{
			return symbolTable[symbol].nodeLink;
		}
		else
		{
			return nullNode;
		}
	}
	
	/***
	* Versucht das Token pcCurrentToken einem Symbol zuzuordnen. Liefert das Symbol; 
	* bei Nichtfinden ST_NULL_SYMBOL
	**/
	public int classifySymbol(String currentToken)
	{
		
		for (int i = 0; i <= numberOfSymbolsInTable; i++)
		{
			//Console.display("" + i + "\n");
			if (symbolTable[i].sRepresentation.equals(currentToken))
			{
				return i;
			}
		}
		
		return ST_NULL_SYMBOL;
	}
	
	public boolean isAnyIdentifierSymbol(int symbol)
	{
		if (symbol <= numberOfSymbolsInTable )
		{
			return (symbolTable[symbol].type == SymbolType.identifierSymbol);
		}
		
		return false;
	}
	
	public boolean isAnyNumericSymbol(int symbol)
	{
		if (symbol <= numberOfSymbolsInTable )
		{
			return (symbolTable[symbol].type == SymbolType.numberSymbol);
		}
		
		return false;
	}
	
}
