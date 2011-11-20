package compiler;

import java.util.ArrayList;

import symboltable.SymbolTable;
import symboltable.Symbols;
import syntaxtree.nodes.AssignmentNode;
import syntaxtree.nodes.ConstantNode;
import syntaxtree.nodes.DeclarationNode;
import syntaxtree.nodes.DyadicOpNode;
import syntaxtree.nodes.ErrorNode;
import syntaxtree.nodes.IdentifierNode;
import syntaxtree.nodes.MondanicOpNode;
import syntaxtree.nodes.Node;
import syntaxtree.nodes.NullNode;
import syntaxtree.nodes.ProgrammNode;
import syntaxtree.nodes.ReadNode;
import syntaxtree.nodes.SequenceNode;
import syntaxtree.nodes.TypeNameNode;
import syntaxtree.nodes.WriteNode;

import compiler.scanner.ScannerWrapper;

import files.FileManager;

public class Parser {

	private ScannerWrapper scanner;	
	private Node rootNode;
	private final NullNode NULLNODE = new NullNode();
	
	private static ArrayList<Node> history = new ArrayList<Node>();
	
	
	public Parser(ScannerWrapper scanner) {
		this.scanner = scanner;
	}

	private boolean isEOFSymbol(){
		return Symbols.EOF == scanner.getCurrentSymbol();
	}
	
	private void reportGeneralSyntaxError(int line, int column, String error){
		FileManager.getInstance().getListing().write(String.format("\nSYNTAX ERROR near line %d, column %d: %s", line, column, error));	
	}
	
	private Node getErrorNode(String message){
		int column = scanner.getColumn();
		int row = scanner.getLine();
		reportGeneralSyntaxError(row, column, message);
		scanner.skipToDelimiter();
		return new ErrorNode(row, column);
	}
	
	private Node isTypeName(){
		Node type = NULLNODE;
		int column = FileManager.getInstance().getInfile().getColumn();
		int row = scanner.getLine();		
		
		if(scanner.getCurrentSymbol() == Symbols.INT_TYPE_SYMBOL ){
			type = new TypeNameNode(column, row, Symbols.INT_TYPE_SYMBOL);
			scanner.getNextSymbol();
		}
		
		return type;
	}
	
	
	private Node isDeclaration(){
		Node type;
		Node decl;
		Node declseq = NULLNODE;
		Node firstdeclseq = NULLNODE;
		
		boolean commaOccured = false;
		int column = FileManager.getInstance().getInfile().getColumn();
		int row = scanner.getLine();		
		
		if(!NULLNODE.equals(type = isTypeName())){
			do{
				int currentSymbol = scanner.getCurrentSymbol();
				
				if(SymbolTable.getInstance().isAnyIdentifierSymbol(currentSymbol)){
					decl = new DeclarationNode(column, row, currentSymbol);
					decl.addChild(type);
					if(!scanner.getNextSymbol())
						return getErrorNode("Unexpected EOF");
				} else 
					return getErrorNode("Identifier expected after type name");
				
				column = FileManager.getInstance().getInfile().getColumn();
				row = scanner.getLine();
				
				if(scanner.getCurrentSymbol() == Symbols.COMMA_SYMBOL ){
					declseq = new SequenceNode(NULLNODE, row, column);
					declseq.addChild(decl);
					if(!commaOccured){
						commaOccured = true;
						firstdeclseq = declseq;
					}
					if(!scanner.getNextSymbol())						
						return getErrorNode("Unexpected EOF");
					
				} else if(scanner.getCurrentSymbol() == Symbols.DELIMITER_SYMBOL ){
					declseq = new SequenceNode(NULLNODE, row, column);
					declseq.addChild(decl);
					if(!commaOccured)
						firstdeclseq = declseq;
				} else 
					return getErrorNode("',' or ';' expected after an identifier declaration");	
				
			}while(scanner.getCurrentSymbol() != Symbols.DELIMITER_SYMBOL );
			return firstdeclseq;
		}
		return NULLNODE;
	}
	
	
	private Node isIdentifier(){
		int symbol = scanner.getCurrentSymbol();
		if(SymbolTable.getInstance().isAnyIdentifierSymbol(symbol)){
			scanner.getNextSymbol();
			return new IdentifierNode(scanner.getLine(),scanner.getColumn(), symbol);
		}
		return NULLNODE;
	}
	
