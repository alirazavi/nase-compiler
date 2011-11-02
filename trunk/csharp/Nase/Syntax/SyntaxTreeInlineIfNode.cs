using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeInlineIfNode : SyntaxTreeNode
    {
        public SyntaxTreeInlineIfNode(FilePosition position, SyntaxTreeNode boolExprNode,
            SyntaxTreeNode thenIntExprNode, SyntaxTreeNode elseIntExprNode)
            : base(position)
        {
            this._children.Add(boolExprNode);
            this._children.Add(thenIntExprNode);
            this._children.Add(elseIntExprNode);
        }
    }
}
