using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeSequenceNode : SyntaxTreeNode
    {
        public SyntaxTreeSequenceNode(FilePosition position, SyntaxTreeNode statementNode, SyntaxTreeNode prevSequenceNode)
            : base(position)
        {
            this._children.Add(statementNode);
            if (null != prevSequenceNode && prevSequenceNode is SyntaxTreeSequenceNode)
            {
                SyntaxTreeSequenceNode curNode = (SyntaxTreeSequenceNode)prevSequenceNode;
                while (curNode._children.Count >= 2)
                {
                    curNode = (SyntaxTreeSequenceNode)curNode._children[1];
                }
                curNode._children.Add(this);
            }
        }
    }
}
