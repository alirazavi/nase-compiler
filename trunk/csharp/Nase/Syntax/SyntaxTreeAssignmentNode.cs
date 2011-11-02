using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeAssignmentNode : SyntaxTreeNode
    {
        public SyntaxTreeAssignmentNode(FilePosition position, SyntaxTreeNode identNode, SyntaxTreeNode intExprNode)
            : base(position)
        {
            this._children.Add(identNode);
            this._children.Add(intExprNode);
        }
    }
}