	private Node isInteger(){
		int symbol = scanner.getCurrentSymbol();
		int line = scanner.getLine();
		int column = scanner.getColumn();
		
		if(SymbolTable.getInstance().isAnyNumericSymbol(symbol)){
			scanner.getNextSymbol();
			Node constantNode = SymbolTable.getInstance().getNodeLink(symbol);
			if(constantNode == null){
				constantNode = new ConstantNode(line, column, symbol);
				SymbolTable.getInstance().setNodeLink(symbol, constantNode);
			}
			return constantNode;
		}
		return NULLNODE;
	}
	
	private boolean isRelationalOp(int sym){
		return (sym == Symbols.LT_SYMBOL
				 || sym == Symbols.LE_SYMBOL
				 || sym == Symbols.EQ_SYMBOL
				 || sym == Symbols.GT_SYMBOL
				 || sym == Symbols.GE_SYMBOL
				 || sym == Symbols.NE_SYMBOL);
	}
	
	private Node isBoolExpr(){
		Node left;
		Node right;
		Node boolExpr1;
		Node boolExpr2;
		Node boolExpr3;
		int line; 
		int column;
		int symbol;
		
		if(!NULLNODE.equals(left = isIntExpr())){
			line = scanner.getLine();
			column = scanner.getColumn();
			symbol = scanner.getCurrentSymbol();
			if(isRelationalOp(scanner.getCurrentSymbol())){
				scanner.getNextSymbol();
				if(!NULLNODE.equals(right = isIntExpr())){
					boolExpr1 = new DyadicOpNode(line, column, symbol, left, right);
				} else
					return getErrorNode("'intExpr expected after 'relationOp'");
			} else
				return getErrorNode("'relationOp' expected after 'intExpr'");
		} else
			return NULLNODE;
		
		int opSymbol = scanner.getCurrentSymbol();
		while(  opSymbol == Symbols.AND_SYMBOL
				 || opSymbol == Symbols.OR_SYMBOL){
			
			line = scanner.getLine();
			column = scanner.getColumn();
			scanner.getNextSymbol();
			
			if(!NULLNODE.equals(left = isIntExpr())){
				symbol = scanner.getCurrentSymbol();
				if(isRelationalOp(scanner.getCurrentSymbol())){
					scanner.getNextSymbol();
					if(!NULLNODE.equals(right = isIntExpr())){
						boolExpr2 = new DyadicOpNode(line, column, symbol, left, right);
						boolExpr3 = new DyadicOpNode(	line, column, opSymbol, boolExpr1, boolExpr2);
						boolExpr1 = boolExpr3;
						opSymbol = scanner.getCurrentSymbol();
					}else
						return getErrorNode("'intExpr expected after 'relationOp'");
				} else
					return getErrorNode("'relationOp' expected after 'intExpr'");
			} else
				return getErrorNode("'intExpr' expected after 'boolOp'");
		}
		return boolExpr1;
	}
	
	private Node isInlineIf(){
		int line = scanner.getLine();
		int column = scanner.getColumn();
		Node boolExpr;
		Node thenExpr;
		Node elseExpr;

		if(scanner.getCurrentSymbol() == Symbols.INLINE_IF_SYMBOL){
			scanner.getNextSymbol();
			if(!NULLNODE.equals(boolExpr = isBoolExpr())){
				if(scanner.getCurrentSymbol() == Symbols.INLINE_THEN_SYMBOL){
					scanner.getNextSymbol();
					if(!NULLNODE.equals(thenExpr = isIntExpr())){
						scanner.getNextSymbol();
						if(scanner.getCurrentSymbol() == Symbols.INLINE_ELSE_SYMBOL){
							scanner.getNextSymbol();
							if(!NULLNODE.equals(elseExpr = isIntExpr())){
								scanner.getNextSymbol();
								if(scanner.getCurrentSymbol() == Symbols.INLINE_FI_SYMBOL)
									return new InlineIfNode(line, column, boolExpr, thenExpr, elseExpr);
								else
									return getErrorNode("FII expected");
							} else
								return getErrorNode("intExpt expected");
						} else
							return getErrorNode("'FI' expected");
					} else
						return getErrorNode("intExpr expected");
				} else 
					return getErrorNode("':' expected");
			} else
				return getErrorNode("boolExpr expected");
		} else 
			return NULLNODE;
	}
	
	
	private Node isIntFactor(){
		int symbol = scanner.getCurrentSymbol();
		int line = scanner.getLine();
		int column = scanner.getColumn();
		
		Node factor;
		Node expr;
		
		if(!NULLNODE.equals(factor = isInteger())
			 || !NULLNODE.equals(factor = isIdentifier())
			 || !NULLNODE.equals(factor = isInlineIf())){
			return factor;
		} else if(scanner.getCurrentSymbol() == Symbols.OPEN_PARAENTHESIS_SYMBOL) {
			scanner.getNextSymbol();
			symbol = scanner.getCurrentSymbol();
			if(!NULLNODE.equals(expr = isIntExpr())){
				symbol = scanner.getCurrentSymbol();
				if(symbol == Symbols.CLOSE_PARENTHESIS_SYMBOL){
					scanner.getNextSymbol();
					return new ParenthesisNode(line, column, expr);
				} else
					return getErrorNode("')' expected");
			} else
				return getErrorNode("intExpr expected after '('");
		}
		return getErrorNode("intFactor expected");
	}
	
