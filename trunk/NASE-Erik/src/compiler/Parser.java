package compiler;

import symboltable.SymbolTable;
import symboltable.Symbols;
import syntaxtree.nodes.DeclarationNode;
import syntaxtree.nodes.ErrorNode;
import syntaxtree.nodes.Node;
import syntaxtree.nodes.NullNode;
import syntaxtree.nodes.ProgrammNode;
import syntaxtree.nodes.SequenceNode;
import syntaxtree.nodes.TypeNameNode;
import files.FileManager;
import files.Infile;

public class Parser {

	private Scanner scanner;	
	private Node rootNode;
	private final NullNode NULLNODE = new NullNode();
	
	
	public Parser(Scanner s) {
		scanner = s;
	}

	private boolean isEOFSymbol(){
		return Infile.EOF == FileManager.getInstance().getInfile().getCurrentChar();
	}
	
	private void reportGeneralSyntaxError(int line, int column, String error){
		FileManager.getInstance().getListing().write(String.format("\nSYNTAX ERROR near line %d, column %d: %s", line, column, error));	
	}
	
	private Node isTypeName(){
		Node type = NULLNODE;
		int column = FileManager.getInstance().getInfile().getColumn();
		int row = FileManager.getInstance().getInfile().getLine();		
		
		if(scanner.getCurrentSymbol() == Symbols.INT_TYPE_SYMBOL.ordinal()){
			type = new TypeNameNode(column, row, Symbols.INT_TYPE_SYMBOL);
			scanner.getNextSymbol();
		}
		
		return type;
	}
	
	
	private Node isDeclaration(){
		Node error;
		Node type;
		Node decl;
		Node declseq = NULLNODE;
		Node firstdeclseq = NULLNODE;
		
		boolean commaOccured = false;
		int column = FileManager.getInstance().getInfile().getColumn();
		int row = FileManager.getInstance().getInfile().getLine();		
		
		if(!NULLNODE.equals(type = isTypeName())){
			do{
				int currentSymbol = scanner.getCurrentSymbol();
				
				if(SymbolTable.getInstance().isAnyIdentifierSymbol(currentSymbol)){
					decl = new DeclarationNode(column, row, currentSymbol);
					decl.addChild(type);
					if(!scanner.getNextSymbol()){
						reportGeneralSyntaxError(row, column, "Unexpected EOF");
						column = FileManager.getInstance().getInfile().getColumn();
						row = FileManager.getInstance().getInfile().getLine();
						return new ErrorNode(row, column);
					} 
				} else {
					column = FileManager.getInstance().getInfile().getColumn();
					row = FileManager.getInstance().getInfile().getLine();
					reportGeneralSyntaxError(row, column, "Identifier expected after type name");
					scanner.skipToDelimiter();
					return new ErrorNode(row, column);
				}
				
				column = FileManager.getInstance().getInfile().getColumn();
				row = FileManager.getInstance().getInfile().getLine();
				
				if(scanner.getCurrentSymbol() == Symbols.COMMA_SYMBOL.ordinal()){
					declseq = new SequenceNode(NULLNODE, row, column);
					declseq.addChild(decl);
					if(!commaOccured){
						commaOccured = true;
						firstdeclseq = declseq;
					}
					if(!scanner.getNextSymbol()){
						reportGeneralSyntaxError(row, column, "Unexpected EOF");
						column = FileManager.getInstance().getInfile().getColumn();
						row = FileManager.getInstance().getInfile().getLine();
						return new ErrorNode(row, column);						
					}
				} else if(scanner.getCurrentSymbol() == Symbols.DELIMITER_SYMBOL.ordinal()){
					declseq = new SequenceNode(NULLNODE, row, column);
					declseq.addChild(decl);
					if(!commaOccured)
						firstdeclseq = declseq;
				} else {
					column = FileManager.getInstance().getInfile().getColumn();
					row = FileManager.getInstance().getInfile().getLine();
					reportGeneralSyntaxError(row, column, "',' or ';' expected after an identifier declaration");
					scanner.skipToDelimiter();
					return new ErrorNode(row, column);	
				}
				
			}while(scanner.getCurrentSymbol() != Symbols.DELIMITER_SYMBOL.ordinal());
			return firstdeclseq;
		}
		return NULLNODE;
	}
	
	private Node isStatement(){
		Node statement;	
		int line;
		int column;
		
		if(! NULLNODE.equals((statement = isDeclaration()))){
			if(Symbols.DELIMITER_SYMBOL.ordinal() == scanner.getCurrentSymbol()){				
				scanner.getNextSymbol();
				return statement;
			} else {
				line = FileManager.getInstance().getInfile().getLine();
				column = FileManager.getInstance().getInfile().getColumn();
				
				reportGeneralSyntaxError(line, column, "';' expected");
				statement = new ErrorNode(line, column);
				scanner.skipToDelimiter();
				return statement;
			}
		}
		
		
		return NULLNODE;
	}
	
	private Node isStatementSequence(){
		Node statementNode;
		Node statementSequenceNode = NULLNODE;
		Node firstStatementSequenceNode;
		
		int column = FileManager.getInstance().getInfile().getColumn();
		int row = FileManager.getInstance().getInfile().getLine();		
		
		if(!NULLNODE.equals((statementNode = isStatement()))){
			statementSequenceNode = new SequenceNode(NULLNODE, row, column);
			statementSequenceNode.addChild(statementNode);
		}
		firstStatementSequenceNode = statementSequenceNode;
		
		while(!NULLNODE.equals(statementNode = isStatement())){		
			column = FileManager.getInstance().getInfile().getColumn();
			row = FileManager.getInstance().getInfile().getLine();
			statementSequenceNode = new SequenceNode(statementSequenceNode, row, column);
			statementSequenceNode.addChild(statementNode);
		}
			
		return firstStatementSequenceNode;
	}
	
	private Node isProgramm() {
		Node statementSequenceNode;
		Node programmNode = NULLNODE;

		int column = FileManager.getInstance().getInfile().getColumn();
		int row = FileManager.getInstance().getInfile().getLine();
		
		scanner.getNextSymbol();
		
		if(! NULLNODE.equals(statementSequenceNode = isStatementSequence())){
			if(isEOFSymbol()){
				programmNode = new ProgrammNode(null, row, column);
				programmNode.addChild(statementSequenceNode);
				return programmNode;
			} else {
				reportGeneralSyntaxError(FileManager.getInstance().getInfile().getLine(), FileManager.getInstance().getInfile().getColumn(), "EOF symbol at program end missed");
			}
			return NULLNODE;	
		}
		return NULLNODE;		
	}

	public void parseProgramm() {
		FileManager.getInstance().getListing().write(" ");
		
		rootNode = isProgramm(); 
		
		FileManager.getInstance().getListing().write(SymbolTable.getInstance().toString());
		FileManager.getInstance().getListing().write(rootNode.toString());

	}

}
