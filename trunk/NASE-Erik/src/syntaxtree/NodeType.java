package syntaxtree;

public enum NodeType {

	ERROR_NODE ("errorNode"), PROGRAM_NODE ("programmNode"), SEQ_NODE("sequenceNode"), DECL_NODE("declarationNode"),
	TYPE_NODE("typeNode"), STMT_NODE(""), ASSIGN_NODE("assignmentNode"), READ_NODE("readNode"), WRITE_NODE("writeNode"), 
	IDENT_NODE("identifierNode"), VALUE_NODE ("valueNode"), MONA_OP_NODE("operatorNode"), DYAD_OP_NODE("operatorNode"),
	PARANTHESIS_NODE("paranthesisNode"), INLINE_IF_NODE("inlineIfNode"), INVALID_NODE("INVALID_NODE_TYPE");
	
	private final String nodeName;
	
	private NodeType(String nodeName){
		this.nodeName = nodeName;
	}
	
	public String toString(){
		return this.nodeName;
	}
}
