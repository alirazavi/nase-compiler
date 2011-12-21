package compiler.parsers;

import symboltable.Symbols;
import syntaxtree.nodes.IfNode;
import syntaxtree.nodes.Node;

import compiler.scanner.ScannerWrapper;

public class ParserBLXIF extends ParserBLX{

	public ParserBLXIF(ScannerWrapper scanner) {
		super(scanner);		
	}
	
	/*
	 * ifStatement = IF_SYMBOL boolExpr THEN_SYMBOL statement [ ELSE_SYMBOL statement ].
	 */
	Node isIfStmt(){
		int line = scanner.getLine();
		int column = scanner.getColumn();
		
		Node boolExpr;
		Node thenStmt;
		Node elseStmt;
		
		if(Symbols.IF_SYMBOL == scanner.getCurrentSymbol()){
			scanner.getNextSymbol();
			if(!NULLNODE.equals(boolExpr = isBoolExpr())){
				if(Symbols.THEN_SYMBOL == scanner.getCurrentSymbol()){
					scanner.getNextSymbol();
					if(!NULLNODE.equals(thenStmt = isStatement())){
						scanner.getNextSymbol();
						if(Symbols.ELSE_SYMBOL == scanner.getCurrentSymbol()){
							scanner.getNextSymbol();
							if(!NULLNODE.equals(elseStmt = isStatement()))
								return new IfNode(line, column, boolExpr, thenStmt, elseStmt);
							else
								return getErrorNode("Statment expected after ELSE");
						} else
							return new IfNode(line, column, boolExpr, thenStmt);
					} else 
						return getErrorNode("Statement expected after THEN");
				} else
					return getErrorNode("THEN expected");			
			} else
				return getErrorNode("boolExpr expected");
		}
		
		return NULLNODE;
	}
	
	/*
	 * statement = declarationSequence | ifStatement | [ block | assignment | read | write ] DELIMITER_SYMBOL.
	 */
	@Override
	Node isStatement(){
		Node statement;
		
		if(!NULLNODE.equals(statement = isDeclarationSequence())
				|| !NULLNODE.equals(statement = isIfStmt()))
			return statement;
		
		if(!NULLNODE.equals(statement = isBlock())
				|| !NULLNODE.equals(statement = isAssignment())
				|| !NULLNODE.equals(statement = isRead())
				|| !NULLNODE.equals(statement = isWrite())){
			
			if(Symbols.DELIMITER_SYMBOL == scanner.getCurrentSymbol()){
				scanner.getNextSymbol();
				return statement;
			} else
				return getErrorNode("';' expected");
		}
		
		if(isEOFSymbol())
			return NULLNODE;
		
		return NULLNODE;
	}
	
}
