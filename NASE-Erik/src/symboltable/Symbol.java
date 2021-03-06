/**
 * 
 */
package symboltable;

import syntaxtree.nodes.Node;

/**
 * @author student
 *
 */
public class Symbol {
	
	private String stringRepresentation;
	private boolean breakingCharSeq;
	private int yielding;
	private boolean fixed;
	private SymbolType type;
	private Node nodeLink;
	private int symbols;

	
	/**
	 * @param stringRepresentation
	 * @param breakingCharSeq
	 * @param yielding
	 * @param fixed
	 */
	public Symbol(String stringRepresentation, boolean breakingCharSeq,
			int yielding, boolean fixed, SymbolType type, Node nodeLink, int symbols) {
		super();
		this.stringRepresentation = stringRepresentation;
		this.breakingCharSeq = breakingCharSeq;
		this.yielding = yielding;
		this.fixed = fixed;
		this.type = type;
		this.nodeLink = nodeLink;
		this.symbols = symbols;
	}

	/**
	 * @return the stringRepresentation
	 */
	public String getStringRepresentation() {
		return stringRepresentation;
	}

	/**
	 * @param stringRepresentation the stringRepresentation to set
	 */
	public void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

	/**
	 * @return the breakingCharSeq
	 */
	public boolean isBreakingCharSeq() {
		return breakingCharSeq;
	}

	/**
	 * @param breakingCharSeq the breakingCharSeq to set
	 */
	public void setBreakingCharSeq(boolean breakingCharSeq) {
		this.breakingCharSeq = breakingCharSeq;
	}

	/**
	 * @return the yielding
	 */
	public int getYielding() {
		return yielding;
	}

	/**
	 * @param yielding the yielding to set
	 */
	public void setYielding(int yielding) {
		this.yielding = yielding;
	}

	/**
	 * @return the fixed
	 */
	public boolean isFixed() {
		return fixed;
	}

	/**
	 * @param fixed the fixed to set
	 */
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	/**
	 * @return the type
	 */
	public SymbolType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(SymbolType type) {
		this.type = type;
	}

	/**
	 * @return the nodeLink
	 */
	public Node getNodeLink() {
		return nodeLink;
	}

	/**
	 * @param nodeLink the nodeLink to set
	 */
	public void setNodeLink(Node nodeLink) {
		this.nodeLink = nodeLink;
	}

	/**
	 * @return the symbols
	 */
	public int getSymbols() {
		return symbols;
	}

	/**
	 * @param symbols the symbols to set
	 */
	public void setSymbols(int symbols) {
		this.symbols = symbols;
	}

	@Override
	public String toString() {	
		String id = (nodeLink == null) ? "null" : ""+nodeLink.getId();
		return String.format("number: %5d\tfixed: %-5b\tBreaking Sequence: %-5b\ttype: %-20s\tNodeLink: %s\t Value: %-10s", symbols, fixed, breakingCharSeq, type.name(), id, stringRepresentation);
	}
	
	
	
}