	private Node isIntTerm(){
		Node leftFactor;
		Node rightFactor;
		
		if(NULLNODE.equals(leftFactor = isIntFactor()))
			return NULLNODE;
		
		int symbol = scanner.getCurrentSymbol();
	
		while(Symbols.TIMES_SYMBOL  == symbol
				|| Symbols.DIVIDE_SYMBOL  == symbol
				|| Symbols.MODULO_SYMBOL  == symbol){
			
			int column = FileManager.getInstance().getInfile().getColumn();
			int row = scanner.getLine();			
			scanner.getNextSymbol();
			
			if(NULLNODE.equals(rightFactor = isIntFactor()))
				return getErrorNode("intFactor expected after 'multOp'");	
			else {
				Node newTerm = new DyadicOpNode(row, column, symbol, leftFactor, rightFactor);
				leftFactor = newTerm;
				symbol = scanner.getCurrentSymbol();
			}
		}
		return leftFactor;
	}
	
	private Node isIntExpr(){
		boolean minusOccured = false;
		Node leftTerm;
		Node rightTerm;
		Node minusPrefix;
		
		int column, row;
		
		int columnMinus = FileManager.getInstance().getInfile().getColumn();
		int rowMinus = scanner.getLine();
		if(scanner.getCurrentSymbol() == Symbols.MINUS_SYMBOL ){
			minusOccured = true;
			scanner.getNextSymbol();
		}
		
		if(NULLNODE.equals(leftTerm = isIntTerm())){
			if(!minusOccured)
				return NULLNODE;
			
			return getErrorNode("intTerm expected after '-'");	
		}
		
		if(minusOccured){
			minusPrefix = new MondanicOpNode(rowMinus, columnMinus);
			minusPrefix.addChild(leftTerm);
			minusPrefix.addUserEntry(Symbols.MINUS_SYMBOL );
		}
		
		while(Symbols.MINUS_SYMBOL  == scanner.getCurrentSymbol()
				|| Symbols.PLUS_SYMBOL  == scanner.getCurrentSymbol()){
			
			column = FileManager.getInstance().getInfile().getColumn();
			row = scanner.getLine();
			
			scanner.getNextSymbol();
			if(NULLNODE.equals(rightTerm = isIntTerm()))
				return getErrorNode("intTerm expected after 'addOp'");	
			else {
				Node newTerm = new DyadicOpNode(row, column, scanner.getCurrentSymbol(), leftTerm, rightTerm);
				leftTerm = newTerm;				
			}
		}
		return leftTerm;
	}
	
	private Node isAssignment(){
		
		Node identNode;
		Node exprNode;
		
		if(! NULLNODE.equals(identNode = isIdentifier())){			
			if(Symbols.ASSIGN_SYMBOL  == scanner.getCurrentSymbol()){
				scanner.getNextSymbol();
				if(! NULLNODE.equals(exprNode = isIntExpr())){
					Node assignment = new AssignmentNode(scanner.getLine(),scanner.getColumn());
					assignment.addChild(identNode);
					assignment.addChild(exprNode);
					return assignment;
				} else 
					return getErrorNode("IntExp expected on right hand side of ':='");
			} else 
				return getErrorNode("':=' expected");	
		}
		return NULLNODE;
	}
	
