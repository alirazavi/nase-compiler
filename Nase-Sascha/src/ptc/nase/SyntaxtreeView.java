package ptc.nase;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ptc.nase.syntaxtree.Syntaxtree;
import ptc.nase.syntaxtree.nodes.SyntaxtreeNode;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;


public class SyntaxtreeView extends JFrame
{
	private mxGraph graph;
	private Object parent;
	
	private final int height = 50;
	private final int width = 120;
	private final int padding = 40;
	private final int paddingX = 150;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2707712944901661771L;
	
	
	class SymboltableModel extends AbstractTableModel
	{
		private SymbolTable symbolTable;
		
		public SymboltableModel(SymbolTable sSymbolTable)
		{
			symbolTable = sSymbolTable;
		}
		
		
		private static final long serialVersionUID = 1L;

			public String getColumnName(int column) 
			{
				switch (column)
				{
					case 0: return "ID";
					case 1: return "Fixed";
					case 2: return "Breaking";
					case 3: return "Type";
					case 4: return "NodeLink";
					case 5: return "String";
					default: return "";
				}
			}
	
			public int getRowCount()
			{
				return symbolTable.getSize();
			}
	
			public int getColumnCount()
			{
				return 6;
			}
	
			public boolean isCellEditable(int row, int col) {
				return false;
			}
			
			public Object getValueAt(int rowIndex, int columnIndex) 
			{
				
				switch (columnIndex)
				{
					case 0: return new Integer(rowIndex);
					case 1: return symbolTable.getEntry(rowIndex).isFixEntry;
					case 2: return symbolTable.getEntry(rowIndex).isBreackingCharSeq;
					case 3: return symbolTable.getEntry(rowIndex).type;
					case 4: 
						if ( symbolTable.getEntry(rowIndex).nodeLink != null)
							return new Integer(symbolTable.getEntry(rowIndex).nodeLink.getID());
						return null;
					case 5: return symbolTable.getEntry(rowIndex).sRepresentation;
				}
				
				return null;				
			}
			
			public Class<?> getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}
			
			
		}
	
	private JTable table;
	private JScrollPane scrollPane;
	
	public SyntaxtreeView(Syntaxtree syntaxtree, SymbolTable symbolTable)
	{
		super("Syntaxtree");
		
		graph = new mxGraph();
		parent = graph.getDefaultParent();
		
		//graph.setCellsLocked(true);
		
		graph.getModel().beginUpdate();
		drawNode(syntaxtree.getRoot(), parent, 300, 20, 1);
		graph.getModel().endUpdate();
		
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		
		SymboltableModel model = new SymboltableModel(symbolTable);
		
		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		
		getContentPane().setLayout(new BorderLayout());
		
		scrollPane.setPreferredSize(new Dimension(260, 0));
		
		//scrollPane.setLayout(new FlowLayout());
		getContentPane().add(scrollPane, BorderLayout.LINE_START );
		getContentPane().add(graphComponent, BorderLayout.CENTER);
				
		//getContentPane().setPreferredSize(new Dimension(100, 800));
		
		pack();
	}
	
	private Object drawNode(SyntaxtreeNode node, Object par, int x, int y, int depth)
	{
		Object current = graph.insertVertex(par, null, node.toString(), x, y, width, height);
		int childs = node.getNumberOfChilds();
		
		int childY = y + height + padding;
		int childStartX = ((childs-1) * width) + ((childs-1)*(int)(paddingX));
		
		childStartX = x - childStartX / 2;
		
		for (int i = 0; i < childs; i++)
		{
			Object child = drawNode(node.getChild(i), par, childStartX, childY, depth+1);
			graph.insertEdge(par, null, "", current, child);
			childStartX += width + (int)(paddingX);
		}
		
		return current;
	}

}
