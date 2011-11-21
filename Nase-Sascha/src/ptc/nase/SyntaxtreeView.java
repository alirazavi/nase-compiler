package ptc.nase;

import javax.swing.JFrame;

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
	
	public SyntaxtreeView(Syntaxtree syntaxtree)
	{
		super("Syntaxtree");
		
		graph = new mxGraph();
		parent = graph.getDefaultParent();
		
		//graph.setCellsLocked(true);
		
		graph.getModel().beginUpdate();
		drawNode(syntaxtree.getRoot(), parent, 300, 20, 1);
		graph.getModel().endUpdate();
		
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		
		
		getContentPane().add(graphComponent);
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