	private Node isWrite(){
		int line = scanner.getLine();
		int column = scanner.getColumn();
		
		Node ident;
		
		if(scanner.getCurrentSymbol() == Symbols.WRITE_SYMBOL ){
			scanner.getNextSymbol();
			
			if(!NULLNODE.equals(ident = isIdentifier())){
				Node writeNode = new WriteNode(line, column);
				writeNode.addChild(ident);
				return writeNode;
			} else
				return getErrorNode("Identifier expected after write");
		}
		return NULLNODE;
	}
	
	private Node isRead(){
		int line = scanner.getLine();
		int column = FileManager.getInstance().getInfile().getColumn();
		
		Node ident;
		
		if(scanner.getCurrentSymbol() == Symbols.READ_SYMBOL ){
			scanner.getNextSymbol();
			
			if(!NULLNODE.equals(ident = isIdentifier())){
				Node readNode = new ReadNode(line, column);
				readNode.addChild(ident);
				return readNode;
			} else 
				return getErrorNode("Identifier expected after read");
		}
		return NULLNODE;
	}
	
	
	private Node isStatement(){
		Node statement;	
	
		if(!NULLNODE.equals(statement = isDeclaration())
				|| !NULLNODE.equals(statement = isAssignment())
				|| !NULLNODE.equals(statement = isRead())
				|| !NULLNODE.equals(statement = isWrite())){
			
			if(Symbols.DELIMITER_SYMBOL  == scanner.getCurrentSymbol()){				
				scanner.getNextSymbol();
				return statement;
			} else
				return getErrorNode("';' expected");
			
		}	else if(isEOFSymbol()) 
			return NULLNODE;
		
		getErrorNode("Statment expected");
		return NULLNODE;
	}
	
	private Node isStatementSequence(){
		Node statementNode;
		Node statementSequenceNode = NULLNODE;
		Node firstStatementSequenceNode;
		
		int column = scanner.getColumn();
		int row = scanner.getLine();		
		
		if(!NULLNODE.equals((statementNode = isStatement()))){
			statementSequenceNode = new SequenceNode(NULLNODE, row, column);
			statementSequenceNode.addChild(statementNode);
		}
		
		firstStatementSequenceNode = statementSequenceNode;
		
		while(!NULLNODE.equals(statementNode = isStatement())){		
			column = scanner.getColumn();
			row = scanner.getLine();
			statementSequenceNode = new SequenceNode(statementSequenceNode, row, column);
			statementSequenceNode.addChild(statementNode);
			firstStatementSequenceNode.addChild(statementSequenceNode);
		}
			
		return firstStatementSequenceNode;
	}
	
	private Node isProgramm() {
		Node statementSequenceNode;
		Node programmNode = NULLNODE;

		int column = scanner.getColumn();
		int row = scanner.getLine();
		
		scanner.getNextSymbol();
		
		if(! NULLNODE.equals(statementSequenceNode = isStatementSequence())){
			if(isEOFSymbol()){
				programmNode = new ProgrammNode(null, row, column);
				programmNode.addChild(statementSequenceNode);
				return programmNode;
			} else {
				reportGeneralSyntaxError(scanner.getLine(), scanner.getColumn(), "EOF symbol at program end missed");
			}
			return NULLNODE;	
		}
		return NULLNODE;		
	}

	public void parseProgramm() {
		FileManager.getInstance().getListing().write(" ");
		
		rootNode = isProgramm(); 
		
		FileManager.getInstance().getListing().write(SymbolTable.getInstance().toString());
		FileManager.getInstance().getListing().write("SYNTAXTREE-DUMP\n---------------------\n");
		FileManager.getInstance().getListing().write(rootNode.toString());
		FileManager.getInstance().getListing().write(dumpHistory());
		
	}

	public static void addToHistory(Node node){
		history.add(node);
	}
	
	private String dumpHistory(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("NODE-HISTORY");
		buffer.append("\n---------------------\n");
		for(Node n : history)
			buffer.append(n.dumpNode()+"\n");
		
		return buffer.toString();
	}
	
}
