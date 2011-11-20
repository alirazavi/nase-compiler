package symboltable;

import java.lang.reflect.Field;

public class Symbols {
	public final static int NULL_SYMBOL = 0;
	public final static int	EOF = 1;
	public final static int DELIMITER_SYMBOL = 2;
	public final static int BEGIN_SYMBOL = 3;
	public final static int	END_SYMBOL = 4;
	public final static int INT_TYPE_SYMBOL = 5;
	public final static int COMMA_SYMBOL= 6;
	public final static int ASSIGN_SYMBOL = 7;
	public final static int MINUS_SYMBOL = 8;
	public final static int PLUS_SYMBOL = 9;
	public final static int TIMES_SYMBOL = 10;
	public final static int DIVIDE_SYMBOL = 11;
	public final static int MODULO_SYMBOL = 12;
	public final static int OR_SYMBOL = 13;
	public final static int AND_SYMBOL = 14;
	public final static int OPEN_PARAENTHESIS_SYMBOL = 15;
	public final static int CLOSE_PARENTHESIS_SYMBOL = 16;
	public final static int INLINE_IF_SYMBOL = 17;
	public final static int INLINE_FI_SYMBOL = 18;
	public final static int INLINE_THEN_SYMBOL = 19;
	public final static int INLINE_ELSE_SYMBOL = 20;
	public final static int LT_SYMBOL = 21;
	public final static int LE_SYMBOL = 22;
	public final static int EQ_SYMBOL = 23;
	public final static int GE_SYMBOL = 24;
	public final static int GT_SYMBOL = 25;
	public final static int NE_SYMBOL = 26;
	public final static int READ_SYMBOL = 27;
	public final static int WRITE_SYMBOL = 28;
	public final static int BOOL_TYPE_SYMBOL = 29;
	public final static int TRUE_SYMBOL = 30;
	public final static int FALSE_SYMBOL = 31;
	
	
	public final static int INTEGER_LITERAL = 98;
	public final static int STRING_LITERAL = 99;

	
	
	
	public static String getSymbolName(int i){
		for(Field f : new Symbols().getClass().getFields()){
			try {
				if(f.getInt(null) == i)
					return f.getName();
			} catch (IllegalArgumentException e) {
				return "";
			} catch (IllegalAccessException e) {
				return "";
			}
		}
		return "";
	}
}
